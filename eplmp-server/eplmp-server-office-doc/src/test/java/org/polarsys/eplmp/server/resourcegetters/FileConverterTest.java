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


package org.polarsys.eplmp.server.resourcegetters;

import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.exceptions.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@RunWith(MockitoJUnitRunner.class)
public class FileConverterTest {

    @InjectMocks
    private FileConverter fileConverter;

    @Mock
    private OfficeConfig officeConfig;

    @Mock
    private OfficeManager officeManager;

    @Before
    public void setup() throws OfficeException {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convertToPDFTest() throws StorageException, IOException, OfficeException, FileNotFoundException, URISyntaxException {

        try(InputStream resourceAsStream = getClass().getResourceAsStream("/org/polarsys/eplmp/server/resourcegetters/sample.txt")){

            Assert.assertNotNull(resourceAsStream);
            try{

                fileConverter.convertToPDF("sample.txt", resourceAsStream);
                Assert.fail("Should have thrown an IOException");

            }catch (IOException e ){

                Mockito.verify(officeManager, Mockito.times(1)).execute(Matchers.any());
            }

        }catch (IOException e ){

            Assert.fail("Resource not found: " + e.getMessage());
        }
    }
}