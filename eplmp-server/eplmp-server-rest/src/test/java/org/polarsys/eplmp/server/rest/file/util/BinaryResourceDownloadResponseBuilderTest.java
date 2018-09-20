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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.polarsys.eplmp.server.rest.exceptions.RequestedRangeNotSatisfiableException;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class BinaryResourceDownloadResponseBuilderTest {


    @InjectMocks
    private BinaryResourceDownloadResponseBuilder binaryResourceDownloadResponseBuilder;
    @Mock
    private InputStream inputStream;
    @Mock
    private BinaryResourceDownloadMeta binaryResourceDownloadMeta;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void prepareResponseTest() throws RequestedRangeNotSatisfiableException {

        Mockito.when(binaryResourceDownloadMeta.isConverted()).thenReturn(false);

        Response response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream,binaryResourceDownloadMeta, null,true,null);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertFalse(response.getHeaders().containsKey("Content-Security-Policy"));

        response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream,binaryResourceDownloadMeta, "bytes=0-",true,"");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertFalse(response.getHeaders().containsKey("Content-Security-Policy"));

        response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream,binaryResourceDownloadMeta, "bytes=0-2",true,"http://localhost:9001");
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertTrue(response.getHeaders().containsKey("Content-Security-Policy"));

        try {

            response = binaryResourceDownloadResponseBuilder.prepareResponse(inputStream, binaryResourceDownloadMeta, "0-2", true, "http://localhost:9001");
            Assert.fail();
        }catch (RequestedRangeNotSatisfiableException e){

            Assert.assertNotNull(response);
            Assert.assertNotNull(response.getHeaders());
            Assert.assertTrue(response.getHeaders().containsKey("Content-Security-Policy"));
        }
    }
}