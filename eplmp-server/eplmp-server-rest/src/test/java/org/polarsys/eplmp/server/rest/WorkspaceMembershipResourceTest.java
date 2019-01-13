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
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.security.WorkspaceUserGroupMembership;
import org.polarsys.eplmp.core.security.WorkspaceUserMembership;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.rest.dto.WorkspaceUserGroupMemberShipDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceUserMemberShipDTO;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.MockitoAnnotations.initMocks;

public class WorkspaceMembershipResourceTest {

    @InjectMocks
    private WorkspaceMembershipResource workspaceMembershipResource = new WorkspaceMembershipResource();

    @Mock
    private IUserManagerLocal userManager;

    private String workspaceId = "wks";
    private String login = "foo";
    private String groupId = "bar";
    private Workspace workspace = new Workspace(workspaceId);
    private Account account = new Account(login);
    private User user= new User(workspace, account);
    private UserGroup group = new UserGroup(workspace, groupId);

    @Before
    public void setup() throws Exception {
        initMocks(this);
        workspaceMembershipResource.init();
    }

    @Test
    public void getWorkspaceUserMemberShipsTest() throws ApplicationException {
        WorkspaceUserMembership membership = new WorkspaceUserMembership(workspace, user);
        WorkspaceUserMembership[] list = new WorkspaceUserMembership[]{membership};
        Mockito.when(userManager.getWorkspaceUserMemberships(workspaceId))
                .thenReturn(list);
        WorkspaceUserMemberShipDTO[] workspaceUserMemberShips = workspaceMembershipResource.getWorkspaceUserMemberShips(workspaceId);
        Assert.assertNotNull(workspaceUserMemberShips);
        Assert.assertEquals(list.length, workspaceUserMemberShips.length);
    }

    @Test
    public void getWorkspaceSpecificUserMemberShipsTest() throws ApplicationException {
        WorkspaceUserMembership membership = new WorkspaceUserMembership(workspace, user);
        Mockito.when(userManager.getWorkspaceSpecificUserMemberships(workspaceId))
                .thenReturn(membership);
        WorkspaceUserMemberShipDTO workspaceSpecificUserMemberShips = workspaceMembershipResource.getWorkspaceSpecificUserMemberShips(workspaceId);
        Assert.assertNotNull(workspaceSpecificUserMemberShips);
        Assert.assertEquals(login, workspaceSpecificUserMemberShips.getMember().getLogin());
    }

    @Test
    public void getWorkspaceUserGroupMemberShipsTest() throws ApplicationException {
        WorkspaceUserGroupMembership membership = new WorkspaceUserGroupMembership(workspace, group);
        WorkspaceUserGroupMembership[] list = new WorkspaceUserGroupMembership[]{membership};
        Mockito.when(userManager.getWorkspaceUserGroupMemberships(workspaceId))
                .thenReturn(list);
        WorkspaceUserGroupMemberShipDTO[] workspaceUserGroupMemberShips = workspaceMembershipResource.getWorkspaceUserGroupMemberShips(workspaceId);
        Assert.assertEquals(list.length, workspaceUserGroupMemberShips.length);
    }

    @Test
    public void getWorkspaceSpecificUserGroupMemberShipsTest() throws ApplicationException {
        WorkspaceUserGroupMembership membership = new WorkspaceUserGroupMembership(workspace, group);
        WorkspaceUserGroupMembership[] list = new WorkspaceUserGroupMembership[]{membership, null};
        Mockito.when(userManager.getWorkspaceSpecificUserGroupMemberships(workspaceId))
                .thenReturn(list);
        Response res = workspaceMembershipResource.getWorkspaceSpecificUserGroupMemberShips(workspaceId);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList resultList = (ArrayList) entity;
        Assert.assertEquals(1, resultList.size());
        Object o = resultList.get(0);
        Assert.assertNotNull(o);
        Assert.assertTrue(o.getClass().isAssignableFrom(WorkspaceUserGroupMemberShipDTO.class));
        WorkspaceUserGroupMemberShipDTO result = (WorkspaceUserGroupMemberShipDTO) o;
        Assert.assertEquals(groupId,result.getMemberId());

    }

}
