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
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.services.IWorkflowManagerLocal;
import org.polarsys.eplmp.core.workflow.Role;
import org.polarsys.eplmp.core.workflow.RoleKey;
import org.polarsys.eplmp.server.rest.dto.RoleDTO;
import org.polarsys.eplmp.server.rest.dto.UserDTO;
import org.polarsys.eplmp.server.rest.dto.UserGroupDTO;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Morgan Guimard
 */
public class RoleResourceTest {

    @InjectMocks
    private RoleResource roleResource = new RoleResource();

    @Mock
    private IWorkflowManagerLocal roleService;

    private String workspaceId = "wks";
    private Workspace workspace = new Workspace("wks");

    @Before
    public void setup(){
        initMocks(this);
        roleResource.init();
    }

    @Test
    public void getRolesInWorkspaceTest() throws ApplicationException{
        Role role = new Role();
        role.setWorkspace(workspace);
        role.setName("role");
        Role[] roles = new Role[]{role};

        Mockito.when(roleService.getRoles(workspaceId))
                .thenReturn(roles);
        Response res = roleResource.getRolesInWorkspace(workspaceId);

        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList result = (ArrayList) entity;
        Assert.assertEquals(roles.length, result.size());
        Object o = result.get(0);
        Assert.assertTrue(o.getClass().isAssignableFrom(RoleDTO.class));
        RoleDTO roleDTO = (RoleDTO) o;
        Assert.assertEquals(role.getName(), roleDTO.getName());
        Assert.assertEquals(role.getWorkspace().getId(), roleDTO.getWorkspaceId());
    }

    @Test
    public void getRolesInUseInWorkspaceTest() throws ApplicationException{
        Role role = new Role();
        role.setWorkspace(workspace);
        role.setName("role");
        Role[] roles = new Role[]{role};

        Mockito.when(roleService.getRolesInUse(workspaceId))
                .thenReturn(roles);
        Response res = roleResource.getRolesInUseInWorkspace(workspaceId);

        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList result = (ArrayList) entity;
        Assert.assertEquals(roles.length, result.size());
        Object o = result.get(0);
        Assert.assertTrue(o.getClass().isAssignableFrom(RoleDTO.class));
        RoleDTO roleDTO = (RoleDTO) o;
        Assert.assertEquals(role.getName(), roleDTO.getName());
        Assert.assertEquals(role.getWorkspace().getId(), roleDTO.getWorkspaceId());
    }

    @Test
    public void createRoleTest() throws ApplicationException{
        Role role = new Role();
        role.setWorkspace(workspace);
        role.setName("role");

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("role");
        roleDTO.setWorkspaceId(workspaceId);
        UserDTO user = new UserDTO();
        String login ="foo";
        user.setLogin(login);
        List<UserDTO> users = Collections.singletonList(user);
        roleDTO.setDefaultAssignedUsers(users);

        UserGroupDTO group = new UserGroupDTO();
        String groupId = "bar";
        group.setId(groupId);
        List<UserGroupDTO> groups = Collections.singletonList(group);
        roleDTO.setDefaultAssignedGroups(groups);
        List<String> userLogins = Collections.singletonList(login);
        List<String> userGroupIds  = Collections.singletonList(groupId);

        Mockito.when(roleService.createRole(roleDTO.getName(), roleDTO.getWorkspaceId(), userLogins, userGroupIds))
                .thenReturn(role);

        Response res = roleResource.createRole(workspaceId, roleDTO);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());

    }

    @Test
    public void updateRoleTest() throws ApplicationException{
        Role role = new Role();
        role.setWorkspace(workspace);
        role.setName("role");

        RoleKey roleKey = new RoleKey(role.getWorkspace().getId(), role.getName());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("role");
        roleDTO.setWorkspaceId(workspaceId);
        UserDTO user = new UserDTO();
        String login ="foo";
        user.setLogin(login);
        List<UserDTO> users = Collections.singletonList(user);
        roleDTO.setDefaultAssignedUsers(users);

        UserGroupDTO group = new UserGroupDTO();
        String groupId = "bar";
        group.setId(groupId);
        List<UserGroupDTO> groups = Collections.singletonList(group);
        roleDTO.setDefaultAssignedGroups(groups);
        List<String> userLogins = Collections.singletonList(login);
        List<String> userGroupIds  = Collections.singletonList(groupId);

        Mockito.when(roleService.updateRole(roleKey, userLogins, userGroupIds))
                .thenReturn(role);

        Response res = roleResource.updateRole(workspaceId, role.getName(), roleDTO);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

    }

    @Test
    public void deleteRoleTest() throws ApplicationException{
        RoleKey roleKey = new RoleKey(workspaceId, "role");
        Mockito.doNothing().when(roleService).deleteRole(roleKey);
        Response res = roleResource.deleteRole(workspaceId, roleKey.getName());
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), res.getStatus());
    }

}
