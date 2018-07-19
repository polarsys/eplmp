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

import org.polarsys.eplmp.core.exceptions.UserNotActiveException;
import org.polarsys.eplmp.core.exceptions.UserNotFoundException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotEnabledException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotFoundException;
import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Morgan Guimard
 */

@RequestScoped
@Api(hidden = true, value = "attributes", description = "Operations about attributes",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class AttributesResource {

    @Inject
    private IProductManagerLocal productManager;

    private Mapper mapper;

    public AttributesResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @Path("part-iterations")
    @ApiOperation(value = "Get parts instance attributes list for given workspace",
            response = InstanceAttributeDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of InstanceAttributeDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartIterationsAttributes(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        List<InstanceAttribute> attributes = productManager.getPartIterationsInstanceAttributesInWorkspace(workspaceId);
        List<InstanceAttributeDTO> attributeDTOList = new ArrayList<>();
        Set<String> seen=new HashSet<>();

        for (InstanceAttribute attribute : attributes) {
            if(attribute==null)
                continue;

            InstanceAttributeDTO dto = mapper.map(attribute, InstanceAttributeDTO.class);
            if(seen.add(dto.getType()+"."+dto.getName())) {
                dto.setValue(null);
                dto.setMandatory(false);
                dto.setLocked(false);
                dto.setLovName(null);
                attributeDTOList.add(dto);
            }
        }

        return Response.ok(new GenericEntity<List<InstanceAttributeDTO>>((List<InstanceAttributeDTO>) attributeDTOList) {
        }).build();
    }

    @GET
    @Path("path-data")
    @ApiOperation(value = "Get path data instance attributes list for given workspace",
            response = InstanceAttributeDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of InstanceAttributeDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPathDataAttributes(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        List<InstanceAttribute> attributes = productManager.getPathDataInstanceAttributesInWorkspace(workspaceId);
        List<InstanceAttributeDTO> attributeDTOList = new ArrayList<>();
        Set<String> seen=new HashSet<>();

        for (InstanceAttribute attribute : attributes) {
            if(attribute==null)
                continue;

            InstanceAttributeDTO dto = mapper.map(attribute, InstanceAttributeDTO.class);
            if(seen.add(dto.getType()+"."+dto.getName())) {
                dto.setValue(null);
                dto.setMandatory(false);
                dto.setLocked(false);
                dto.setLovName(null);
                attributeDTOList.add(dto);
            }
        }
        return Response.ok(new GenericEntity<List<InstanceAttributeDTO>>((List<InstanceAttributeDTO>) attributeDTOList) {
        }).build();
    }
}
