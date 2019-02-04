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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.polarsys.eplmp.core.exceptions.PlatformHealthException;
import org.polarsys.eplmp.core.services.IPlatformHealthManagerLocal;
import org.polarsys.eplmp.server.rest.dto.PlatformHealthDTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Api(value = "Platforms", description = "Operations about platform")
@Path("platform")
public class PlatformResource {

    @Inject
    private IPlatformHealthManagerLocal platformHealthManager;

    public PlatformResource() {
    }

    @GET
    @Path("health")
    @ApiOperation(value = "Get platform health status",
            response = PlatformHealthDTO.class,
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Server health is ok. A JSON object is sent in the body"),
            @ApiResponse(code = 500, message = "Server health is ko or partial")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public PlatformHealthDTO getPlatformHealthStatus() throws PlatformHealthException {
        long before=System.currentTimeMillis();
        platformHealthManager.runHealthCheck();
        long after=System.currentTimeMillis();
        PlatformHealthDTO platformHealthDTO = new PlatformHealthDTO();
        platformHealthDTO.setStatus("ok");
        platformHealthDTO.setExecutionTime(after-before);
        return platformHealthDTO;
    }
}
