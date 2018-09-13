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

package org.polarsys.eplmp.server.rest.file.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.polarsys.eplmp.server.rest.exceptions.RequestedRangeNotSatisfiableException;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;

public class BinaryResourceDownloadResponseBuilderTest {


    @InjectMocks
    private BinaryResourceDownloadResponseBuilder binaryResourceDownloadResponseBuilder;

    @Mock
    private InputStream inputStream;

    @Mock
    private BinaryResourceDownloadMeta binaryResourceDownloadMeta;

    @Mock
    Properties properties;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void prepareResponseTest() throws RequestedRangeNotSatisfiableException {

        Mockito.when(properties.getProperty("xFrameOptions")).thenReturn("ALLOWALL");

        Response response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream,binaryResourceDownloadMeta, "",true,properties.getProperty("xFrameOptions"));
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertNotNull(response.getHeaders().get("xFrameOptions"));
        Assert.assertTrue(response.getHeaders().get("xFrameOptions").contains("ALLOWALL"));

        Mockito.when(binaryResourceDownloadMeta.isConverted()).thenReturn(false);
        response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream,binaryResourceDownloadMeta, "",true,properties.getProperty("xFrameOptions"));
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertNotNull(response.getHeaders().get("xFrameOptions"));
        Assert.assertTrue(response.getHeaders().get("xFrameOptions").contains("ALLOWALL"));

        try {

            response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream, binaryResourceDownloadMeta, "0-0", true,properties.getProperty("xFrameOptions"));
        }catch (RequestedRangeNotSatisfiableException e){

            Assert.assertNotNull(response);
            Assert.assertNotNull(response.getHeaders());
            Assert.assertNotNull(response.getHeaders().get("xFrameOptions"));
            Assert.assertTrue(response.getHeaders().get("xFrameOptions").contains("ALLOWALL"));
        }
    }
}