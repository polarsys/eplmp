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

package org.polarsys.server.importers.attributes;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.polarsys.eplmp.core.util.FileIO;
import org.polarsys.eplmp.i18n.PropertiesLoader;
import org.polarsys.eplmp.server.importers.AttributesImporterUtils;
import org.polarsys.eplmp.server.importers.PartImporter;
import org.polarsys.eplmp.server.importers.PartImporterResult;
import org.polarsys.eplmp.server.importers.PartToImport;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that imports attribute modification on attribute part from an Excel File.
 *
 * @author Laurent Le Van
 * @version 1.0.0
 * @since 12/02/16.
 */

@PartAttributesImporter
@Stateless
public class PartAttributesImporterImpl implements PartImporter {

    private static final Logger LOGGER = Logger.getLogger(PartAttributesImporterImpl.class.getName());
    private static final String[] EXTENSIONS = {"xls"};
    private static final String I18N_CONF = "/org/polarsys/server/importers/attributes/ExcelImport";
    private Properties properties;

    @Override
    public boolean canImportFile(String importFileName) {
        String ext = FileIO.getExtension(importFileName);
        return Arrays.asList(EXTENSIONS).contains(ext);
    }

    @Override
    public PartImporterResult importFile(Locale locale, String workspaceId, File file, boolean autoCheckout, boolean autoCheckIn, boolean permissiveUpdate) {

        properties = PropertiesLoader.loadLocalizedProperties(locale, I18N_CONF, PartAttributesImporterImpl.class);

        Map<String, PartToImport> partsToImport = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try {
            ExcelParser excelParser = new ExcelParser(file, locale);
            List<String> checkFileErrors = excelParser.checkFile();
            errors.addAll(checkFileErrors);
            if (errors.isEmpty()) {
                partsToImport = excelParser.getPartsToImport();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
            errors.add(AttributesImporterUtils.createError(properties, "InternalError", "IOException"));
        } catch (InvalidFormatException e) {
            LOGGER.log(Level.SEVERE, null, e);
            errors.add(AttributesImporterUtils.createError(properties, "InvalidFormatException"));
        } catch (WrongCellCommentException e) {
            errors.add(AttributesImporterUtils.createError(properties, "WrongCellCommentException"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            errors.add(AttributesImporterUtils.createError(properties, "InternalError", e.toString()));
        }

        if (!errors.isEmpty()) {
            return new PartImporterResult(file, warnings, errors, null, null, null);
        }

        return new PartImporterResult(file, warnings, errors, null, null, partsToImport);
    }

}

