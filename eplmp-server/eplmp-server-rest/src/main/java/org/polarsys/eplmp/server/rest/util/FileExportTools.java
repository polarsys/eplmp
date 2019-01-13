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

package org.polarsys.eplmp.server.rest.util;

import org.polarsys.eplmp.core.exceptions.StorageException;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Elisabel Généreux
 */
public class FileExportTools {

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    public static void addToZipFile(InputStream binaryResourceInputStream, String binaryResourceName, String folderName, ZipOutputStream zos)
            throws IOException, StorageException {

        ZipEntry zipEntry = new ZipEntry(folderName + "/" + binaryResourceName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = binaryResourceInputStream.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
    }
}
