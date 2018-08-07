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
package org.polarsys.eplmp.server.products;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PartRevisionKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.INotifierLocal;
import org.polarsys.eplmp.core.services.IPartWorkflowManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.core.workflow.Activity;
import org.polarsys.eplmp.core.workflow.Task;
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.Workflow;
import org.polarsys.eplmp.server.dao.PartRevisionDAO;
import org.polarsys.eplmp.server.dao.TaskDAO;
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
@Local(IPartWorkflowManagerLocal.class)
@Stateless(name = "PartWorkflowManagerBean")
public class PartWorkflowManagerBean implements IPartWorkflowManagerLocal {

    @Inject
    private EntityManager em;

    @Inject
    private PartRevisionDAO partRevisionDAO;

    @Inject
    private TaskDAO taskDAO;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IProductManagerLocal productManager;

    @Inject
    private INotifierLocal mailer;

    @Inject
    private WorkflowDAO workflowDAO;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public Workflow getCurrentWorkflow(PartRevisionKey partRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(partRevisionKey.getPartMaster().getWorkspace());

        if (!productManager.canUserAccess(user, partRevisionKey)) {
            throw new AccessRightException(user);
        }

        PartRevision partR = partRevisionDAO.loadPartR(partRevisionKey);
        return partR.getWorkflow();
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public Workflow[] getAbortedWorkflow(PartRevisionKey partRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(partRevisionKey.getPartMaster().getWorkspace());

        if (!productManager.canUserAccess(user, partRevisionKey)) {
            throw new AccessRightException(user);
        }

        PartRevision partR = partRevisionDAO.loadPartR(partRevisionKey);
        List<Workflow> abortedWorkflowList = partR.getAbortedWorkflows();

        return abortedWorkflowList.toArray(new Workflow[abortedWorkflowList.size()]);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public PartRevision approveTaskOnPart(String pWorkspaceId, TaskKey pTaskKey, PartRevisionKey partRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);

        PartRevision partRevision = productManager.getPartRevision(partRevisionKey);
        Task task = partRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(pTaskKey)).findFirst().get();

        Workflow workflow = task.getActivity().getWorkflow();
        PartRevision partR = checkTaskAccess(user, task);

        task.approve(user, pComment, partR.getLastIteration().getIteration(), pSignature);

        Collection<Task> runningTasks = workflow.getRunningTasks();
        runningTasks.forEach(org.polarsys.eplmp.core.workflow.Task::start);

        em.flush();
        mailer.sendApproval(pWorkspaceId, runningTasks, partR);

        return partR;
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public PartRevision rejectTaskOnPart(String pWorkspaceId, TaskKey pTaskKey, PartRevisionKey partRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);

        PartRevision partRevision = productManager.getPartRevision(partRevisionKey);
        Task task = partRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(pTaskKey)).findFirst().get();

        PartRevision partR = checkTaskAccess(user, task);

        task.reject(user, pComment, partR.getLastIteration().getIteration(), pSignature);

        // Relaunch Workflow ?
        Activity currentActivity = task.getActivity();
        Activity relaunchActivity = currentActivity.getRelaunchActivity();

        if (currentActivity.isStopped() && relaunchActivity != null) {
            relaunchWorkflow(partR, relaunchActivity.getStep());
            em.flush();
            // Send mails for running tasks
            mailer.sendApproval(pWorkspaceId, partR.getWorkflow().getRunningTasks(), partR);
            // Send notification for relaunch
            mailer.sendPartRevisionWorkflowRelaunchedNotification(pWorkspaceId, partR);
        }

        return partR;
    }

    /**
     * Check if a user can approve or reject a task
     *
     * @param user The specific user
     * @param task The specific task
     * @return The part concern by the task
     * @throws WorkflowNotFoundException If no workflow was find for this task
     * @throws NotAllowedException       If you can not make this task
     */
    private PartRevision checkTaskAccess(User user, Task task) throws WorkflowNotFoundException, NotAllowedException {
        Workflow workflow = task.getActivity().getWorkflow();
        PartRevision partR = workflowDAO.getPartTarget(workflow);
        if (partR == null) {
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
        if (partR.isCheckedOut()) {
            throw new NotAllowedException("NotAllowedException17");
        }
        return partR;
    }

    private void relaunchWorkflow(PartRevision partR, int activityStep) {
        Workflow workflow = partR.getWorkflow();
        // Clone new workflow
        Workflow relaunchedWorkflow = workflowDAO.duplicateWorkflow(workflow);

        // Move aborted workflow in docR list
        workflow.abort();
        workflowDAO.removeWorkflowConstraints(workflow);
        partR.addAbortedWorkflows(workflow);
        // Set new workflow on document
        partR.setWorkflow(relaunchedWorkflow);
        // Reset some properties
        relaunchedWorkflow.relaunch(activityStep);
    }
}
