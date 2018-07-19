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

import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.ListOfValues;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.ILOVManagerLocal;
import org.polarsys.eplmp.server.rest.dto.ListOfValuesDTO;
import io.swagger.annotations.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lebeaujulien on 03/03/15.
 */

@RequestScoped
@Api(hidden = true, value = "listOfValues", description = "Operations about ListOfValues",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class LOVResource {

    @Inject
    private ILOVManagerLocal lovManager;

    private Mapper mapper;

    public LOVResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @ApiOperation(value = "Get a list of ListOfValues for given parameters",
            response = ListOfValuesDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of ListOfValuesDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLOVs(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {

        List<ListOfValuesDTO> LOVDTOs = new ArrayList<>();
        List<ListOfValues> LOVs = lovManager.findLOVFromWorkspace(workspaceId);

        for (ListOfValues lov : LOVs) {
            ListOfValuesDTO lovDTO = mapper.map(lov, ListOfValuesDTO.class);
            lovDTO.setDeletable(lovManager.isLOVDeletable(new ListOfValuesKey(lov.getWorkspaceId(), lov.getName())));
            LOVDTOs.add(lovDTO);
        }
        return Response.ok(new GenericEntity<List<ListOfValuesDTO>>((List<ListOfValuesDTO>) LOVDTOs) {
        }).build();
    }

    @POST
    @ApiOperation(value = "Create a new ListOfValues",
            response = ListOfValuesDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful retrieval of created ListOfValuesDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLOV(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "LOV to create") ListOfValuesDTO lovDTO)
            throws ListOfValuesAlreadyExistsException, CreationException, UnsupportedEncodingException,
            UserNotFoundException, AccessRightException, UserNotActiveException, WorkspaceNotFoundException,
            WorkspaceNotEnabledException {

        ListOfValues lov = mapper.map(lovDTO, ListOfValues.class);
        lovManager.createLov(workspaceId, lov.getName(), lov.getValues());
        return Response.created(URI.create(URLEncoder.encode(lov.getName(), "UTF-8"))).entity(lovDTO).build();
    }

    @GET
    @ApiOperation(value = "Get a ListOfValues from the given parameters",
            response = ListOfValuesDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful retrieval of ListOfValuesDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ListOfValuesDTO getLOV(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Name") @PathParam("name") String name)
            throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {

        ListOfValuesKey lovKey = new ListOfValuesKey(workspaceId, name);
        ListOfValues lov = lovManager.findLov(lovKey);
        return mapper.map(lov, ListOfValuesDTO.class);
    }

    @PUT
    @Path("/{name}")
    @ApiOperation(value = "Update a ListOfValues",
            response = ListOfValuesDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful retrieval of updated ListOfValuesDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ListOfValuesDTO updateLOV(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Name") @PathParam("name") String name,
            @ApiParam(required = true, value = "LOV to update") ListOfValuesDTO lovDTO)
            throws ListOfValuesNotFoundException, ListOfValuesAlreadyExistsException, CreationException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {

        ListOfValuesKey lovKey = new ListOfValuesKey(workspaceId, name);
        ListOfValues lov = mapper.map(lovDTO, ListOfValues.class);

        ListOfValues updatedLOV = lovManager.updateLov(lovKey, lov.getName(), workspaceId, lov.getValues());
        return mapper.map(updatedLOV, ListOfValuesDTO.class);
    }

    @DELETE
    @Path("/{name}")
    @ApiOperation(value = "Delete a ListOfValues",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful deletion of ListOfValuesDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLOV(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Name") @PathParam("name") String name)
            throws ListOfValuesNotFoundException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, EntityConstraintException, WorkspaceNotEnabledException {
        ListOfValuesKey lovKey = new ListOfValuesKey(workspaceId, name);
        lovManager.deleteLov(lovKey);
        return Response.noContent().build();
    }
}
