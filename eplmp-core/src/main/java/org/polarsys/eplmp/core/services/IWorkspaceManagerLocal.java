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

import org.polarsys.eplmp.core.admin.WorkspaceBackOptions;
import org.polarsys.eplmp.core.admin.WorkspaceFrontOptions;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;

/**
 * @author Morgan Guimard
 * @version 2.0, 13/09/14
 * @since V1.0
 */
public interface IWorkspaceManagerLocal {
    long getDiskUsageInWorkspace(String workspaceId) throws AccountNotFoundException;

    void deleteWorkspace(String workspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException;

    Workspace changeAdmin(String workspaceId, String login) throws WorkspaceNotFoundException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException, NotAllowedException;

    Workspace enableWorkspace(String workspaceId, boolean enabled) throws WorkspaceNotFoundException;

    void updateWorkspaceBackOptions(WorkspaceBackOptions pWorkspaceBackOptions) throws WorkspaceNotFoundException, AccountNotFoundException, AccessRightException;

    WorkspaceBackOptions getWorkspaceBackOptions(String workspaceId) throws AccountNotFoundException, WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    Workspace getWorkspace(String pWorkspaceId) throws WorkspaceNotFoundException, AccountNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;
    Workspace createWorkspace(String pID, Account pAdmin, String pDescription, boolean pFolderLocked) throws FolderAlreadyExistsException, UserAlreadyExistsException, WorkspaceAlreadyExistsException, CreationException,  NotAllowedException;
    Workspace updateWorkspace(String workspaceId, String description, boolean isFolderLocked) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;
    void updateWorkspaceFrontOptions(WorkspaceFrontOptions pWorkspaceFrontOptions) throws AccessRightException, AccountNotFoundException, WorkspaceNotFoundException;
    WorkspaceFrontOptions getWorkspaceFrontOptions(String workspaceId) throws AccountNotFoundException, WorkspaceNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;

}
