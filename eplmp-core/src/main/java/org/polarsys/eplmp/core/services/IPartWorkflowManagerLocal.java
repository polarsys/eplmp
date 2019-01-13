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

import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PartRevisionKey;
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.Workflow;

/**
 *
 * @author Taylor Labejof
 * @version 2.0, 15/10/14
 * @since   V2.0
 */
public interface IPartWorkflowManagerLocal {
    Workflow getCurrentWorkflow(PartRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    Workflow[] getAbortedWorkflow(PartRevisionKey documentRevisionKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    PartRevision approveTaskOnPart(String pWorkspaceId, TaskKey pTaskKey, PartRevisionKey partRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException;
    PartRevision rejectTaskOnPart(String pWorkspaceId, TaskKey pTaskKey, PartRevisionKey partRevisionKey, String pComment, String pSignature) throws WorkspaceNotFoundException, TaskNotFoundException, NotAllowedException, UserNotFoundException, UserNotActiveException, WorkflowNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException;
}
