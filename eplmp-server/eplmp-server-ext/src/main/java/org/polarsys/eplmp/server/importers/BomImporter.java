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

package org.polarsys.eplmp.server.importers;

import javax.ejb.Remote;
import java.io.File;
import java.util.Locale;

/**
 * BomImporter plugin interface
 */
@Remote
public interface BomImporter {

    /**
     * Exception reporting a unrecoverable problem during conversion process.
     */
    class ImporterException extends Exception {

        private static final long serialVersionUID = 1L;

        public ImporterException(String message) {
            super(message);
        }

        public ImporterException(Throwable cause) {
            super(cause);
        }

        public ImporterException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Determine if plugin is able to import the given file format
     *
     * @param importFileName the file name
     * @return true if plugin can handle the import, false otherwise
     */
    boolean canImportFile(String importFileName);

    /**
     * Import the file and make requested changes
     *
     * @param workspaceId      the workspace concerned by the import
     * @param file             the file to import
     * @param autoCheckout     check out the part if not checked out
     * @param autoCheckIn      check in the modified parts after operation
     * @param permissiveUpdate don't throw if any warnings or errors
     * @return an ImportResult result object
     */
    // TODO : replace java.io.File with java.io.InputStream
    BomImporterResult importFile(Locale locale, String workspaceId, File file, boolean autoCheckout, boolean autoCheckIn, boolean permissiveUpdate) throws ImporterException;

}
