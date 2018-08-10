/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/
package org.polarsys.eplmp.server.documents;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.gcm.GCMAccount;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.core.workflow.Activity;
import org.polarsys.eplmp.core.workflow.Task;
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.Workflow;
import org.polarsys.eplmp.server.CheckActivity;
import org.polarsys.eplmp.server.dao.DocumentRevisionDAO;
import org.polarsys.eplmp.server.dao.SubscriptionDAO;
import org.polarsys.eplmp.server.dao.WorkflowDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IDocumentWorkflowManagerLocal.class)
@Stateless(name = "DocumentWorkflowManagerBean")
public class DocumentWorkflowManagerBean implements IDocumentWorkflowManagerLocal {

    @Inject
    private EntityManager em;

    @Inject
    private DocumentRevisionDAO documentRevisionDAO;

    @Inject
    private SubscriptionDAO subscriptionDAO;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IDocumentManagerLocal documentManager;

    @Inject
    private INotifierLocal mailer;

    @Inject
    private IGCMSenderLocal gcmNotifier;

    @Inject
    private WorkflowDAO workflowDAO;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public Workflow getCurrentWorkflow(DocumentRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, DocumentRevisionNotFoundException, AccessRightException, WorkflowNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(documentRevisionKey.getDocumentMaster().getWorkspace());
        if (!documentManager.canUserAccess(user, documentRevisionKey)) {
            throw new AccessRightException(user);
        }
        DocumentRevision docR = documentRevisionDAO.loadDocR(documentRevisionKey);
        return docR.getWorkflow();
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public Workflow[] getAbortedWorkflow(DocumentRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, DocumentRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(documentRevisionKey.getDocumentMaster().getWorkspace());
        if (!documentManager.canUserAccess(user, documentRevisionKey)) {
            throw new AccessRightException(user);
        }

        DocumentRevision docR = documentRevisionDAO.loadDocR(documentRevisionKey);
        List<Workflow> abortedWorkflowList = docR.getAbortedWorkflows();
        return abortedWorkflowList.toArray(new Workflow[abortedWorkflowList.size()]);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @CheckActivity
    @Override
    public DocumentRevision approveTaskOnDocument(String workspaceId, TaskKey pTaskKey, DocumentRevisionKey documentRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, AccessRightException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(documentRevisionKey.getWorkspaceId());

        DocumentRevision documentRevision = documentManager.getDocumentRevision(documentRevisionKey);
        Task task = documentRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(pTaskKey)).findFirst().get();

        Workflow workflow = task.getActivity().getWorkflow();
        DocumentRevision docR = checkTaskAccess(user, task);

        int previousStep = workflow.getCurrentStep();
        task.approve(user, pComment, docR.getLastIteration().getIteration(), pSignature);
        int currentStep = workflow.getCurrentStep();

        if (previousStep != currentStep) {

            Collection<User> subscribers = subscriptionDAO.getStateChangeEventSubscribers(docR);
            if (!subscribers.isEmpty()) {
                mailer.sendStateNotification(docR.getWorkspaceId(), subscribers, docR);
            }

            GCMAccount[] gcmAccounts = subscriptionDAO.getStateChangeEventSubscribersGCMAccount(docR);
            if (gcmAccounts.length != 0) {
                gcmNotifier.sendStateNotification(gcmAccounts, docR);
            }
        }

        Collection<Task> runningTasks = workflow.getRunningTasks();
        runningTasks.forEach(Task::start);

        em.flush();
        mailer.sendApproval(docR.getWorkspaceId(), runningTasks, docR);
        return docR;
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @CheckActivity
    @Override
    public DocumentRevision rejectTaskOnDocument(String workspaceId, TaskKey pTaskKey, DocumentRevisionKey documentRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, AccessRightException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(documentRevisionKey.getWorkspaceId());

        DocumentRevision documentRevision = documentManager.getDocumentRevision(documentRevisionKey);
        Task task = documentRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(pTaskKey)).findFirst().get();

        DocumentRevision docR = checkTaskAccess(user, task);

        task.reject(user, pComment, docR.getLastIteration().getIteration(), pSignature);

        // Relaunch Workflow ?
        Activity currentActivity = task.getActivity();
        Activity relaunchActivity = currentActivity.getRelaunchActivity();

        if (currentActivity.isStopped() && relaunchActivity != null) {
            relaunchWorkflow(docR, relaunchActivity.getStep());
            em.flush();
            // Send mails for running tasks
            mailer.sendApproval(docR.getWorkspaceId(), docR.getWorkflow().getRunningTasks(), docR);
            // Send notification for relaunch
            mailer.sendDocumentRevisionWorkflowRelaunchedNotification(docR.getWorkspaceId(), docR);
        }
        return docR;
    }

    /**
     * Check if a user can approve or reject a task
     *
     * @param user The specific user
     * @param task The specific task
     * @return The document concern by the task
     * @throws WorkflowNotFoundException If no workflow was find for this task
     * @throws NotAllowedException       If you can not make this task
     */
    private DocumentRevision checkTaskAccess(User user, Task task) throws WorkflowNotFoundException, NotAllowedException {
        Workflow workflow = task.getActivity().getWorkflow();
        DocumentRevision docR = workflowDAO.getDocumentTarget(workflow);
        if (docR == null) {
            throw new WorkflowNotFoundException(workflow.getId());
        }
        if (!task.isInProgress()) {
            throw new NotAllowedException("NotAllowedException15");
        }
        if (!task.isPotentialWorker(user)) {
            throw new NotAllowedException("NotAllowedException14");
        }
        if (!workflow.getRunningTasks().contains(task)) {
            throw new NotAllowedException("NotAllowedException15");
        }
        if (docR.isCheckedOut()) {
            throw new NotAllowedException("NotAllowedException16");
        }
        return docR;
    }

    private void relaunchWorkflow(DocumentRevision docR, int activityStep) {
        Workflow workflow = docR.getWorkflow();
        // Clone new workflow
        Workflow relaunchedWorkflow = workflowDAO.duplicateWorkflow(workflow);

        // Move aborted workflow in docR list
        workflow.abort();
        workflowDAO.removeWorkflowConstraints(workflow);
        docR.addAbortedWorkflows(workflow);
        // Set new workflow on document
        docR.setWorkflow(relaunchedWorkflow);
        // Reset some properties
        relaunchedWorkflow.relaunch(activityStep);
    }
}
