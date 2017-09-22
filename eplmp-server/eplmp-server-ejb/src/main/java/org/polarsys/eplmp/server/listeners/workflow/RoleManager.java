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
package org.polarsys.eplmp.server.listeners.workflow;


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.exceptions.AccessRightException;
import org.polarsys.eplmp.core.exceptions.UserNotFoundException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotEnabledException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotFoundException;
import org.polarsys.eplmp.core.services.IWorkflowManagerLocal;
import org.polarsys.eplmp.server.events.Removed;
import org.polarsys.eplmp.server.events.UserEvent;
import org.polarsys.eplmp.server.events.UserGroupEvent;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Morgan Guimard
 */
@Named
@RequestScoped
public class RoleManager {

    @Inject
    private IWorkflowManagerLocal workflowService;

    private void onRemoveUser(@Observes @Removed UserEvent event) throws UserNotFoundException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = event.getObservedUser();
        workflowService.removeUserFromAllRoleMappings(user);
    }

    private void onRemoveUserGroup(@Observes @Removed UserGroupEvent event) throws UserNotFoundException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        UserGroup group = event.getObservedUserGroup();
        workflowService.removeUserGroupFromAllRoleMappings(group);
    }

}
