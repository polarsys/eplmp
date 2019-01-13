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

package org.polarsys.eplmp.server.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.common.*;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.notification.TagUserGroupSubscription;
import org.polarsys.eplmp.core.services.INotificationManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.rest.dto.TagSubscriptionDTO;
import org.polarsys.eplmp.server.rest.dto.UserDTO;
import org.polarsys.eplmp.server.rest.dto.UserGroupDTO;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.MockitoAnnotations.initMocks;

public class UserGroupResourceTest {

    @InjectMocks
    private UserGroupResource userGroupResource = new UserGroupResource();

    @Mock
    private IUserManagerLocal userManager;

    @Mock
    private INotificationManagerLocal notificationManager;
    private String workspaceId = "wks";
    private Workspace workspace = new Workspace(workspaceId);
    private String groupId = "bar";


    @Before
    public void setup() throws Exception {
        initMocks(this);
        userGroupResource.init();
    }

    @Test
    public void getGroupsTest() throws ApplicationException {
        UserGroup group = new UserGroup(workspace, groupId);
        UserGroup[] groups = new UserGroup[]{group};
        Mockito.when(userManager.getUserGroups(workspaceId))
                .thenReturn(groups);
        UserGroupDTO[] result = userGroupResource.getGroups(workspaceId);
        Assert.assertEquals(groups.length, result.length);
        Assert.assertEquals(groups[0].getId(), result[0].getId());
        Assert.assertEquals(groups[0].getWorkspaceId(), result[0].getWorkspaceId());
    }

    @Test
    public void getTagSubscriptionsForGroupTest() throws ApplicationException {

        Tag tag1 = new Tag(workspace, "tag1");
        Tag tag2 = new Tag(workspace, "tag2");

        UserGroup group1 = new UserGroup(workspace, "group1");
        UserGroup group2 = new UserGroup(workspace, "group2");

        TagUserGroupSubscription tagSubscription1 = new TagUserGroupSubscription(tag1, group1);
        TagUserGroupSubscription tagSubscription2 = new TagUserGroupSubscription(tag2, group2);

        List<TagUserGroupSubscription> subscriptions = Arrays.asList(
                tagSubscription1, tagSubscription2
        );

        Mockito.when(notificationManager.getTagUserGroupSubscriptionsByGroup(workspaceId, groupId))
                .thenReturn(subscriptions);

        TagSubscriptionDTO[] subscriptionsDTO = userGroupResource.getTagSubscriptionsForGroup(workspaceId, groupId);
        Assert.assertEquals(subscriptions.size(), subscriptionsDTO.length);

    }

    @Test
    public void getUsersInGroupTest() throws ApplicationException {
        UserGroup group = new UserGroup(workspace, groupId);
        Set<User> users = new HashSet<>();
        Account account = new Account("foo");
        User user = new User(workspace, account);
        users.add(user);
        group.setUsers(users);
        Mockito.when(userManager.getUserGroup(new UserGroupKey(workspaceId, groupId)))
                .thenReturn(group);
        UserDTO[] result = userGroupResource.getUsersInGroup(workspaceId, groupId);
        Assert.assertEquals(1, result.length);

        Assert.assertEquals(user.getLogin(), result[0].getLogin());
    }

    @Test
    public void updateUserGroupSubscriptionTest() throws ApplicationException{
        String tagName = "foo";
        TagSubscriptionDTO subDTO = new TagSubscriptionDTO();
        subDTO.setOnIterationChange(true);
        subDTO.setOnStateChange(true);
        Mockito.when(notificationManager.createOrUpdateTagUserGroupSubscription(workspaceId,
                groupId, tagName, subDTO.isOnIterationChange(), subDTO.isOnStateChange()))
        .thenReturn(null);
        Response res = userGroupResource.updateUserGroupSubscription(workspaceId, groupId, tagName, subDTO);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());

        // cannot cover full method, as the UnsupportedEncodingException cannot be thrown
        // ...
    }

    @Test
    public void deleteUserGroupSubscriptionTest() throws ApplicationException {
        String tagName = "whatever";
        Mockito.doNothing().when(notificationManager)
                .removeTagUserGroupSubscription(workspaceId, groupId, tagName);
        Response response = userGroupResource.deleteUserGroupSubscription(workspaceId, groupId, tagName);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

}
