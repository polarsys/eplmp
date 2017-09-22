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

import org.polarsys.eplmp.server.rest.exceptions.UnMatchingUuidException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taylor LABEJOF
 */
@Provider
public class UnMatchingUuidExceptionMapper implements ExceptionMapper<UnMatchingUuidException> {
    private static final Logger LOGGER = Logger.getLogger(UnMatchingUuidExceptionMapper.class.getName());

    public UnMatchingUuidExceptionMapper() {
    }

    @Override
    public Response toResponse(UnMatchingUuidException e) {
        LOGGER.log(Level.FINE, null, e);
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .build();
    }
}
