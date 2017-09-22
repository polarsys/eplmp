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
package org.polarsys.eplmp.server.rest.exceptions.mapper;

import org.polarsys.eplmp.server.rest.exceptions.SharedResourceAccessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taylor LABEJOF
 */
@Provider
public class SharedResourceAccessExceptionMapper implements ExceptionMapper<SharedResourceAccessException>{
    private static final Logger LOGGER = Logger.getLogger(SharedResourceAccessExceptionMapper.class.getName());

    public SharedResourceAccessExceptionMapper() {
    }

    @Override
    public Response toResponse(SharedResourceAccessException e) {
        LOGGER.log(Level.WARNING, e.getMessage());
        LOGGER.log(Level.FINE, null, e);
        return Response.status(Response.Status.NOT_FOUND)
                .header("Reason-Phrase", e.getMessage())
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
