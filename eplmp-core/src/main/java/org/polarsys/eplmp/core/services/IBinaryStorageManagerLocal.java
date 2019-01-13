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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.exceptions.StorageException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public interface IBinaryStorageManagerLocal {
    InputStream getBinaryResourceInputStream(BinaryResource binaryResource) throws StorageException;
    OutputStream getBinaryResourceOutputStream(BinaryResource binaryResource) throws StorageException;
    boolean exists(BinaryResource binaryResource, String generatedFileName) throws StorageException;
    Date getLastModified(BinaryResource binaryResource, String generatedFileName) throws StorageException;
    InputStream getGeneratedFileInputStream(BinaryResource binaryResource, String generatedFileName) throws StorageException;
    OutputStream getGeneratedFileOutputStream(BinaryResource binaryResource, String generatedFileName) throws StorageException;
    void copyData(BinaryResource source, BinaryResource destination) throws StorageException;
    void deleteData(BinaryResource binaryResource) throws StorageException;
    void renameFile(BinaryResource binaryResource, String pNewName) throws StorageException, FileNotFoundException;
    String getExternalStorageURI(BinaryResource binaryResource);
    String getShortenExternalStorageURI(BinaryResource binaryResource);
    void deleteWorkspaceFolder(String workspaceId) throws StorageException;
}
