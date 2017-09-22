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

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.server.storage.StorageProvider;
import org.polarsys.eplmp.server.storage.filesystem.FileStorageProvider;
import org.polarsys.eplmp.server.util.DocumentUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.util.Date;


public class DataManagerBeanTest {

    public static final String TARGET_FILE_STORAGE="";

    private StorageProvider defaultStorageProvider;
    private BinaryResource binaryResource;

    @Before
    public void setUp() throws Exception {
        //Given
        defaultStorageProvider = new FileStorageProvider(System.getProperty("java.io.tmpdir")+TARGET_FILE_STORAGE);
        binaryResource = new BinaryResource(DocumentUtil.FULL_NAME4,DocumentUtil.DOCUMENT_SIZE,new Date());
    }

    @Test
    public void testGetBinaryResourceOutputStream() throws Exception {
        //When
        BufferedOutputStream outputStream = (BufferedOutputStream)defaultStorageProvider.getBinaryResourceOutputStream(binaryResource);
        //Then
        Assert.assertTrue(outputStream != null);
    }

}
