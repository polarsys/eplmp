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


import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import org.polarsys.eplmp.core.admin.WorkspaceFrontOptions;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.admin.WorkspaceBackOptions;
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
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IWorkspaceManagerLocal.class)
@Stateless(name = "WorkspaceManagerBean")
public class WorkspaceManagerBean implements IWorkspaceManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

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

    private static final Logger LOGGER = Logger.getLogger(WorkspaceManagerBean.class.getName());

    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    @Override
    public long getDiskUsageInWorkspace(String workspaceId) throws AccountNotFoundException {
        Account account = new AccountDAO(em).loadAccount(contextManager.getCallerPrincipalLogin());
        return new WorkspaceDAO(new Locale(account.getLanguage()), em).getDiskUsageForWorkspace(workspaceId);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Asynchronous
    public void deleteWorkspace(String workspaceId) {

        Workspace workspace;
        Account admin = null;
        Exception exceptionThrown = null;

        try {
            WorkspaceDAO workspaceDAO = new WorkspaceDAO(em, storageManager);
            workspace = workspaceDAO.loadWorkspace(workspaceId);
            admin = workspace.getAdmin();

            String callerLogin = contextManager.getCallerPrincipalLogin();

            boolean isAllowedToDeleteWorkspace =
                    contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID) ||
                            workspace.getAdmin().getLogin().equals(callerLogin);

            if (isAllowedToDeleteWorkspace) {
                workspaceDAO.removeWorkspace(workspace);
                indexerManager.deleteWorkspaceIndex(workspaceId);
                mailerManager.sendWorkspaceDeletionNotification(admin, workspaceId);
            } else {
                User user = userManager.whoAmI(workspaceId);
                LOGGER.log(Level.SEVERE, "Caller (" + user.getLogin() + ") is not authorized to delete workspace : (" + workspaceId + ")");
                throw new AccessRightException(new Locale(user.getLanguage()), user);
            }

        } catch (UserNotFoundException | UserNotActiveException | AccessRightException | WorkspaceNotEnabledException e) {
            LOGGER.log(Level.SEVERE, "Caller not authorized to delete workspace : (" + workspaceId + ")", e);
            exceptionThrown = e;
        } catch (WorkspaceNotFoundException e) {
            LOGGER.log(Level.WARNING, "Attempt to delete a workspace which does not exist : (" + workspaceId + ")", e);
            exceptionThrown = e;
        } catch (StorageException | IOException e) {
            LOGGER.log(Level.SEVERE, "Unhandled Exception deleting workspace " + workspaceId, e);
            exceptionThrown = e;
        } catch (FolderNotFoundException | AccountNotFoundException | EntityConstraintException e) {
            LOGGER.log(Level.SEVERE, "Application Exception deleting workspace " + workspaceId, e);
            exceptionThrown = e;
        }

        if (null != exceptionThrown && null != admin) {
            mailerManager.sendWorkspaceDeletionErrorNotification(admin, workspaceId);
        }

    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public Workspace changeAdmin(String workspaceId, String login) throws WorkspaceNotFoundException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException, NotAllowedException {
        Workspace workspace = new WorkspaceDAO(em).loadWorkspace(workspaceId);
        Account account = new AccountDAO(em).loadAccount(login);

        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID) || workspace.getAdmin().getLogin().equals(contextManager.getCallerPrincipalLogin())) {
            User[] users = userManager.getUsers(workspaceId);
            if(Arrays.stream(users).noneMatch(u -> u.getLogin().equals(login))) {
                User user = userManager.whoAmI(workspaceId);
                throw new NotAllowedException(new Locale(user.getLanguage()), "NotAllowedException70");
            }
            workspace.setAdmin(account);
        } else {
            User user = userManager.whoAmI(workspaceId);
            throw new AccessRightException(new Locale(user.getLanguage()), user);
        }

        return workspace;
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public Workspace enableWorkspace(String workspaceId, boolean enabled) throws WorkspaceNotFoundException {
        Workspace workspace = new WorkspaceDAO(em).loadWorkspace(workspaceId);
        workspace.setEnabled(enabled);
        return workspace;
    }


    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Workspace createWorkspace(String pID, Account pAdmin, String pDescription, boolean pFolderLocked) throws WorkspaceAlreadyExistsException, FolderAlreadyExistsException, UserAlreadyExistsException, CreationException, NotAllowedException {
        if (!NamingConvention.correct(pID)) {
            throw new NotAllowedException(new Locale(pAdmin.getLanguage()), "NotAllowedException9", pID);
        }
        OperationSecurityStrategy workspaceCreationStrategy = platformOptionsManager.getWorkspaceCreationStrategy();
        Workspace workspace = new Workspace(pID, pAdmin, pDescription, pFolderLocked);
        workspace.setEnabled(workspaceCreationStrategy.equals(OperationSecurityStrategy.NONE));
        new WorkspaceDAO(em).createWorkspace(workspace);
        User userToCreate = new User(workspace, pAdmin);
        UserDAO userDAO = new UserDAO(new Locale(pAdmin.getLanguage()), em);
        userDAO.createUser(userToCreate);
        userDAO.addUserMembership(workspace, userToCreate);

        try {
            indexerManager.createWorkspaceIndex(pID);
        } catch (Exception e) { // TODO review exception thrown
            throw new WorkspaceAlreadyExistsException(new Locale(pAdmin.getLanguage()), workspace);
        }

        return workspace;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Workspace getWorkspace(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException {

        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
            return new WorkspaceDAO(em).loadWorkspace(workspaceId);
        }

        String login = contextManager.getCallerPrincipalLogin();

        User[] users = new UserDAO(em).getUsers(login);
        Account account = new AccountDAO(em).loadAccount(login);
        Locale locale = new Locale(account.getLanguage());

        Workspace workspace = null;
        for (User user : users) {
            if (user.getWorkspace().getId().equals(workspaceId)) {
                workspace = user.getWorkspace();
                break;
            }
        }

        if (workspace == null) {
            throw new WorkspaceNotFoundException(locale, workspaceId);
        }

        return workspace;
    }


    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public WorkspaceFrontOptions getWorkspaceFrontOptions(String workspaceId) throws AccountNotFoundException, WorkspaceNotFoundException {

        WorkspaceFrontOptions settings = new WorkspaceDAO(em).loadWorkspaceFrontOptions(workspaceId);
        if (contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
            return settings;
        }

        String login = contextManager.getCallerPrincipalLogin();

        User[] users = new UserDAO(em).getUsers(login);
        Account account = new AccountDAO(em).loadAccount(login);
        Locale locale = new Locale(account.getLanguage());

        Workspace workspace = null;
        for (User user : users) {
            if (user.getWorkspace().getId().equals(workspaceId)) {
                workspace = user.getWorkspace();
                break;
            }
        }

        if (workspace == null) {
            throw new WorkspaceNotFoundException(locale, workspaceId);
        }
        return settings;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void updateWorkspaceFrontOptions(WorkspaceFrontOptions pWorkspaceFrontOptions) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException {
        String workspaceId = pWorkspaceFrontOptions.getWorkspace().getId();
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        WorkspaceDAO workspaceDAO = new WorkspaceDAO(locale, em);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        pWorkspaceFrontOptions.setWorkspace(workspace);
        new WorkspaceDAO(new Locale(account.getLanguage()), em).updateWorkspaceFrontOptions(pWorkspaceFrontOptions);
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public Workspace updateWorkspace(String workspaceId, String description, boolean isFolderLocked) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException {
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        WorkspaceDAO workspaceDAO = new WorkspaceDAO(locale, em);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);

        workspace.setDescription(description);
        workspace.setFolderLocked(isFolderLocked);

        return workspace;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public void updateWorkspaceBackOptions(WorkspaceBackOptions pWorkspaceBackOptions) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        String workspaceId = pWorkspaceBackOptions.getWorkspace().getId();
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        WorkspaceDAO workspaceDAO = new WorkspaceDAO(locale, em);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        pWorkspaceBackOptions.setWorkspace(workspace);
        new WorkspaceDAO(new Locale(account.getLanguage()), em).updateWorkspaceBackOptions(pWorkspaceBackOptions);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public WorkspaceBackOptions getWorkspaceBackOptions(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        WorkspaceBackOptions workspaceBackOptions =  new WorkspaceDAO(locale, em).loadWorkspaceBackOptions(workspaceId);

        Workspace workspace = em.find(Workspace.class, workspaceId);
        if(workspaceBackOptions == null){
            workspaceBackOptions =new WorkspaceBackOptions(workspace);
        }

        return workspaceBackOptions;
    }

}
