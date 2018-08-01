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
import org.polarsys.eplmp.i18n.PropertiesLoader;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("languages")
@Api(value = "languages", description = "Operations about languages", authorizations = {})
public class LanguagesResource {

    public LanguagesResource() {
    }

    @GET
    @ApiOperation(value = "Get supported languages",
            response = String.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of supported languages"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLanguages() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        PropertiesLoader.getSupportedLanguages().forEach(arrayBuilder::add);
        return Response.ok().entity(arrayBuilder.build()).build();
    }

}
