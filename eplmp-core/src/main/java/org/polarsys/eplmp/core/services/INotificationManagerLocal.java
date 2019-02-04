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


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.notification.TagUserGroupSubscription;
import org.polarsys.eplmp.core.notification.TagUserSubscription;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Florent Garin
 */
public interface INotificationManagerLocal {

    TagUserSubscription subscribeToTagEvent(String pWorkspaceId, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TagNotFoundException, WorkspaceNotEnabledException;
    void unsubscribeToTagEvent(String pWorkspaceId, String pLabel) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    TagUserSubscription createOrUpdateTagUserSubscription(String pWorkspaceId, String pLogin, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, TagNotFoundException, WorkspaceNotEnabledException;
    void removeTagUserSubscription(String pWorkspaceId, String pLogin, String pLabel) throws AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    void removeAllTagSubscriptions(String pWorkspaceId, String pLabel) throws TagNotFoundException, AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    void removeAllTagUserSubscriptions(String pWorkspaceId, String pLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void removeAllTagUserGroupSubscriptions(String pWorkspaceId, String pGroupId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, UserGroupNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void removeAllSubscriptions(String pWorkspaceId, String pLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    TagUserGroupSubscription createOrUpdateTagUserGroupSubscription(String pWorkspaceId, String pId, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, TagNotFoundException, UserGroupNotFoundException, WorkspaceNotEnabledException;
    void removeTagUserGroupSubscription(String pWorkspaceId, String pId, String pLabel) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    List<TagUserGroupSubscription> getTagUserGroupSubscriptionsByGroup(String pWorkspaceId, String pId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, UserGroupNotFoundException, WorkspaceNotEnabledException;
    List<TagUserSubscription> getTagUserSubscriptionsByUser(String pWorkspaceId, String pLogin) throws AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    Collection<User> getSubscribersForTag(String pWorkspaceId, String pLabel);
}
