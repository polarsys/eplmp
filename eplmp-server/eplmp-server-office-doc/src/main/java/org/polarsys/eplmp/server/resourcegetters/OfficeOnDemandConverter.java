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

package org.polarsys.eplmp.server.resourcegetters;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.exceptions.ConvertedResourceException;
import org.polarsys.eplmp.core.exceptions.StorageException;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.util.FileIO;
import org.polarsys.eplmp.core.util.Tools;
import org.polarsys.eplmp.server.InternalService;
import org.polarsys.eplmp.server.converters.OnDemandConverter;
import org.polarsys.eplmp.server.extras.TitleBlockGenerator;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Florent Garin
 */
public class OfficeOnDemandConverter implements OnDemandConverter {

    private static final Logger LOGGER = Logger.getLogger(OnDemandConverter.class.getName());

    @Inject
    private FileConverter fileConverter;

    @InternalService
    @Inject
    private IBinaryStorageManagerLocal storageManager;


    @Override
    public boolean canConvert(String outputFormat, BinaryResource binaryResource) {
        return FileIO.isDocFile(binaryResource.getName()) && outputSupported(outputFormat);
    }

    private boolean outputSupported(String outputFormat) {
        return outputFormat != null && "pdf".equals(outputFormat);
    }

    @Override
    public InputStream getConvertedResource(String outputFormat, BinaryResource binaryResource, DocumentIteration docI, Locale locale) throws ConvertedResourceException {
        try {
            // TODO check for resources to be closed
            InputStream inputStream = null;

            if ("pdf".equals(outputFormat)) {
                inputStream = getPdfConvertedResource(binaryResource);
            }

            if ("documents".equals(binaryResource.getHolderType()) && docI != null) {
                LOGGER.log(Level.INFO, "Adding document information to first pages");
                return TitleBlockGenerator.addBlockTitleToPDF(inputStream, docI, locale);
            }

            return inputStream;
        } catch (StorageException | IOException e) {
            throw new ConvertedResourceException(locale, e);
        }
    }

    @Override
    public InputStream getConvertedResource(String outputFormat, BinaryResource binaryResource, PartIteration partIteration, Locale locale) throws ConvertedResourceException {
        try {
            InputStream inputStream = null;

            if ("pdf".equals(outputFormat)) {
                inputStream = getPdfConvertedResource(binaryResource);
            }

            if ("parts".equals(binaryResource.getHolderType()) && partIteration != null) {
                return TitleBlockGenerator.addBlockTitleToPDF(inputStream, partIteration, locale);
            }

            return inputStream;
        } catch (StorageException | IOException e) {
            throw new ConvertedResourceException(locale, e);
        }
    }

    private InputStream getPdfConvertedResource(BinaryResource binaryResource) throws StorageException, IOException {

        InputStream inputStream;

        String extension = FileIO.getExtension(binaryResource.getName());

        if ("pdf".equals(extension)) {
            LOGGER.log(Level.INFO, "File is already as pdf format");
            return storageManager.getBinaryResourceInputStream(binaryResource);
        }

        String pdfFileName = FileIO.getFileNameWithoutExtension(binaryResource.getName()) + ".pdf";

        if (storageManager.exists(binaryResource, pdfFileName) &&
                storageManager.getLastModified(binaryResource, pdfFileName).after(binaryResource.getLastModified())) {
            LOGGER.log(Level.INFO, "File is already converted to pdf");
            inputStream = storageManager.getGeneratedFileInputStream(binaryResource, pdfFileName);
        } else {
            LOGGER.log(Level.INFO, "Converting " + binaryResource.getName() + " to pdf");
            String normalizedName = Tools.unAccent(binaryResource.getName());

            //copy the converted file for further reuse
            // TODO check for resources to be closed
            try (OutputStream outputStream = storageManager.getGeneratedFileOutputStream(binaryResource, pdfFileName);
                 InputStream binaryResourceInputStream = storageManager.getBinaryResourceInputStream(binaryResource);
                 InputStream inputStreamConverted = fileConverter.convertToPDF(normalizedName, binaryResourceInputStream)) {
                FileIO.copy(inputStreamConverted, outputStream);
            }
            inputStream = storageManager.getGeneratedFileInputStream(binaryResource, pdfFileName);
        }


        return inputStream;
    }

}
