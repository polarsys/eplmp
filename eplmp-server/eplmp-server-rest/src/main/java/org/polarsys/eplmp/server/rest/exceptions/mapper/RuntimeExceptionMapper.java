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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles non-mapped exceptions (Most of runtime exceptions),
 * and sends a HTTP response with code 500 and original cause in the Reason-Phrase header
 *
 * @author Morgan Guimard
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger LOGGER = Logger.getLogger(RuntimeExceptionMapper.class.getName());

    private static final String MESSAGE_PREFIX = "Unhandled system error";

    public RuntimeExceptionMapper() {
    }

    @Override
    public Response toResponse(RuntimeException e) {

        LOGGER.log(Level.SEVERE, MESSAGE_PREFIX, e);

        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        StackTraceElement[] stackTrace = cause.getStackTrace();
        StackTraceElement firstTraceElement = stackTrace[0];
        String fileName = firstTraceElement.getFileName();
        String methodName = firstTraceElement.getMethodName();
        int lineNumber = firstTraceElement.getLineNumber();
        String className = firstTraceElement.getClassName();

        String fullMessage = MESSAGE_PREFIX
                + " : "
                + className + "." + methodName
                + " threw "
                + cause.toString()
                + " in "
                + fileName
                + " at line "
                + lineNumber;

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Reason-Phrase", fullMessage)
                .entity(fullMessage)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

}
