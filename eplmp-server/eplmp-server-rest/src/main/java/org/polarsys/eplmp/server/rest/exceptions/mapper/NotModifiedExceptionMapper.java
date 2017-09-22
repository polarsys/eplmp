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

import org.polarsys.eplmp.server.rest.exceptions.NotModifiedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taylor LABEJOF
 */
@Provider
public class NotModifiedExceptionMapper implements ExceptionMapper<NotModifiedException> {
    private static final Logger LOGGER = Logger.getLogger(NotModifiedExceptionMapper.class.getName());

    public NotModifiedExceptionMapper() {
    }

    @Override
    public Response toResponse(NotModifiedException e) {
        LOGGER.log(Level.FINE, null, e);
        return Response.status(Response.Status.NOT_MODIFIED)
                .expires(e.getExpireDate())
                .tag(e.getETag())
                .build();
    }
}
