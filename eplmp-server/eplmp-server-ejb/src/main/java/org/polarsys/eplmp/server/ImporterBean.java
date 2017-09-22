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

package org.polarsys.eplmp.server;


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ImportPreview;
import org.polarsys.eplmp.core.product.ImportResult;
import org.polarsys.eplmp.core.services.IImporterManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.i18n.PropertiesLoader;
import org.polarsys.eplmp.server.importers.PartImporter;
import org.polarsys.eplmp.server.importers.PathDataImporter;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Attributes importer
 *
 * @author Elisabel Généreux
 */
@Stateless(name = "ImporterBean")
public class ImporterBean implements IImporterManagerLocal {


    private static final Logger LOGGER = Logger.getLogger(ImporterBean.class.getName());

    @Inject
    @Any
    private Instance<PartImporter> partImporters;

    @Inject
    @Any
    private Instance<PathDataImporter> pathDataImporters;

    @Inject
    private IUserManagerLocal userManager;

    @Override
    @Asynchronous
    @FileImport
    public Future<ImportResult> importIntoParts(String workspaceId, File file, String originalFileName, String revisionNote, boolean autoCheckout, boolean autoCheckin, boolean permissiveUpdate) {

        PartImporter selectedImporter = selectPartImporter(file);

        ImportResult result;

        if (selectedImporter != null) {
            result = selectedImporter.importFile(workspaceId, file, revisionNote, autoCheckout, autoCheckin, permissiveUpdate);
        } else {
            result = getNoImporterAvailableError(file, originalFileName, getUserLocale(workspaceId));
        }

        return new AsyncResult<>(result);
    }

    @Override
    @Asynchronous
    @FileImport
    public Future<ImportResult> importIntoPathData(String workspaceId, File file, String originalFileName, String revisionNote, boolean autoFreezeAfterUpdate, boolean permissiveUpdate) {
        PathDataImporter selectedImporter = selectPathDataImporter(file);

        ImportResult result;

        if (selectedImporter != null) {
            result = selectedImporter.importFile(workspaceId, file, revisionNote, autoFreezeAfterUpdate, permissiveUpdate);
        } else {
            result = getNoImporterAvailableError(file, originalFileName, getUserLocale(workspaceId));
        }

        return new AsyncResult<>(result);
    }


    @Override
    public ImportPreview dryRunImportIntoParts(String workspaceId, File file, String originalFileName, boolean autoCheckout, boolean autoCheckin, boolean permissiveUpdate) throws ImportPreviewException {

        PartImporter selectedImporter = selectPartImporter(file);

        if (selectedImporter != null) {
            return selectedImporter.dryRunImport(workspaceId, file, originalFileName, autoCheckout, autoCheckin, permissiveUpdate);
        }

        return null;

    }

    private Locale getUserLocale(String workspaceId) {
        Locale locale;
        try {
            User user = userManager.whoAmI(workspaceId);
            locale = new Locale(user.getLanguage());
        } catch (ApplicationException e) {
            LOGGER.log(Level.SEVERE, "Cannot fetch account info", e);
            locale = Locale.getDefault();
        }
        return locale;
    }

    private PartImporter selectPartImporter(File file) {
        PartImporter selectedImporter = null;
        for (PartImporter importer : partImporters) {
            if (importer.canImportFile(file.getName())) {
                selectedImporter = importer;
                break;
            }
        }
        return selectedImporter;
    }

    private PathDataImporter selectPathDataImporter(File file) {
        PathDataImporter selectedImporter = null;
        for (PathDataImporter importer : pathDataImporters) {
            if (importer.canImportFile(file.getName())) {
                selectedImporter = importer;
                break;
            }
        }
        return selectedImporter;
    }

    private ImportResult getNoImporterAvailableError(File file, String fileName, Locale locale) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Properties properties = PropertiesLoader.loadLocalizedProperties(locale, "/org/polarsys/eplmp/core/i18n/LocalStrings", getClass());
        errors.add(properties.getProperty("NoImporterAvailable"));
        return new ImportResult(file, warnings, errors);
    }
}
