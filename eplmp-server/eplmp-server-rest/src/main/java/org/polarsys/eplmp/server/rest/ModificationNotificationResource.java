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
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.ModificationNotificationDTO;
import io.swagger.annotations.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Florent Garin
 */

@RequestScoped
@Api(hidden = true, value = "modificationNotifications", description = "Operations about modification notifications",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class ModificationNotificationResource {

    @Inject
    private IProductManagerLocal productService;

    public ModificationNotificationResource() {
    }

    @PUT
    @ApiOperation(value = "Acknowledge a modification notification",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful acknowledge of ModificationNotification"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("/{notificationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acknowledgeNotification(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Notification id") @PathParam("notificationId") int notificationId,
            @ApiParam(required = true, value = "Modification notification to acknowledge") ModificationNotificationDTO notificationDTO)
            throws UserNotFoundException, AccessRightException, PartRevisionNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException {

        productService.updateModificationNotification(workspaceId, notificationId, notificationDTO.getAckComment());
        return Response.noContent().build();
    }


}
