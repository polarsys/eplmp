/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server;


import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import org.polarsys.eplmp.core.admin.WorkspaceBackOptions;
import org.polarsys.eplmp.core.admin.WorkspaceFrontOptions;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.core.util.NamingConvention;
import org.polarsys.eplmp.server.dao.AccountDAO;
import org.polarsys.eplmp.server.dao.UserDAO;
import org.polarsys.eplmp.server.dao.WorkspaceDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IWorkspaceManagerLocal.class)
@Stateless(name = "WorkspaceManagerBean")
public class WorkspaceManagerBean implements IWorkspaceManagerLocal {

    @Inject
    private EntityManager em;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private WorkspaceDAO workspaceDAO;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IContextManagerLocal contextManager;

    @Inject
    private INotifierLocal mailerManager;

    @Inject
    private IIndexerManagerLocal indexerManager;

    @Inject
    private IPlatformOptionsManagerLocal platformOptionsManager;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    private static final Logger LOGGER = Logger.getLogger(WorkspaceManagerBean.class.getName());

    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    @Override
    public long getDiskUsageInWorkspace(String workspaceId) throws AccountNotFoundException {
        accountDAO.loadAccount(contextManager.getCallerPrincipalLogin());
        return workspaceDAO.getDiskUsageForWorkspace(workspaceId);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public void deleteWorkspace(String workspaceId)
            throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        if (!contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
            userManager.checkAdmin(workspaceId);
        }
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        doDeleteWorkspace(workspace);
    }

    @Asynchronous
    private void doDeleteWorkspace(Workspace workspace) {
        String workspaceId = workspace.getId();
        Exception exceptionThrown = null;
        Account admin = workspace.getAdmin();
        try {
            workspaceDAO.removeWorkspace(workspace);
            storageManager.deleteWorkspaceFolder(workspaceId);
            indexerManager.deleteWorkspaceIndex(workspaceId);
            mailerManager.sendWorkspaceDeletionNotification(admin, workspaceId);
        } catch (ApplicationException e) {
            LOGGER.log(Level.SEVERE, "Application Exception deleting workspace " + workspaceId, e);
            exceptionThrown = e;
        } catch (StorageException e) {
            LOGGER.log(Level.SEVERE, "Unhandled Exception deleting workspace " + workspaceId, e);
            exceptionThrown = e;
        }
        if (null != exceptionThrown) {
            mailerManager.sendWorkspaceDeletionErrorNotification(admin, workspaceId);
        }
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public Workspace changeAdmin(String workspaceId, String login) throws WorkspaceNotFoundException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException, NotAllowedException {
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        Account account = accountDAO.loadAccount(login);

        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID) || workspace.getAdmin().getLogin().equals(contextManager.getCallerPrincipalLogin())) {
            User[] users = userManager.getUsers(workspaceId);
            if (Arrays.stream(users).noneMatch(u -> u.getLogin().equals(login))) {

                throw new NotAllowedException("NotAllowedException70");
            }
            workspace.setAdmin(account);
        } else {
            User user = userManager.whoAmI(workspaceId);
            throw new AccessRightException(user);
        }

        return workspace;
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public Workspace enableWorkspace(String workspaceId, boolean enabled) throws WorkspaceNotFoundException {
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        workspace.setEnabled(enabled);
        return workspace;
    }


    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Workspace createWorkspace(String pID, Account pAdmin, String pDescription, boolean pFolderLocked) throws WorkspaceAlreadyExistsException, FolderAlreadyExistsException, UserAlreadyExistsException, CreationException, NotAllowedException {
        if (!NamingConvention.correct(pID)) {
            throw new NotAllowedException("NotAllowedException9", pID);
        }
        OperationSecurityStrategy workspaceCreationStrategy = platformOptionsManager.getWorkspaceCreationStrategy();
        Workspace workspace = new Workspace(pID, pAdmin, pDescription, pFolderLocked);
        workspace.setEnabled(workspaceCreationStrategy.equals(OperationSecurityStrategy.NONE));
        workspaceDAO.createWorkspace(workspace);
        User userToCreate = new User(workspace, pAdmin);
        userDAO.createUser(userToCreate);
        userDAO.addUserMembership(workspace, userToCreate);

        indexerManager.createWorkspaceIndex(pID);

        return workspace;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Workspace getWorkspace(String workspaceId)
            throws WorkspaceNotFoundException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
            return workspaceDAO.loadWorkspace(workspaceId);
        } else {
            userManager.checkWorkspaceReadAccess(workspaceId);
        }

        String login = contextManager.getCallerPrincipalLogin();
        User[] users = userDAO.getUsers(login);

        Workspace workspace = null;
        for (User user : users) {
            if (user.getWorkspace().getId().equals(workspaceId)) {
                workspace = user.getWorkspace();
                break;
            }
        }

        if (workspace == null) {
            throw new WorkspaceNotFoundException(workspaceId);
        }

        return workspace;
    }


    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public WorkspaceFrontOptions getWorkspaceFrontOptions(String workspaceId)
            throws AccountNotFoundException, WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        WorkspaceFrontOptions settings = workspaceDAO.loadWorkspaceFrontOptions(workspaceId);
        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
            return settings;
        } else {
            userManager.checkWorkspaceReadAccess(workspaceId);
        }

        String login = contextManager.getCallerPrincipalLogin();

        User[] users = userDAO.getUsers(login);

        Workspace workspace = null;
        for (User user : users) {
            if (user.getWorkspace().getId().equals(workspaceId)) {
                workspace = user.getWorkspace();
                break;
            }
        }

        if (workspace == null) {
            throw new WorkspaceNotFoundException(workspaceId);
        }
        return settings;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void updateWorkspaceFrontOptions(WorkspaceFrontOptions pWorkspaceFrontOptions) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException {
        String workspaceId = pWorkspaceFrontOptions.getWorkspace().getId();
        userManager.checkAdmin(workspaceId);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        pWorkspaceFrontOptions.setWorkspace(workspace);
        workspaceDAO.updateWorkspaceFrontOptions(pWorkspaceFrontOptions);
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public Workspace updateWorkspace(String workspaceId, String description, boolean isFolderLocked) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException {
        userManager.checkAdmin(workspaceId);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        workspace.setDescription(description);
        workspace.setFolderLocked(isFolderLocked);
        return workspace;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public void updateWorkspaceBackOptions(WorkspaceBackOptions pWorkspaceBackOptions) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        String workspaceId = pWorkspaceBackOptions.getWorkspace().getId();
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        pWorkspaceBackOptions.setWorkspace(workspace);
        workspaceDAO.updateWorkspaceBackOptions(pWorkspaceBackOptions);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public WorkspaceBackOptions getWorkspaceBackOptions(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        userManager.checkAdmin(workspaceId);
        WorkspaceBackOptions workspaceBackOptions = workspaceDAO.loadWorkspaceBackOptions(workspaceId);

        Workspace workspace = em.find(Workspace.class, workspaceId);
        if (workspaceBackOptions == null) {
            workspaceBackOptions = new WorkspaceBackOptions(workspace);
        }

        return workspaceBackOptions;
    }

}
