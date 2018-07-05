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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.TimeZone;

@RequestScoped
@Path("timezones")
@Api(value = "timezone", description = "Operations about timezones",
        authorizations = {})
public class TimeZoneResource {

    public TimeZoneResource() {
    }

    @GET
    @ApiOperation(value = "Get supported timezones",
            response = String.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of timezones. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimeZones() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String timeZone : TimeZone.getAvailableIDs()) {
            arrayBuilder.add(timeZone);
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

}
