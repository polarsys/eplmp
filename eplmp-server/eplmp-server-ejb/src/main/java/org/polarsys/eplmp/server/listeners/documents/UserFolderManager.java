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
package org.polarsys.eplmp.server.listeners.documents;


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;
import org.polarsys.eplmp.server.events.Removed;
import org.polarsys.eplmp.server.events.UserEvent;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Morgan Guimard
 */
@Named
@RequestScoped
public class UserFolderManager {

    @Inject
    private IDocumentManagerLocal documentService;

    private void onRemoveUser(@Observes @Removed UserEvent event) throws EntityConstraintException, WorkspaceNotFoundException, UserNotFoundException, NotAllowedException, DocumentRevisionNotFoundException, FolderNotFoundException, AccessRightException, UserNotActiveException, WorkspaceNotEnabledException {
        User user = event.getObservedUser();
        documentService.deleteUserFolder(user);
    }

}
