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

package org.polarsys.eplmp.server.rest.file;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.product.PartMasterTemplateKey;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.file.util.BinaryResourceBinaryStreamingOutput;
import org.polarsys.eplmp.server.util.PartImpl;
import org.polarsys.eplmp.server.util.ResourceUtil;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;

public class PartTemplateBinaryResourceTest {

    @InjectMocks
    PartTemplateBinaryResource partTemplateBinaryResource = new PartTemplateBinaryResource();
    @Mock
    private IBinaryStorageManagerLocal storageManager;
    @Mock
    private IProductManagerLocal productService;
    @Spy
    BinaryResource binaryResource;

    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    /**
     * test the upload of a simple file in parts templates
     *
     * @throws Exception
     */
    @Test
    public void uploadPartTemplateFiles() throws Exception {
        //Given
        final File fileToUpload = new File(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME1).getFile());
        File uploadedFile = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + ResourceUtil.FILENAME_TARGET_PART, ResourceUtil.TEMP_SUFFIX);
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> parts = new ArrayList<>();
        parts.add(new PartImpl(fileToUpload));
        Mockito.when(request.getParts()).thenReturn(parts);
        binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());

        OutputStream outputStream = new FileOutputStream(uploadedFile);
        Mockito.when(productService.saveFileInTemplate(ArgumentMatchers.any(PartMasterTemplateKey.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceOutputStream(binaryResource)).thenReturn(outputStream);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/parts-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.FILENAME_TARGET_PART);
        //When
        Response response = partTemplateBinaryResource.uploadPartTemplateFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), 201);
        Assert.assertEquals(response.getStatusInfo(), Response.Status.CREATED);
        //delete temp file
        uploadedFile.deleteOnExit();


    }

    /**
     * test the upload of a file (under name that contains special characters) in parts templates
     *
     * @throws Exception
     */
    @Test
    public void uploadPartTemplateFilesUnderANameContainingSpecialCharacters() throws Exception {
        //Given

        final File fileToUpload = new File(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME1).getFile());
        File uploadedFile = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER, ResourceUtil.TEMP_SUFFIX);
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> parts = new ArrayList<>();
        parts.add(new PartImpl(fileToUpload));
        Mockito.when(request.getParts()).thenReturn(parts);
        binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());

        OutputStream outputStream = new FileOutputStream(uploadedFile);
        Mockito.when(productService.saveFileInTemplate(ArgumentMatchers.any(PartMasterTemplateKey.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceOutputStream(binaryResource)).thenReturn(outputStream);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/parts-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.FILENAME_TARGET_PART);
        //When
        Response response = partTemplateBinaryResource.uploadPartTemplateFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), 201);
        Assert.assertEquals(response.getStatusInfo(), Response.Status.CREATED);

        //delete temp file
        uploadedFile.deleteOnExit();
    }

    /**
     * test the upload of a file (that contains special characters) in parts templates
     *
     * @throws Exception
     */
    @Test
    public void uploadPartTemplateFilesNameContainingSpecialCharacters() throws Exception {
        //Given

        final File fileToUpload = new File(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER).toURI());
        File uploadedFile = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + "new_" + ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER, ResourceUtil.TEMP_SUFFIX);
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> parts = new ArrayList<>();
        parts.add(new PartImpl(fileToUpload));
        Mockito.when(request.getParts()).thenReturn(parts);
        binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());

        OutputStream outputStream = new FileOutputStream(uploadedFile);
        Mockito.when(productService.saveFileInTemplate(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceOutputStream(binaryResource)).thenReturn(outputStream);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/parts-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.FILENAME_TARGET_PART);


        //When
        Response response = partTemplateBinaryResource.uploadPartTemplateFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(201, response.getStatus());
        Assert.assertEquals(Response.Status.CREATED, response.getStatusInfo());

        //delete temp file
        uploadedFile.deleteOnExit();
    }


    /**
     * test the upload of several files to parts templates
     *
     * @throws Exception
     */
    @Test
    public void uploadPartTemplateSeveralFiles() throws Exception {
        //Given
        final File fileToUpload1 = File.createTempFile(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER).getFile(), ResourceUtil.TEMP_SUFFIX);
        final File fileToUpload2 = new File(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME1).getFile());
        final File fileToUpload3 = new File(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME2).getFile());
        File uploadedFile1 = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + "new_" + ResourceUtil.TEST_PART_FILENAME1, ResourceUtil.TEMP_SUFFIX);
        File uploadedFile2 = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + "new_" + ResourceUtil.TEST_PART_FILENAME2, ResourceUtil.TEMP_SUFFIX);
        File uploadedFile3 = File.createTempFile(ResourceUtil.TARGET_PART_STORAGE + "new_" + ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER, ResourceUtil.TEMP_SUFFIX);
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> parts = new ArrayList<>();
        parts.add(new PartImpl(fileToUpload1));
        parts.add(new PartImpl(fileToUpload2));
        parts.add(new PartImpl(fileToUpload3));
        Mockito.when(request.getParts()).thenReturn(parts);
        BinaryResource binaryResource1 = new BinaryResource(ResourceUtil.TEST_PART_FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());
        BinaryResource binaryResource2 = new BinaryResource(ResourceUtil.TEST_PART_FILENAME2, ResourceUtil.DOCUMENT_SIZE, new Date());
        BinaryResource binaryResource3 = new BinaryResource(ResourceUtil.FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER, ResourceUtil.DOCUMENT_SIZE, new Date());

        OutputStream outputStream1 = new FileOutputStream(uploadedFile1);
        OutputStream outputStream2 = new FileOutputStream(uploadedFile2);
        OutputStream outputStream3 = new FileOutputStream(uploadedFile3);
        Mockito.when(productService.saveFileInTemplate(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(binaryResource1, binaryResource1, binaryResource2, binaryResource2, binaryResource3, binaryResource3);

        Mockito.when(storageManager.getBinaryResourceOutputStream(ArgumentMatchers.any(BinaryResource.class))).thenReturn(outputStream1, outputStream2, outputStream3);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/parts-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.FILENAME_TARGET_PART);

        //When
        Response response = partTemplateBinaryResource.uploadPartTemplateFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(204, response.getStatus());
        Assert.assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());

        //delete temp files
        uploadedFile1.deleteOnExit();
        uploadedFile2.deleteOnExit();
        uploadedFile3.deleteOnExit();

    }

    /**
     * test the download of file from part templates with non null range
     *
     * @throws Exception
     */
    @Test
    public void downloadPartTemplateFileNonNullRange() throws Exception {

        //Given
        Request request = Mockito.mock(Request.class);
        binaryResource = new BinaryResource(ResourceUtil.TEST_PART_FILENAME1, ResourceUtil.PART_SIZE, new Date());
        File file = File.createTempFile(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME1).getFile(), ResourceUtil.TEMP_SUFFIX);
        FileInputStream fileInputStream = new FileInputStream(file);
        Mockito.when(productService.getTemplateBinaryResource(ResourceUtil.WORKSPACE_ID + "/part-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.TEST_PART_FILENAME1)).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(fileInputStream);
        //When
        Response response = partTemplateBinaryResource.downloadPartTemplateFile(request, ResourceUtil.RANGE, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID, ResourceUtil.TEST_PART_FILENAME1);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusInfo(), Response.Status.PARTIAL_CONTENT);
        Assert.assertEquals(response.getStatus(), 206);
        Assert.assertTrue(response.hasEntity());
        Assert.assertTrue(response.getEntity() instanceof BinaryResourceBinaryStreamingOutput);

        //delete temp file
        file.deleteOnExit();

    }

    /**
     * test the download of file from part templates with null range
     *
     * @throws Exception
     */
    @Test
    public void downloadPartTemplateFileNullRange() throws Exception {

        //Given
        Request request = Mockito.mock(Request.class);
        binaryResource = new BinaryResource(ResourceUtil.TEST_PART_FILENAME1, ResourceUtil.PART_SIZE, new Date());
        File file = File.createTempFile(getClass().getClassLoader().getResource(ResourceUtil.SOURCE_PART_STORAGE + ResourceUtil.TEST_PART_FILENAME1).getFile(), ResourceUtil.TEMP_SUFFIX);
        FileInputStream fileInputStream = new FileInputStream(file);
        Mockito.when(productService.getTemplateBinaryResource(ResourceUtil.WORKSPACE_ID + "/part-templates/" + ResourceUtil.PART_TEMPLATE_ID + "/" + ResourceUtil.TEST_PART_FILENAME1)).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(fileInputStream);
        //When
        Response response = partTemplateBinaryResource.downloadPartTemplateFile(request, null, ResourceUtil.WORKSPACE_ID, ResourceUtil.PART_TEMPLATE_ID, ResourceUtil.TEST_PART_FILENAME1);
        //Then
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusInfo(), Response.Status.OK);
        Assert.assertTrue(response.hasEntity());
        Assert.assertTrue(response.getEntity() instanceof BinaryResourceBinaryStreamingOutput);
        Assert.assertNotNull(response.getHeaders().getFirst("Content-Disposition"));
        Assert.assertNotNull("attachment;filename=\"" + ResourceUtil.TEST_PART_FILENAME1 + "\"".equals(response.getHeaders().getFirst("Content-Disposition")));

        //delete temp file
        file.deleteOnExit();

    }


}
