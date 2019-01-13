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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroupKey;
import org.polarsys.eplmp.core.common.UserKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.TagKey;
import org.polarsys.eplmp.core.notification.TagUserGroupSubscription;
import org.polarsys.eplmp.core.notification.TagUserGroupSubscriptionKey;
import org.polarsys.eplmp.core.notification.TagUserSubscription;
import org.polarsys.eplmp.core.notification.TagUserSubscriptionKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.INotificationManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.dao.SubscriptionDAO;
import org.polarsys.eplmp.server.dao.TagDAO;
import org.polarsys.eplmp.server.dao.UserDAO;
import org.polarsys.eplmp.server.dao.UserGroupDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * @author Florent Garin on 07/09/16
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(INotificationManagerLocal.class)
@Stateless(name = "NotificationManagerBean")
public class NotificationManagerBean implements INotificationManagerLocal {

    @Inject
    private EntityManager em;

    @Inject
    private SubscriptionDAO subscriptionDAO;

    @Inject
    private TagDAO tagDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserGroupDAO userGroupDAO;

    @Inject
    private IUserManagerLocal userManager;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public TagUserSubscription subscribeToTagEvent(String pWorkspaceId, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, TagNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        TagUserSubscription subscription = new TagUserSubscription(
                tagDAO.loadTag(new TagKey(pWorkspaceId, pLabel)),
                user,
                pOnIterationChange, pOnStateChange);
        return subscriptionDAO.saveTagUserSubscription(subscription);
    }


    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void unsubscribeToTagEvent(String pWorkspaceId, String pLabel) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        subscriptionDAO.removeTagUserSubscription(new TagUserSubscriptionKey(pWorkspaceId, user.getLogin(), pLabel));
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public TagUserSubscription createOrUpdateTagUserSubscription(String pWorkspaceId, String pLogin, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, TagNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            TagUserSubscription subscription = new TagUserSubscription(
                    tagDAO.loadTag(new TagKey(pWorkspaceId, pLabel)),
                    userDAO.loadUser(new UserKey(pWorkspaceId, pLogin)),
                    pOnIterationChange, pOnStateChange);
            return subscriptionDAO.saveTagUserSubscription(subscription);
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public TagUserGroupSubscription createOrUpdateTagUserGroupSubscription(String pWorkspaceId, String pId, String pLabel, boolean pOnIterationChange, boolean pOnStateChange) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, TagNotFoundException, UserGroupNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            TagUserGroupSubscription subscription = new TagUserGroupSubscription(
                    tagDAO.loadTag(new TagKey(pWorkspaceId, pLabel)),
                    userGroupDAO.loadUserGroup(new UserGroupKey(pWorkspaceId, pId)),
                    pOnIterationChange, pOnStateChange);
            return subscriptionDAO.saveTagUserGroupSubscription(subscription);
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeTagUserSubscription(String pWorkspaceId, String pLogin, String pLabel) throws AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeTagUserSubscription(new TagUserSubscriptionKey(pWorkspaceId, pLogin, pLabel));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeAllTagSubscriptions(String pWorkspaceId, String pLabel) throws TagNotFoundException, AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeAllTagSubscriptions(tagDAO.loadTag(new TagKey(pWorkspaceId, pLabel)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeAllTagUserSubscriptions(String pWorkspaceId, String pLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeAllTagSubscriptions(userDAO.loadUser(new UserKey(pWorkspaceId, pLogin)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeAllTagUserGroupSubscriptions(String pWorkspaceId, String pGroupId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, UserGroupNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeAllTagSubscriptions(userGroupDAO.loadUserGroup(new UserGroupKey(pWorkspaceId, pGroupId)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeAllSubscriptions(String pWorkspaceId, String pLogin) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeAllSubscriptions(userDAO.loadUser(new UserKey(pWorkspaceId, pLogin)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void removeTagUserGroupSubscription(String pWorkspaceId, String pId, String pLabel) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            subscriptionDAO.removeTagUserGroupSubscription(new TagUserGroupSubscriptionKey(pWorkspaceId, pId, pLabel));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<TagUserGroupSubscription> getTagUserGroupSubscriptionsByGroup(String pWorkspaceId, String pId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, UserGroupNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            return subscriptionDAO.getTagUserGroupSubscriptionsByGroup(userGroupDAO.loadUserGroup(new UserGroupKey(pWorkspaceId, pId)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<TagUserSubscription> getTagUserSubscriptionsByUser(String pWorkspaceId, String pLogin) throws AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(pWorkspaceId);
        // Check if it is the workspace's administrator
        if (user.isAdministrator()) {
            return subscriptionDAO.getTagUserSubscriptionsByUser(userDAO.loadUser(new UserKey(pWorkspaceId, pLogin)));
        } else {
            // Else throw a AccessRightException
            throw new AccessRightException(user);
        }
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public Collection<User> getSubscribersForTag(String pWorkspaceId, String pLabel) {
        Set<User> users=new HashSet<>();

        List<User> listUsers = em.createNamedQuery("TagUserSubscription.findSubscribersByTags", User.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setParameter("tags", Collections.singletonList(pLabel))
                .getResultList();
        users.addAll(listUsers);

        listUsers = em.createNamedQuery("TagUserGroupSubscription.findSubscribersByTags", User.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setParameter("tags", Collections.singletonList(pLabel))
                .getResultList();
        users.addAll(listUsers);

        return users;
    }
}
