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

import org.polarsys.eplmp.core.product.ImportPreview;
import org.polarsys.eplmp.core.product.ImportResult;

import java.io.File;

/**
 * PartImporter plugin interface
 */
public interface PartImporter {
    /**
     * Determine if plugin is able to import the given file format
     *
     * @param importFileName   the file name
     * @return true if plugin can handle the import, false otherwise
     */
    boolean canImportFile(String importFileName);
    /**
     * Import the file and make requested changes
     *
     * @param workspaceId the workspace concerned by the import
     * @param file the file to import
     * @param revisionNote a revision note to apply on parts changed
     * @param autoCheckout check out the part if not checked out
     * @param autoCheckin check in the modified parts after operation
     * @param permissiveUpdate todo
     * @return an ImportResult result object
     */
    // TODO : replace java.io.File with java.io.InputStream
    ImportResult importFile(String workspaceId, File file, String revisionNote, boolean autoCheckout, boolean autoCheckin, boolean permissiveUpdate);

    /**
     * Run a dry import
     *
     * @param workspaceId the workspace concerned by the import
     * @param file the file to import
     * @param originalFileName the original file name
     * @param autoCheckout check out the part if not checked out
     * @param autoCheckin check in the modified parts after operation
     * @param permissiveUpdate todo
     * @return an ImportPreview result object
     */
    // TODO : replace java.io.File with java.io.InputStream
    ImportPreview dryRunImport(String workspaceId, File file, String originalFileName, boolean autoCheckout, boolean autoCheckin, boolean permissiveUpdate);

}
