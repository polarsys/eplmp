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
package org.polarsys.eplmp.server.rest.exceptions.mapper;

import org.polarsys.eplmp.core.exceptions.PlatformHealthException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morgan Guimard
 */
@Provider
public class PlatformHealthExceptionMapper implements ExceptionMapper<PlatformHealthException> {
    private static final Logger LOGGER = Logger.getLogger(PlatformHealthExceptionMapper.class.getName());

    public PlatformHealthExceptionMapper() {
    }

    @Override
    public Response toResponse(PlatformHealthException e) {
        LOGGER.log(Level.WARNING, e.getMessage());
        LOGGER.log(Level.FINE, null, e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.toString())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
