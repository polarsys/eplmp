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

package org.polarsys.eplmp.server.storage;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.exceptions.StorageException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public interface StorageProvider {
    InputStream getBinaryResourceInputStream(BinaryResource pBinaryResource) throws StorageException, FileNotFoundException;
    File getBinaryResourceFile(BinaryResource pBinaryResource) throws StorageException, FileNotFoundException;
    OutputStream getBinaryResourceOutputStream(BinaryResource pBinaryResource) throws StorageException;
    void copyData(BinaryResource pSourceBinaryResource, BinaryResource pTargetBinaryResource) throws StorageException, FileNotFoundException;
    File copyFile(File file, BinaryResource pTargetBinaryResource) throws StorageException, FileNotFoundException;
    void delData(BinaryResource pBinaryResource) throws StorageException;
    String getExternalResourceURI(BinaryResource binaryResource);
    String getShortenExternalResourceURI(BinaryResource binaryResource);
    void deleteWorkspaceFolder(String workspaceId) throws StorageException;
    void renameData(File file, String pNewName) throws StorageException;
    boolean exists(BinaryResource binaryResource, String generatedFileName);
    Date getLastModified(BinaryResource binaryResource, String generatedFileName) throws FileNotFoundException;
    InputStream getGeneratedFileInputStream(BinaryResource pBinaryResource, String generatedFileName) throws StorageException, FileNotFoundException;
    OutputStream getGeneratedFileOutputStream(BinaryResource binaryResource, String generatedFileName) throws StorageException;
}
