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

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Singleton
public class FileConverter {

    private final OfficeConfig officeConfig;

    @Inject
    public FileConverter(OfficeConfig officeConfig) {
        this.officeConfig = officeConfig;
    }

    private OfficeManager officeManager;

    @PostConstruct
    private void init() {
        officeManager = new DefaultOfficeManagerConfiguration()
                .setOfficeHome(new File(officeConfig.getOfficeHome()))
                .setPortNumber(officeConfig.getOfficePort())
                .buildOfficeManager();
        officeManager.start();
    }

    @PreDestroy
    private void close() {
        officeManager.stop();
    }

    public synchronized InputStream convertToPDF(String sourceName, final InputStream streamToConvert) throws IOException {
        File tmpDir = Files.createTempDirectory("docdoku-").toFile();
        File fileToConvert = new File(tmpDir, sourceName);

        Files.copy(streamToConvert, fileToConvert.toPath());

        File pdfFile = convertToPDF(fileToConvert);

        //clean-up
        tmpDir.deleteOnExit();

        return new FileInputStream(pdfFile);
    }

    private File convertToPDF(File fileToConvert) {
        File pdfFile = new File(fileToConvert.getParentFile(), "converted.pdf");
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(fileToConvert, pdfFile);
        return pdfFile;
    }

}
