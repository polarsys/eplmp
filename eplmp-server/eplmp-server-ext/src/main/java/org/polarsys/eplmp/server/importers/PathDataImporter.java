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

import org.polarsys.eplmp.core.product.ImportResult;

import java.io.File;

/**
 * PathDataImporter plugin interface
 */
public interface PathDataImporter {
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
     * @param autoFreezeAfterUpdate todo
     * @param permissiveUpdate todo
     * @return an import result object
     */
    ImportResult importFile(String workspaceId, File file, String revisionNote, boolean autoFreezeAfterUpdate, boolean permissiveUpdate);
}
