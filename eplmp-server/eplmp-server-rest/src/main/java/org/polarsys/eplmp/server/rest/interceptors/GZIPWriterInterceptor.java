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

package org.polarsys.eplmp.server.rest.interceptors;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@Provider
@Compress
public class GZIPWriterInterceptor implements WriterInterceptor {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    @Context
    private HttpHeaders requestHeaders;

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException {


        MultivaluedMap<String, Object> responseHeaders = context.getHeaders();
        Object rangeHeader = responseHeaders.getFirst("Content-Range");

        // Use a custom header here
        // Some clients needs to know the content length in response headers in order to display a loading state
        // Browsers don't let programmers to change the default "Accept-Encoding" header, then we use a custom one.
        String acceptEncoding = requestHeaders.getHeaderString("x-accept-encoding");

        GZIPOutputStream gzipOutputStream = null;

        if (acceptEncoding != null && acceptEncoding.equals("identity")) {
            responseHeaders.add("Content-Encoding", "identity");
        } else if (rangeHeader == null) {
            responseHeaders.add("Content-Encoding", "gzip");
            responseHeaders.remove("Content-Length");
            gzipOutputStream = new GZIPOutputStream(context.getOutputStream(), DEFAULT_BUFFER_SIZE);
            context.setOutputStream(gzipOutputStream);
        }

        try {
            context.proceed();
        } finally {
            if (gzipOutputStream != null) {
                gzipOutputStream.finish();
            }
        }
    }


}
