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

import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.hooks.SNSWebhookApp;
import org.polarsys.eplmp.core.hooks.SimpleWebhookApp;
import org.polarsys.eplmp.core.hooks.Webhook;

import java.util.List;

/**
 * @author Morgan Guimard
 */
public interface IWebhookManagerLocal {
    Webhook createWebhook(String workspaceId, String name, boolean active) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException, AccessRightException, AccountNotFoundException;

    List<Webhook> getWebHooks(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException;

    Webhook getWebHook(String workspaceId, int id) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException, WebhookNotFoundException;

    Webhook updateWebHook(String workspaceId, int id, String name, boolean active) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException;

    void deleteWebhook(String workspaceId, int id) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException, WebhookNotFoundException;

    SimpleWebhookApp configureSimpleWebhook(String workspaceId, int webhookId, String method, String uri, String authorization) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException;

    SNSWebhookApp configureSNSWebhook(String workspaceId, int webhookId, String topicArn, String region, String awsAccount, String awsSecret) throws WorkspaceNotFoundException, AccessRightException, WebhookNotFoundException, AccountNotFoundException;

    List<Webhook> getActiveWebHooks(String workspaceId) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
}
