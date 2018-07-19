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

import io.swagger.annotations.*;
import org.polarsys.eplmp.core.exceptions.PlatformHealthException;
import org.polarsys.eplmp.core.services.IPlatformHealthManagerLocal;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Api(value = "Platforms", description = "Operations about platform")
@Path("platform")
public class PlatformResource {

    @Inject
    private IPlatformHealthManagerLocal platformHealthManager;

    public PlatformResource() {
    }

    @PostConstruct
    public void init() {
    }

    // TODO: use DTO as response
    @GET
    @Path("health")
    @ApiOperation(value = "Get platform health status",
            response = String.class,
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Server health is ok. A JSON object is sent in the body"),
            @ApiResponse(code = 500, message = "Server health is ko or partial")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlatformHealthStatus() {
        try {
            long before=System.currentTimeMillis();
            platformHealthManager.runHealthCheck();
            long after=System.currentTimeMillis();
            JsonObject result= Json.createObjectBuilder()
                    .add("status", "ok")
                    .add("executionTime", after-before)
                    .build();
            return Response.ok(result).build();
        } catch (PlatformHealthException e) {
            return Response.serverError().build();
        }
    }
}
