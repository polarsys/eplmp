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


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.services.INotificationManagerLocal;
import org.polarsys.eplmp.core.services.INotifierLocal;
import org.polarsys.eplmp.server.events.*;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

/**
 * @author Florent Garin
 */
@Named
@RequestScoped
public class SubscriptionManager {

    @Inject
    private INotificationManagerLocal notificationService;

    @Inject
    private INotifierLocal mailer;

    private void onRemoveTag(@Observes @Removed TagEvent event) throws UserNotFoundException, AccessRightException, UserNotActiveException, TagNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        Tag tag = event.getObservedTag();
        notificationService.removeAllTagSubscriptions(tag.getWorkspaceId(),tag.getLabel());
    }

    private void onRemoveUser(@Observes @Removed UserEvent event) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {
        User user=event.getObservedUser();
        notificationService.removeAllSubscriptions(user.getWorkspaceId(), user.getLogin());
        notificationService.removeAllTagUserSubscriptions(user.getWorkspaceId(), user.getLogin());
    }

    private void onRemoveUserGroup(@Observes @Removed UserGroupEvent event) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, UserGroupNotFoundException, WorkspaceNotEnabledException {
        UserGroup group=event.getObservedUserGroup();
        notificationService.removeAllTagUserGroupSubscriptions(group.getWorkspaceId(), group.getId());
    }

    private void onTagItem(@Observes @Tagged TagEvent event){
        Tag t = event.getObservedTag();
        Collection<User> subscribers = notificationService.getSubscribersForTag(t.getWorkspaceId(), t.getLabel());
        DocumentRevision doc =  event.getTaggableDocument();
        PartRevision part = event.getTaggablePart();
        if(doc !=null)
            mailer.sendTaggedNotification(doc.getWorkspaceId(), subscribers, doc, event.getObservedTag());
        else if(part !=null)
            mailer.sendTaggedNotification(part.getWorkspaceId(), subscribers, part, event.getObservedTag());
    }

    private void onUntagItem(@Observes @Untagged TagEvent event){
        Tag t = event.getObservedTag();
        Collection<User> subscribers = notificationService.getSubscribersForTag(t.getWorkspaceId(),t.getLabel());
        DocumentRevision doc =  event.getTaggableDocument();
        PartRevision part = event.getTaggablePart();
        if(doc !=null)
            mailer.sendUntaggedNotification(doc.getWorkspaceId(), subscribers, doc, event.getObservedTag());
        else if(part != null)
            mailer.sendUntaggedNotification(part.getWorkspaceId(), subscribers, part, event.getObservedTag());
    }


}
