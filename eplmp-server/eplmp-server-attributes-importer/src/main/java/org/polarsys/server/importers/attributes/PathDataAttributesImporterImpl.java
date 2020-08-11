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
import org.polarsys.eplmp.server.importers.PathDataImporter;
import org.polarsys.eplmp.server.importers.PathDataImporterResult;
import org.polarsys.eplmp.server.importers.PathDataToImport;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that imports attribute modification on attribute Path Data from an Excel File.
 *
 * @author Laurent Le Van
 * @version 1.0.0
 * @since 12/02/16.
 */

@PathDataAttributesImporter
@Stateless
public class PathDataAttributesImporterImpl implements PathDataImporter {

    private static final String[] EXTENSIONS = {"xls"};
    private static final Logger LOGGER = Logger.getLogger(PathDataAttributesImporterImpl.class.getName());

    private static final String I18N_CONF = "/com/docdoku/server/importers/attributes/ExcelImport";

    private Properties properties;

    /**
     * Checks if valid extension
     *
     * @param importFileName name of the file we want to import
     * @return true if good extension, false else
     */
    @Override
    public boolean canImportFile(String importFileName) {
        String ext = FileIO.getExtension(importFileName);
        return Arrays.asList(EXTENSIONS).contains(ext);
    }

    /**
     * This method import data of a file with different options
     *
     * @param workspaceId      Workspace in which we work
     * @param file             file containing data we want to update
     * @param autoFreeze       autofreeze after modification
     * @param permissiveUpdate boolean to indicate if allow or not permissive update
     * @return an ImportResult object containing file, and warnings, errors
     */
    @Override
    public PathDataImporterResult importFile(Locale locale, String workspaceId, File file, boolean autoFreeze, boolean permissiveUpdate) {

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        properties = PropertiesLoader.loadLocalizedProperties(locale, I18N_CONF, PartAttributesImporterImpl.class);

        Map<String, PathDataToImport> result = new HashMap<>();

        try {
            ExcelParser excelParser = new ExcelParser(file, locale);
            List<String> checkFileErrors = excelParser.checkFile();
            errors.addAll(checkFileErrors);
            result = excelParser.importPathData();
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
            return new PathDataImporterResult(file, warnings, errors, null, null, null);
        }

        return new PathDataImporterResult(file, warnings, errors, null, null, result);
    }

}

