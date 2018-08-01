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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.core.workflow.*;
import org.polarsys.eplmp.server.dao.DocumentRevisionDAO;
import org.polarsys.eplmp.server.dao.PartRevisionDAO;
import org.polarsys.eplmp.server.dao.TaskDAO;
import org.polarsys.eplmp.server.dao.WorkflowDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Morgan Guimard
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(ITaskManagerLocal.class)
@Stateless(name = "TaskManagerBean")
public class TaskManagerBean implements ITaskManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private DocumentRevisionDAO documentRevisionDAO;

    @Inject
    private PartRevisionDAO partRevisionDAO;

    @Inject
    private TaskDAO taskDAO;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IDocumentWorkflowManagerLocal documentWorkflowService;

    @Inject
    private IPartWorkflowManagerLocal partWorkflowService;

    @Inject
    private IWorkflowManagerLocal workflowService;

    @Inject
    private WorkflowDAO workflowDAO;

    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public TaskWrapper[] getAssignedTasksForGivenUser(String workspaceId, String userLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        userManager.checkWorkspaceReadAccess(workspaceId);

        Task[] assignedTasks = taskDAO.findAssignedTasks(workspaceId, userLogin);

        return Stream.of(assignedTasks)
                .map(task -> wrapTask(task, workspaceId))
                .filter(Objects::nonNull).toArray(TaskWrapper[]::new);
    }

    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public TaskWrapper[] getInProgressTasksForGivenUser(String workspaceId, String userLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        userManager.checkWorkspaceReadAccess(workspaceId);
        Task[] inProgressTasks = taskDAO.findInProgressTasks(workspaceId, userLogin);

        return Stream.of(inProgressTasks)
                .map(task -> wrapTask(task, workspaceId))
                .filter(Objects::nonNull).toArray(TaskWrapper[]::new);
    }

    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public TaskWrapper getTask(String workspaceId, TaskKey taskKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TaskNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        Task task = taskDAO.loadTask(taskKey);
        TaskWrapper taskWrapper = wrapTask(task, workspaceId);
        if (taskWrapper == null) {
            throw new AccessRightException(user);
        }
        return taskWrapper;
    }

    @Override
    public void checkTask(String workspaceId, TaskKey taskKey) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException, TaskNotFoundException, WorkflowNotFoundException, NotAllowedException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        Task task = taskDAO.loadTask(taskKey);
        Workflow workflow = task.getActivity().getWorkflow();
        DocumentRevision docR = workflowDAO.getDocumentTarget(workflow);
        if (docR == null) {
            throw new WorkflowNotFoundException(workflow.getId());
        }
        DocumentIteration doc = docR.getLastIteration();
        if (em.createNamedQuery("findLogByDocumentAndUserAndEvent").
                setParameter("userLogin", user.getLogin()).
                setParameter("documentWorkspaceId", doc.getWorkspaceId()).
                setParameter("documentId", doc.getId()).
                setParameter("documentVersion", doc.getVersion()).
                setParameter("documentIteration", doc.getIteration()).
                setParameter("event", "DOWNLOAD").
                getResultList().isEmpty()) {
            throw new NotAllowedException("NotAllowedException10");
        }
    }


    private TaskWrapper wrapTask(Task task, String workspaceId) {
        TaskWrapper taskWrapper = new TaskWrapper(task, workspaceId);

        DocumentRevision documentRevision = documentRevisionDAO.getWorkflowHolder(task.getActivity().getWorkflow());
        if (documentRevision != null) {
            taskWrapper.setHolderType("documents");
            taskWrapper.setHolderReference(documentRevision.getDocumentMasterId());
            taskWrapper.setHolderVersion(documentRevision.getVersion());
            taskWrapper.setTask(documentRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(task.getKey())).findFirst().get());
            return taskWrapper;
        }

        PartRevision partRevision = partRevisionDAO.getWorkflowHolder(task.getActivity().getWorkflow());
        if (partRevision != null) {
            taskWrapper.setHolderType("parts");
            taskWrapper.setHolderReference(partRevision.getPartNumber());
            taskWrapper.setHolderVersion(partRevision.getVersion());
            taskWrapper.setTask(partRevision.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(task.getKey())).findFirst().get());
            return taskWrapper;
        }

        WorkspaceWorkflow workspaceWorkflowTarget = workflowDAO.getWorkspaceWorkflowTarget(workspaceId, task.getActivity().getWorkflow());
        if (workspaceWorkflowTarget != null) {
            taskWrapper.setHolderType("workspace-workflows");
            taskWrapper.setHolderReference(workspaceWorkflowTarget.getId());
            taskWrapper.setTask(workspaceWorkflowTarget.getWorkflow().getTasks().stream().filter(pTask -> pTask.getKey().equals(task.getKey())).findFirst().get());
            return taskWrapper;
        }

        return null;
    }

}
