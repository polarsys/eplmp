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
import org.mockito.*;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.AccessRightException;
import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.security.WorkspaceUserMembership;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.rest.dto.UserDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceMembership;

import javax.ws.rs.core.Response;


public class WorkspaceResourceTest {

    @InjectMocks
    WorkspaceResource workspaceResource = new WorkspaceResource();

    @Mock
    private IUserManagerLocal userManager;

    @Mock
    private UserDTO userDTO;

    @Mock
    private WorkspaceUserMembership workspaceUserMembership;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);
        workspaceResource.init();
    }

    @Test
    public void setUserAccessTest() throws EntityNotFoundException, AccessRightException {

        Mockito.when(userDTO.getMembership()).thenReturn(WorkspaceMembership.READ_ONLY);
        Mockito.when(userManager.grantUserAccess("wks-0001", userDTO.getLogin(), userDTO.getMembership() == WorkspaceMembership.READ_ONLY)).thenReturn(null);
        Response response =  workspaceResource.setUserAccess("wks-0001",userDTO);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),response.getStatus());

        Mockito.when(userDTO.getMembership()).thenReturn(null);
        response =  workspaceResource.setUserAccess("wks-0001",userDTO);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),response.getStatus());

        Mockito.when(userDTO.getMembership()).thenReturn(WorkspaceMembership.READ_ONLY);
        Mockito.when(userManager.grantUserAccess("wks-0001", userDTO.getLogin(), userDTO.getMembership() == WorkspaceMembership.READ_ONLY))
                .thenReturn(workspaceUserMembership);
        Mockito.when(workspaceUserMembership.getMember()).thenReturn(new User());
        response =  workspaceResource.setUserAccess("wks-0001",userDTO);
        Assert.assertNotNull(response.getEntity());
    }
}