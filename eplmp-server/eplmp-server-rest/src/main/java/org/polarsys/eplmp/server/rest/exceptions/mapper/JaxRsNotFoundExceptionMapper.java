/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server.rest.exceptions.mapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Morgan Guimard
 */
@Provider
public class JaxRsNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    public JaxRsNotFoundExceptionMapper() {
    }

    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .header("Reason-Phrase", "Resource does not exists")
                .entity("404")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
