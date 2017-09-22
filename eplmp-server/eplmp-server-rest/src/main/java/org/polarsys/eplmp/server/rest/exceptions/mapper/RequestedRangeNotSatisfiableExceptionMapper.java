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

import org.polarsys.eplmp.server.rest.exceptions.RequestedRangeNotSatisfiableException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taylor LABEJOF
 */
@Provider
public class RequestedRangeNotSatisfiableExceptionMapper implements ExceptionMapper<RequestedRangeNotSatisfiableException> {
    private static final Logger LOGGER = Logger.getLogger(RequestedRangeNotSatisfiableExceptionMapper.class.getName());

    public RequestedRangeNotSatisfiableExceptionMapper() {
    }

    @Override
    public Response toResponse(RequestedRangeNotSatisfiableException e) {
        LOGGER.log(Level.SEVERE, e.getMessage());
        LOGGER.log(Level.FINE, null, e);
        return Response.status(Response.Status.REQUESTED_RANGE_NOT_SATISFIABLE)
                .header("Content-Range", "bytes */" + e.getContentRange())
                .build();
    }
}
