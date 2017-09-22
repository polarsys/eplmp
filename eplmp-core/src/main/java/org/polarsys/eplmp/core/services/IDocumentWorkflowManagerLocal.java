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

import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.Workflow;

/**
 *
 * @author Taylor Labejof
 * @version 2.0, 15/10/14
 * @since   V2.0
 */
public interface IDocumentWorkflowManagerLocal {
    Workflow getCurrentWorkflow(DocumentRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, DocumentRevisionNotFoundException, AccessRightException, WorkflowNotFoundException, WorkspaceNotEnabledException;
    Workflow[] getAbortedWorkflow(DocumentRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, DocumentRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    DocumentRevision approveTaskOnDocument(String workspaceId, TaskKey pTaskKey, DocumentRevisionKey documentRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, AccessRightException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
    DocumentRevision rejectTaskOnDocument(String workspaceId, TaskKey pTaskKey, DocumentRevisionKey documentRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, AccessRightException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
}
