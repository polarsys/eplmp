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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.hooks.SNSWebhookApp;
import org.polarsys.eplmp.core.hooks.SimpleWebhookApp;
import org.polarsys.eplmp.core.hooks.Webhook;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.core.services.IWebhookManagerLocal;
import org.polarsys.eplmp.server.dao.WebhookDAO;
import org.polarsys.eplmp.server.dao.WorkspaceDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

/**
 * @author Morgan Guimard
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(IWebhookManagerLocal.class)
@Stateless(name = "WebhookManagerBean")
public class WebhookManagerBean implements IWebhookManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IContextManagerLocal contextManager;

    @Inject
    private IUserManagerLocal userManager;


    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Webhook createWebhook(String workspaceId, String name, boolean active)
            throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException, AccessRightException, AccountNotFoundException {
        Account account = userManager.checkAdmin(workspaceId);
        WorkspaceDAO workspaceDAO = new WorkspaceDAO(new Locale(account.getLanguage()), em);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        Webhook webhook = new Webhook(new SimpleWebhookApp(), name, active, workspace);
        new WebhookDAO(new Locale(account.getLanguage()), em).createWebhook(webhook);
        return webhook;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public List<Webhook> getWebHooks(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        Account account = userManager.checkAdmin(workspaceId);
        WebhookDAO webhookDAO = new WebhookDAO(new Locale(account.getLanguage()), em);
        return webhookDAO.loadWebhooks(workspaceId);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Webhook getWebHook(String workspaceId, int id) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException, WebhookNotFoundException {
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        Webhook webhook = new WebhookDAO(locale, em).loadWebhook(id);
        if (!webhook.getWorkspace().getId().equals(workspaceId)) {
            throw new WebhookNotFoundException(locale, id);
        }
        return webhook;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Webhook updateWebHook(String workspaceId, int id, String name, boolean active) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException {
        Webhook webHook = getWebHook(workspaceId, id);
        webHook.setActive(active);
        webHook.setName(name);
        return webHook;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public void deleteWebhook(String workspaceId, int id) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException, WebhookNotFoundException {
        Account account = userManager.checkAdmin(workspaceId);
        Locale locale = new Locale(account.getLanguage());
        WebhookDAO webhookDAO = new WebhookDAO(locale, em);
        Webhook webhook = webhookDAO.loadWebhook(id);
        if (!webhook.getWorkspace().getId().equals(workspaceId)) {
            throw new WebhookNotFoundException(locale, id);
        }
        webhookDAO.removeWebook(webhook);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public SimpleWebhookApp configureSimpleWebhook(String workspaceId, int webhookId, String method, String uri, String authorization) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException {
        Webhook webHook = getWebHook(workspaceId, webhookId);
        SimpleWebhookApp app = new SimpleWebhookApp(method, authorization, uri);
        webHook.setWebhookApp(app);
        return app;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public SNSWebhookApp configureSNSWebhook(String workspaceId, int webhookId, String topicArn, String region, String awsAccount, String awsSecret) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException {
        Webhook webHook = getWebHook(workspaceId, webhookId);
        SNSWebhookApp app = new SNSWebhookApp(topicArn, region, awsAccount, awsSecret);
        webHook.setWebhookApp(app);
        return app;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public List<Webhook> getActiveWebHooks(String workspaceId) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        WebhookDAO webhookDAO = new WebhookDAO(new Locale(user.getLanguage()), em);
        return webhookDAO.loadActiveWebhooks(workspaceId);
    }

}
