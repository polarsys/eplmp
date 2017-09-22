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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.*;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.PasswordRecoveryRequest;
import org.polarsys.eplmp.core.security.WorkspaceUserGroupMembership;
import org.polarsys.eplmp.core.security.WorkspaceUserMembership;


/**
 *
 * @author Florent Garin
 */
public interface IUserManagerLocal{

    void recoverPassword(String pPasswdRRUuid, String pPassword) throws PasswordRecoveryRequestNotFoundException;
    PasswordRecoveryRequest createPasswordRecoveryRequest(Account account);
    Workspace[] getAdministratedWorkspaces() throws AccountNotFoundException;
    Workspace[] getWorkspacesWhereCallerIsActive();

    User whoAmI(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    void addUserInWorkspace(String pWorkspaceId, String pLogin) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, UserAlreadyExistsException, FolderAlreadyExistsException, CreationException;
    Workspace removeUser(String pWorkspaceId, String login) throws UserNotFoundException, AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, FolderNotFoundException,  EntityConstraintException, UserNotActiveException, DocumentRevisionNotFoundException;

    UserGroup getUserGroup(UserGroupKey pKey) throws WorkspaceNotFoundException, UserGroupNotFoundException, UserNotFoundException, UserNotActiveException, AccountNotFoundException, WorkspaceNotEnabledException;
    UserGroup[] getUserGroups(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccountNotFoundException, WorkspaceNotEnabledException;

    UserGroup removeUserFromGroup(UserGroupKey pGroupKey, String logins) throws AccessRightException, UserGroupNotFoundException, AccountNotFoundException, WorkspaceNotFoundException;

    UserGroup createUserGroup(String pId, Workspace pWorkspace) throws UserGroupAlreadyExistsException, AccessRightException, AccountNotFoundException, CreationException;

    void passivateUserGroup(String pWorkspaceId, String groupId) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void removeUsers(String pWorkspaceId, String[] pLogins) throws UserNotFoundException, AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, FolderNotFoundException,  EntityConstraintException, UserNotActiveException, DocumentRevisionNotFoundException;

    void passivateUser(String pWorkspaceId, String login) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void removeUserGroups(String pWorkspaceId, String[] pIds) throws UserGroupNotFoundException, AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, EntityConstraintException;
    void addUserInGroup(UserGroupKey pGroupKey, String pLogin) throws AccessRightException, UserGroupNotFoundException, AccountNotFoundException, WorkspaceNotFoundException, UserAlreadyExistsException, FolderAlreadyExistsException, CreationException;
    void removeUserFromGroup(UserGroupKey pGroupKey, String[] pLogins) throws AccessRightException, UserGroupNotFoundException, AccountNotFoundException, WorkspaceNotFoundException;

    boolean hasWorkspaceWriteAccess(User user, String pWorkspaceId) throws WorkspaceNotFoundException, WorkspaceNotEnabledException;

    boolean hasCommonWorkspace(String user1, String user2);

    void grantUserAccess(String pWorkspaceId, String[] pLogins, boolean pReadOnly) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    WorkspaceUserMembership grantUserAccess(String pWorkspaceId, String login, boolean pReadOnly) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    WorkspaceUserGroupMembership grantGroupAccess(String pWorkspaceId, String groupId, boolean pReadOnly) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, UserGroupNotFoundException;

    void grantGroupAccess(String pWorkspaceId, String[] pGroupIds, boolean pReadOnly) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException, UserGroupNotFoundException;

    void activateUsers(String pWorkspaceId, String[] pLogins) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;
    void passivateUsers(String pWorkspaceId, String[] pLogins) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void activateUserGroups(String pWorkspaceId, String[] pGroupIds) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void activateUser(String pWorkspaceId, String login) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void activateUserGroup(String pWorkspaceId, String groupId) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    void passivateUserGroups(String pWorkspaceId, String[] pGroupIds) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;

    WorkspaceUserMembership getWorkspaceSpecificUserMemberships(String workspaceId) throws AccountNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    WorkspaceUserMembership[] getWorkspaceUserMemberships(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccountNotFoundException, WorkspaceNotEnabledException;
    WorkspaceUserGroupMembership[] getWorkspaceSpecificUserGroupMemberships(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, UserGroupNotFoundException, WorkspaceNotEnabledException;
    WorkspaceUserGroupMembership[] getWorkspaceUserGroupMemberships(String pWorkspaceId) throws WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, AccountNotFoundException, WorkspaceNotEnabledException;

    Account checkAdmin(String pWorkspaceId) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;
    Account checkAdmin(Workspace pWorkspace) throws AccessRightException, AccountNotFoundException;
    User checkWorkspaceReadAccess(String pWorkspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    User checkWorkspaceWriteAccess(String pWorkspaceId) throws UserNotFoundException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    UserGroup[] getUserGroupsForUser(UserKey userKey) throws UserNotFoundException;

    User[] getReachableUsers() throws AccountNotFoundException;

    User[] getUsers(String pWorkspaceId) throws WorkspaceNotFoundException, AccessRightException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;

    UserGroup createUserGroup(String pId, String workspaceId) throws UserGroupAlreadyExistsException, AccessRightException, AccountNotFoundException, CreationException, WorkspaceNotFoundException;
}
