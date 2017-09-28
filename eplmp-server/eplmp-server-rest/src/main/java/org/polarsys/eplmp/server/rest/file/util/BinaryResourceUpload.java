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
package org.polarsys.eplmp.server.rest.file.util;


import org.polarsys.eplmp.core.util.FileIO;

import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taylor LABEJOF
 */
public class BinaryResourceUpload {
    private static final Logger LOGGER = Logger.getLogger(BinaryResourceUpload.class.getName());

    private BinaryResourceUpload() {
        super();
    }


    /**
     * Upload a form file in a specific output
     *
     * @param outputStream BinaryResource output stream (in server vault repository)
     * @param formPart     The form part list
     * @return The length of the file uploaded
     */
    public static long uploadBinary(OutputStream outputStream, Part formPart)
            throws IOException {
        long length;
        try (InputStream in = formPart.getInputStream(); OutputStream out = outputStream) {
            length =  FileIO.copy(in, out);
        }
        return length;
    }

    /**
     * Log error & return a 500 error.
     *
     * @param e The exception which cause the error.
     * @return A 500 error.
     */
    public static Response uploadError(Exception e) {
        String message = "Error while uploading the file(s).";
        LOGGER.log(Level.SEVERE, message, e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Reason-Phrase", message)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    public static Response tryToRespondCreated(String uri) {
        try {
            return Response.created(new URI(uri)).build();
        } catch (URISyntaxException e) {
            LOGGER.log(Level.WARNING, null, e);
            return Response.ok().build();
        }
    }
}
