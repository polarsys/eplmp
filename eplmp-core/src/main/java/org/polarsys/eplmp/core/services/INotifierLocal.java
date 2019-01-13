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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.workflow.Task;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflow;

import java.util.Collection;

/**
 * @author Florent Garin
 */
public interface INotifierLocal {

    // Basic account notifications
    void sendPasswordRecovery(Account account, String recoveryUUID);

    void sendCredential(Account account);

    // Admin level notifications
    void sendWorkspaceIndexationSuccess(Account account, String workspaceId, String extraMessage);

    void sendWorkspaceIndexationFailure(Account account, String workspaceId, String extraMessage);

    void sendWorkspaceDeletionNotification(Account admin, String workspaceId);

    void sendWorkspaceDeletionErrorNotification(Account admin, String workspaceId);

    void sendBulkIndexationSuccess(Account account);

    void sendBulkIndexationFailure(Account account, String failureMessage);

    // User level notifications
    void sendStateNotification(String workspaceId, Collection<User> pSubscribers, DocumentRevision pDocumentRevision);

    void sendIterationNotification(String workspaceId, Collection<User> pSubscribers, DocumentRevision pDocumentRevision);

    void sendApproval(String workspaceId, Collection<Task> pRunningTasks, DocumentRevision pDocumentRevision);

    void sendApproval(String workspaceId, Collection<Task> runningTasks, PartRevision partRevision);

    void sendApproval(String workspaceId, Collection<Task> runningTasks, WorkspaceWorkflow workspaceWorkflow);

    void sendPartRevisionWorkflowRelaunchedNotification(String workspaceId, PartRevision partRevision);

    void sendDocumentRevisionWorkflowRelaunchedNotification(String workspaceId, DocumentRevision pDocumentRevision);

    void sendWorkspaceWorkflowRelaunchedNotification(String workspaceId, WorkspaceWorkflow workspaceWorkflow);

    void sendTaggedNotification(String workspaceId, Collection<User> pSubscribers, DocumentRevision pDocR, Tag pTag);

    void sendUntaggedNotification(String workspaceId, Collection<User> pSubscribers, DocumentRevision pDocR, Tag pTag);

    void sendTaggedNotification(String workspaceId, Collection<User> pSubscribers, PartRevision pPartR, Tag pTag);

    void sendUntaggedNotification(String workspaceId, Collection<User> pSubscribers, PartRevision pPartR, Tag pTag);

}
