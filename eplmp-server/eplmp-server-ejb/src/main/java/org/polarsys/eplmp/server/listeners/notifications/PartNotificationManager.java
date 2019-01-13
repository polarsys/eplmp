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
package org.polarsys.eplmp.server.listeners.notifications;


import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.events.CheckedIn;
import org.polarsys.eplmp.server.events.PartIterationEvent;
import org.polarsys.eplmp.server.events.PartRevisionEvent;
import org.polarsys.eplmp.server.events.Removed;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Florent Garin
 */
@Named
@RequestScoped
public class PartNotificationManager {

    @Inject
    private IProductManagerLocal productService;

    private void onRemovePartIteration(@Observes @Removed PartIterationEvent event){
        PartIteration partIteration = event.getObservedPart();
        productService.removeModificationNotificationsOnIteration(partIteration.getKey());
    }

    private void onRemovePartRevision(@Observes @Removed PartRevisionEvent event){
        PartRevision partRevision = event.getObservedPart();
        productService.removeModificationNotificationsOnRevision(partRevision.getKey());
    }
    private void onCheckInPartIteration(@Observes @CheckedIn PartIterationEvent event) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, PartRevisionNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        PartIteration partIteration = event.getObservedPart();
        productService.createModificationNotifications(partIteration);
    }
}
