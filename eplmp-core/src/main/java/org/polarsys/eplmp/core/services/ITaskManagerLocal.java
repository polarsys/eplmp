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
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.TaskWrapper;

/**
 *
 * @author Morgan Guimard
 */
public interface ITaskManagerLocal {
    TaskWrapper[] getAssignedTasksForGivenUser(String workspaceId, String userLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    TaskWrapper[] getInProgressTasksForGivenUser(String workspaceId, String userLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    TaskWrapper getTask(String workspaceId, TaskKey taskKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TaskNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    void checkTask(String workspaceId, TaskKey taskKey) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException, TaskNotFoundException, WorkflowNotFoundException, NotAllowedException;
}
