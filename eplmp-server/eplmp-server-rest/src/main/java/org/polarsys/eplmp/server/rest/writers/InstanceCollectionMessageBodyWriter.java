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
package org.polarsys.eplmp.server.rest.writers;

import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.collections.InstanceCollection;
import org.polarsys.eplmp.server.rest.util.InstanceBodyWriterTools;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.vecmath.Matrix4d;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Florent Garin
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class InstanceCollectionMessageBodyWriter implements MessageBodyWriter<InstanceCollection> {

    @Inject
    private IProductManagerLocal productService;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(InstanceCollection.class);
    }

    @Override
    public long getSize(InstanceCollection t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(InstanceCollection instanceCollection, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws UnsupportedEncodingException {
        String charSet = "UTF-8";
        JsonGenerator jg = Json.createGenerator(new OutputStreamWriter(entityStream, charSet));
        jg.writeStartArray();

        Matrix4d gM = new Matrix4d();
        gM.setIdentity();
        InstanceBodyWriterTools.generateInstanceStreamWithGlobalMatrix(productService, null, gM, instanceCollection, new ArrayList<>(), jg);
        jg.writeEnd();
        jg.flush();
    }
}
