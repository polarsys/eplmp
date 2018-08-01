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

import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.hooks.SNSWebhookApp;
import org.polarsys.eplmp.core.hooks.SimpleWebhookApp;
import org.polarsys.eplmp.core.hooks.Webhook;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.core.services.IWebhookManagerLocal;
import org.polarsys.eplmp.server.dao.WebhookDAO;
import org.polarsys.eplmp.server.dao.WorkspaceDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Morgan Guimard
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(IWebhookManagerLocal.class)
@Stateless(name = "WebhookManagerBean")
public class WebhookManagerBean implements IWebhookManagerLocal {

    @Inject
    private WebhookDAO webhookDAO;

    @Inject
    private WorkspaceDAO workspaceDAO;

    @Inject
    private IUserManagerLocal userManager;

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Webhook createWebhook(String workspaceId, String name, boolean active)
            throws WorkspaceNotFoundException, AccessRightException, AccountNotFoundException {
        userManager.checkAdmin(workspaceId);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);
        Webhook webhook = new Webhook(new SimpleWebhookApp(), name, active, workspace);
        webhookDAO.createWebhook(webhook);
        return webhook;
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public List<Webhook> getWebHooks(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException {
        userManager.checkAdmin(workspaceId);
        return webhookDAO.loadWebhooks(workspaceId);
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Webhook getWebHook(String workspaceId, int id) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException, WebhookNotFoundException {
        userManager.checkAdmin(workspaceId);
        Webhook webhook = webhookDAO.loadWebhook(id);
        if (!webhook.getWorkspace().getId().equals(workspaceId)) {
            throw new WebhookNotFoundException(id);
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
        userManager.checkAdmin(workspaceId);
        Webhook webhook = webhookDAO.loadWebhook(id);

        if (!webhook.getWorkspace().getId().equals(workspaceId)) {
            throw new WebhookNotFoundException(id);
        }
        webhookDAO.removeWebhook(webhook);
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
        userManager.checkWorkspaceReadAccess(workspaceId);
        return webhookDAO.loadActiveWebhooks(workspaceId);
    }

}
