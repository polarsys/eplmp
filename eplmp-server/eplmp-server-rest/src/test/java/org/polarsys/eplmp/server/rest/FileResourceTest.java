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

package org.polarsys.eplmp.server.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.polarsys.eplmp.server.rest.file.*;

/**
 * @author
 */
public class FileResourceTest {


    @InjectMocks
    private FileResource resource = new FileResource();

    @Mock
    private DocumentBinaryResource documentBinaryResource;

    @Mock
    private PartBinaryResource partBinaryResource;

    @Mock
    private DocumentTemplateBinaryResource documentTemplateBinaryResource;

    @Mock
    private PartTemplateBinaryResource partTemplateBinaryResource;

    @Mock
    private ProductInstanceBinaryResource productInstanceBinaryResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void documentFileTest() {
        DocumentBinaryResource _documentBinaryResource = resource.documentFile();
        Assert.assertEquals(documentBinaryResource, _documentBinaryResource);
    }

    @Test
    public void partFileTest() {
        PartBinaryResource _partBinaryResource = resource.partFile();
        Assert.assertEquals(partBinaryResource, _partBinaryResource);
    }

    @Test
    public void documentTemplateFileTest() {
        DocumentTemplateBinaryResource _documentTemplateBinaryResource = resource.documentTemplateFile();
        Assert.assertEquals(documentTemplateBinaryResource, _documentTemplateBinaryResource);
    }

    @Test
    public void partTemplateFileTest() {
        PartTemplateBinaryResource _partTemplateBinaryResource = resource.partTemplateFile();
        Assert.assertEquals(partTemplateBinaryResource, _partTemplateBinaryResource);
    }

    @Test
    public void productInstanceFileTest() {
        ProductInstanceBinaryResource _productInstanceBinaryResource = resource.productInstanceFile();
        Assert.assertEquals(productInstanceBinaryResource, _productInstanceBinaryResource);
    }
}
