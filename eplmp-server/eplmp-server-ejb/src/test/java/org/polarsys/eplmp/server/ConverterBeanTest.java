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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.product.Geometry;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.converters.CADConverter;
import org.polarsys.eplmp.server.converters.ConversionResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConverterBeanTest {

    @Mock
    private BeanLocator locator;

    @Mock
    private IProductManagerLocal product;

    @Mock
    private IBinaryStorageManagerLocal storage;

    @InjectMocks
    private ConverterBean bean;

    @Mock
    private PartIterationKey ipk;

    @Mock
    private PartIteration partIter;

    @Mock
    private BinaryResource cadBinRes;

    @Mock
    private CADConverter conv;

    @Mock
    private ConversionResult result;

    @Mock
    private Geometry lod;

    @Mock
    private BinaryResource attachedFile;

    @Before
    public void setup() throws Exception {
        when(cadBinRes.getName()).thenReturn("foo.dae");
        when(storage.getBinaryResourceInputStream(cadBinRes))
                .thenReturn(new ByteArrayInputStream("fake content".getBytes()));

        when(product.saveGeometryInPartIteration(eq(ipk), anyString(), anyInt(), anyLong(), any(double[].class)))
                .thenReturn(lod);
        when(storage.getBinaryResourceOutputStream(lod)).thenReturn(new ByteArrayOutputStream());

        when(product.saveFileInPartIteration(eq(ipk), anyString(), eq("attachedfiles"), anyLong()))
                .thenReturn(attachedFile, attachedFile);
        when(product.getPartIteration(eq(ipk)))
                .thenReturn(partIter);
        when(storage.getBinaryResourceOutputStream(attachedFile)).thenReturn(new ByteArrayOutputStream());

        when(conv.canConvertToOBJ("dae")).thenReturn(true);

        when(result.getConvertedFile()).thenReturn(Paths.get("src/test/resources/fake.obj"));
        when(result.getMaterials()).thenReturn(Arrays.asList(Paths.get("src/test/resources/fake.obj.1.mtl"),
                Paths.get("src/test/resources/fake.obj.2.mtl")));

        when(conv.convert(any(URI.class), any(URI.class))).thenReturn(result);

        when(locator.search(CADConverter.class)).thenReturn(Arrays.asList(conv));

        bean.init();
    }

    @Test
    public void testNominalConvert() throws Exception {
        // * test *
        bean.convertCADFileToOBJ(ipk, cadBinRes);
        verify(locator).search(CADConverter.class);
        verify(conv).canConvertToOBJ("dae");
        verify(conv).convert(any(URI.class), any(URI.class));
        verify(product).saveGeometryInPartIteration(eq(ipk), anyString(), anyInt(), anyLong(), any(double[].class));
        verify(storage).getBinaryResourceOutputStream(lod);
        verify(product, times(2)).saveFileInPartIteration(eq(ipk), anyString(), eq("attachedfiles"), anyLong());
        verify(storage, times(2)).getBinaryResourceOutputStream(attachedFile);
    }

    @Test
    public void testNoConverter() throws Exception {
        // * setup *
        when(cadBinRes.getName()).thenReturn("foo.unknown");

        // * test *
        bean.convertCADFileToOBJ(ipk, cadBinRes);

        verify(locator).search(CADConverter.class);
        verify(conv).canConvertToOBJ("unknown");
        verify(conv, never()).convert(any(), any());
        verify(product, never()).saveGeometryInPartIteration(any(), anyString(), anyInt(), anyLong(), any());
        verify(product, never()).saveFileInPartIteration(any(), anyString(), anyString(), anyLong());
        verify(storage, never()).getBinaryResourceOutputStream(any());
    }

    @Test
    public void testBrokenConvert() throws Exception {
        // * setup *
        when(conv.convert(any(URI.class), any(URI.class))).thenThrow(new CADConverter.ConversionException("error"));

        // * test *
        bean.convertCADFileToOBJ(ipk, cadBinRes);

        verify(conv).canConvertToOBJ("dae");
        verify(conv).convert(any(URI.class), any(URI.class));
        verify(product, never()).saveGeometryInPartIteration(any(), anyString(), anyInt(), anyLong(), any());
        verify(product, never()).saveFileInPartIteration(any(), anyString(), anyString(), anyLong());
        verify(storage, never()).getBinaryResourceOutputStream(any());
    }

}
