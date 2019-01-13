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

package org.polarsys.eplmp.server.rest.writers;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.configuration.BaselinedDocumentBinaryResourceCollection;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IDocumentBaselineManagerLocal;
import org.polarsys.eplmp.server.rest.util.DocumentBaselineFileExport;
import org.polarsys.eplmp.server.rest.util.FileExportTools;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

@Provider
public class DocumentBaselineFileExportMessageBodyWriter implements MessageBodyWriter<DocumentBaselineFileExport> {

    private static final Logger LOGGER = Logger.getLogger(DocumentBaselineFileExportMessageBodyWriter.class.getName());
    @Inject
    private IBinaryStorageManagerLocal storageManager;
    @Inject
    private IDocumentBaselineManagerLocal documentBaselineService;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(DocumentBaselineFileExport.class);
    }

    @Override
    public long getSize(DocumentBaselineFileExport documentBaselineFileExport, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(DocumentBaselineFileExport documentBaselineFileExport, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream)
            throws IOException, WebApplicationException {

        ZipOutputStream zs = new ZipOutputStream(outputStream);

        try {
            List<BaselinedDocumentBinaryResourceCollection> binaryResourceCollections = documentBaselineService.getBinaryResourcesFromBaseline(documentBaselineFileExport.getWorkspaceId(), documentBaselineFileExport.getBaselineId());
            for (BaselinedDocumentBinaryResourceCollection collection : binaryResourceCollections) {
                for (BinaryResource binaryResource : collection.getAttachedFiles()) {
                    addToZip(collection, binaryResource, zs);
                }
            }
        } catch (UserNotFoundException | UserNotActiveException | WorkspaceNotFoundException | WorkspaceNotEnabledException | BaselineNotFoundException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

        zs.close();
    }

    private void addToZip(BaselinedDocumentBinaryResourceCollection collection, BinaryResource binaryResource, ZipOutputStream zs) {
        try {
            String folderName = collection.getRootFolderName() + "/attachedFiles";
            addToZipFile(binaryResource, folderName, zs);
        } catch (IOException | StorageException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong while adding file to zip", e);
        }
    }

    public void addToZipFile(BinaryResource binaryResource, String folderName, ZipOutputStream zos)
            throws IOException, StorageException {

        try (InputStream binaryResourceInputStream = storageManager.getBinaryResourceInputStream(binaryResource)) {
            FileExportTools.addToZipFile(binaryResourceInputStream, binaryResource.getName(), folderName, zos);
        }
    }
}
