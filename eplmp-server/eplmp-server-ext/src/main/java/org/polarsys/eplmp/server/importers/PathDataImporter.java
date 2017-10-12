/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.importers;

import javax.ejb.Remote;
import java.io.File;
import java.util.Locale;

/**
 * PathDataImporter plugin interface
 */
@Remote
public interface PathDataImporter {

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
     * @param workspaceId           the workspace concerned by the import
     * @param file                  the file to import
     * @param autoFreezeAfterUpdate todo
     * @param permissiveUpdate      todo
     * @return an import result object
     */
    PathDataImporterResult importFile(Locale locale, String workspaceId, File file, boolean autoFreezeAfterUpdate, boolean permissiveUpdate);
}
