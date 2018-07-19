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

package org.polarsys.eplmp.server.rest;

import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.exceptions.UserNotActiveException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.security.WorkspaceUserGroupMembership;
import org.polarsys.eplmp.core.security.WorkspaceUserMembership;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.rest.dto.WorkspaceUserGroupMemberShipDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceUserMemberShipDTO;
import io.swagger.annotations.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan Guimard
 */
@RequestScoped
@Api(hidden = true, value = "workspaceMemberships", description = "Operations about workspace memberships",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class WorkspaceMembershipResource {

    @Inject
    private IUserManagerLocal userManager;

    private Mapper mapper;

    public WorkspaceMembershipResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @ApiOperation(value = "Get workspace's user memberships",
            response = WorkspaceUserMemberShipDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of WorkspaceUserMemberShipDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkspaceUserMemberShipDTO[] getWorkspaceUserMemberShips(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws EntityNotFoundException, UserNotActiveException {

        WorkspaceUserMembership[] workspaceUserMemberships = userManager.getWorkspaceUserMemberships(workspaceId);
        WorkspaceUserMemberShipDTO[] workspaceUserMemberShipDTO = new WorkspaceUserMemberShipDTO[workspaceUserMemberships.length];
        for (int i = 0; i < workspaceUserMemberships.length; i++) {
            workspaceUserMemberShipDTO[i] = mapper.map(workspaceUserMemberships[i], WorkspaceUserMemberShipDTO.class);
        }
        return workspaceUserMemberShipDTO;
    }

    @GET
    @ApiOperation(value = "Get workspace's user membership for authenticated user",
            response = WorkspaceUserMemberShipDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of WorkspaceUserMemberShipDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("users/me")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkspaceUserMemberShipDTO getWorkspaceSpecificUserMemberShips(@ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws EntityNotFoundException, UserNotActiveException {

        WorkspaceUserMembership workspaceUserMemberships = userManager.getWorkspaceSpecificUserMemberships(workspaceId);
        return mapper.map(workspaceUserMemberships, WorkspaceUserMemberShipDTO.class);
    }

    @GET
    @ApiOperation(value = "Get workspace's group membership for authenticated user",
            response = WorkspaceUserGroupMemberShipDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of WorkspaceUserGroupMemberShipDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("usergroups")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkspaceUserGroupMemberShipDTO[] getWorkspaceUserGroupMemberShips(
            @ApiParam(required = true, value = "Workspace id")  @PathParam("workspaceId") String workspaceId)
            throws EntityNotFoundException, UserNotActiveException {

        WorkspaceUserGroupMembership[] workspaceUserGroupMemberships = userManager.getWorkspaceUserGroupMemberships(workspaceId);
        WorkspaceUserGroupMemberShipDTO[] workspaceUserGroupMemberShipDTO = new WorkspaceUserGroupMemberShipDTO[workspaceUserGroupMemberships.length];
        for (int i = 0; i < workspaceUserGroupMemberships.length; i++) {
            workspaceUserGroupMemberShipDTO[i] = mapper.map(workspaceUserGroupMemberships[i], WorkspaceUserGroupMemberShipDTO.class);
        }
        return workspaceUserGroupMemberShipDTO;
    }

    @GET
    @ApiOperation(value = "Get workspace's group membership for authenticated user",
            response = WorkspaceUserGroupMemberShipDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of WorkspaceUserGroupMemberShipDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("usergroups/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkspaceSpecificUserGroupMemberShips(
            @ApiParam(required = true, value = "Workspace id")  @PathParam("workspaceId") String workspaceId)
            throws EntityNotFoundException, UserNotActiveException {

        WorkspaceUserGroupMembership[] workspaceUserGroupMemberships = userManager.getWorkspaceSpecificUserGroupMemberships(workspaceId);
        List<WorkspaceUserGroupMemberShipDTO> workspaceUserGroupMemberShipDTO = new ArrayList<>();
        for (WorkspaceUserGroupMembership workspaceUserGroupMembership : workspaceUserGroupMemberships) {
            if (workspaceUserGroupMembership != null) {
                workspaceUserGroupMemberShipDTO.add(mapper.map(workspaceUserGroupMembership, WorkspaceUserGroupMemberShipDTO.class));
            }
        }

        return Response.ok(new GenericEntity<List<WorkspaceUserGroupMemberShipDTO>>((List<WorkspaceUserGroupMemberShipDTO>) workspaceUserGroupMemberShipDTO) {
        }).build();
    }

}
