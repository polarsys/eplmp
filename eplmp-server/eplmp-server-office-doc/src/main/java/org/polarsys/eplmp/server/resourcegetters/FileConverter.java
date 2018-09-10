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

import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.polarsys.eplmp.server.converters.OnDemandConverter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class FileConverter {

    private final OfficeConfig officeConfig;
    private static final Logger LOGGER = Logger.getLogger(OnDemandConverter.class.getName());

    @Inject
    public FileConverter(OfficeConfig officeConfig) {
        this.officeConfig = officeConfig;
    }

    private OfficeManager officeManager;

    @PostConstruct
    private void init() {
        officeManager = LocalOfficeManager.builder()
                .officeHome(new File(officeConfig.getOfficeHome()))
                .portNumbers(officeConfig.getOfficePort())
                .build();
        try {
            officeManager.start();
        } catch (OfficeException e) {

            LOGGER.log(Level.INFO, "Office manager not started : "+e);
        }
    }

    @PreDestroy
    private void close() {

        try {
            officeManager.stop();
        } catch (OfficeException e) {

            LOGGER.log(Level.INFO, "Office manager not stopped : "+e);
        }
    }

    public synchronized InputStream convertToPDF(String sourceName, final InputStream streamToConvert) throws IOException, OfficeException {
        File tmpDir = Files.createTempDirectory("docdoku-").toFile();
        File fileToConvert = new File(tmpDir, sourceName);

        Files.copy(streamToConvert, fileToConvert.toPath());

        File pdfFile = convertToPDF(fileToConvert);

        //clean-up
        tmpDir.deleteOnExit();

        return new FileInputStream(pdfFile);
    }

    private File convertToPDF(File fileToConvert) throws OfficeException {
        File pdfFile = new File(fileToConvert.getParentFile(), "converted.pdf");
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(fileToConvert, pdfFile);
        return pdfFile;
    }

}
