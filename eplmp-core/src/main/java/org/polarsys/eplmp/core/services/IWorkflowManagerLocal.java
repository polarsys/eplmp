/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/
package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.workflow.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Florent Garin
 * @version 2.0, 11/10/14
 * @since   V1.0
 */
public interface IWorkflowManagerLocal {

    void deleteWorkflowModel(WorkflowModelKey pKey) throws WorkspaceNotFoundException, AccessRightException, WorkflowModelNotFoundException, UserNotFoundException, UserNotActiveException, EntityConstraintException, WorkspaceNotEnabledException;
    WorkflowModel getWorkflowModel(WorkflowModelKey pKey) throws WorkspaceNotFoundException, WorkflowModelNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    WorkflowModel[] getWorkflowModels(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    WorkflowModel createWorkflowModel(String pWorkspaceId, String pId, String pFinalLifeCycleState, ActivityModel[] pActivityModels) throws WorkspaceNotFoundException, AccessRightException, WorkflowModelAlreadyExistsException, UserNotFoundException, CreationException, NotAllowedException, WorkspaceNotEnabledException;
    WorkflowModel updateWorkflowModel(WorkflowModelKey workflowModelKey, String pFinalLifeCycleState, ActivityModel[] pActivityModels) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkflowModelNotFoundException, NotAllowedException, WorkflowModelAlreadyExistsException, CreationException, WorkspaceNotEnabledException;

    Role[] getRoles(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    Role[] getRolesInUse(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    Role createRole(String roleName, String workspaceId, List<String> userLogins, List<String> userGroupIds) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, RoleAlreadyExistsException, CreationException, UserGroupNotFoundException, WorkspaceNotEnabledException;
    Role updateRole(RoleKey roleKey, List<String> userLogins, List<String> userGroupIds) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, RoleNotFoundException, UserGroupNotFoundException, WorkspaceNotEnabledException;
    void deleteRole(RoleKey roleKey) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, RoleNotFoundException, EntityConstraintException, WorkspaceNotEnabledException;
    void removeUserFromAllRoleMappings(User pUser) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    void removeUserGroupFromAllRoleMappings(UserGroup pUserGroup) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    void removeACLFromWorkflow(String pWorkspaceId, String workflowModelId) throws WorkflowNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkflowModelNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    WorkflowModel updateACLForWorkflow(String pWorkspaceId, String workflowModelId, Map<String, String> userEntries, Map<String, String> groupEntries) throws WorkflowNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkflowModelNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    WorkspaceWorkflow instantiateWorkflow(String workspaceId, String id, String workflowModelId, Map<String, Collection<String>> userRoleMapping, Map<String, Collection<String>> groupRoleMapping) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, RoleNotFoundException, WorkflowModelNotFoundException, NotAllowedException, UserGroupNotFoundException, WorkspaceNotEnabledException;

    Workflow getWorkflow(String workspaceId, int workflowId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    WorkspaceWorkflow getWorkspaceWorkflow(String workspaceId, String workspaceWorkflowId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkflowNotFoundException, WorkspaceNotEnabledException;

    WorkspaceWorkflow[] getWorkspaceWorkflowList(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    Workflow[] getWorkflowAbortedWorkflowList(String workspaceId, int workflowId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    void approveTaskOnWorkspaceWorkflow(String workspaceId, TaskKey taskKey, String comment, String signature) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TaskNotFoundException, AccessRightException, WorkflowNotFoundException, NotAllowedException, WorkspaceNotEnabledException;

    void rejectTaskOnWorkspaceWorkflow(String workspaceId, TaskKey taskKey, String comment, String signature) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TaskNotFoundException, AccessRightException, WorkflowNotFoundException, NotAllowedException, WorkspaceNotEnabledException;

    void deleteWorkspaceWorkflow(String workspaceId, String workspaceWorkflowId) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
}
