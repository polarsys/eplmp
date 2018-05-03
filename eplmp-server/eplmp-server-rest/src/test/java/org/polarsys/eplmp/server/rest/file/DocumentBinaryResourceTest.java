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

package org.polarsys.eplmp.server.rest.file;


import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.document.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.core.sharing.SharedDocument;
import org.polarsys.eplmp.core.util.Tools;
import org.polarsys.eplmp.server.auth.AuthConfig;
import org.polarsys.eplmp.server.rest.exceptions.SharedResourceAccessException;
import org.polarsys.eplmp.server.util.PartImpl;
import org.polarsys.eplmp.server.util.ResourceUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.*;

import javax.ejb.SessionContext;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class DocumentBinaryResourceTest {


    @Mock
    private IBinaryStorageManagerLocal storageManager;
    @Mock
    private IContextManagerLocal contextManager;
    @Mock
    private IDocumentManagerLocal documentService;
    @Mock
    private IOnDemandConverterManagerLocal onDemandConverterManager;
    @Mock
    private IShareManagerLocal shareService;
    @Mock
    private SessionContext ctx;
    @Mock
    private IPublicEntityManagerLocal publicEntityManager;
    @Mock
    private AuthConfig authConfig;
    @Spy
    BinaryResource binaryResource;

    @InjectMocks
    DocumentBinaryResource documentBinaryResource = new DocumentBinaryResource();


    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    /**
     * Test the upload of file to a document
     *
     * @throws Exception
     */
    @Test
    public void uploadDocumentFiles() throws Exception {
        //Given
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> filesParts = new ArrayList<>();

        filesParts.add(new PartImpl(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));

        BinaryResource binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());

        File uploadedFile1 = File.createTempFile(ResourceUtil.TARGET_FILE_STORAGE + "new_" + ResourceUtil.FILENAME1, ResourceUtil.TEMP_SUFFIX);

        OutputStream outputStream1 = new FileOutputStream(uploadedFile1);

        Mockito.when(request.getParts()).thenReturn(filesParts);
        Mockito.when(documentService.saveFileInDocument(Matchers.any(DocumentIterationKey.class), Matchers.anyString(), Matchers.anyInt())).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceOutputStream(Matchers.any(BinaryResource.class))).thenReturn(outputStream1);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.FILENAME1);

        //When
        Response response = documentBinaryResource.uploadDocumentFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION);

        //Then
        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertEquals(response.getStatusInfo(), Response.Status.CREATED);

        //delete tem file
        uploadedFile1.deleteOnExit();
    }

    /**
     * Test to upload a file to a document with special characters
     *
     * @throws Exception
     */
    @Test
    public void uploadFileWithSpecialCharactersToDocumentTemplates() throws Exception {

        //Given
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> filesParts = new ArrayList<>();
        filesParts.add(new PartImpl(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE) + ResourceUtil.FILENAME2)));

        BinaryResource binaryResource = new BinaryResource(Tools.unAccent(ResourceUtil.FILENAME2), ResourceUtil.DOCUMENT_SIZE, new Date());

        File uploadedFile1 = File.createTempFile(ResourceUtil.TARGET_FILE_STORAGE + "new_" + ResourceUtil.FILENAME2, ResourceUtil.TEMP_SUFFIX);


        OutputStream outputStream1 = new FileOutputStream(uploadedFile1);

        Mockito.when(request.getParts()).thenReturn(filesParts);
        Mockito.when(documentService.saveFileInDocument(Matchers.any(DocumentIterationKey.class), Matchers.anyString(), Matchers.anyInt())).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceOutputStream(Matchers.any(BinaryResource.class))).thenReturn(outputStream1);
        Mockito.when(request.getRequestURI()).thenReturn(ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.FILENAME2);


        //When
        Response response = documentBinaryResource.uploadDocumentFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION);

        //Then
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertEquals(Response.Status.CREATED, response.getStatusInfo());

        //delete tem file
        uploadedFile1.deleteOnExit();
    }

    /**
     * Test to upload several file to a document
     *
     * @throws Exception
     */
    @Test
    public void uploadSeveralFilesToDocumentsTemplates() throws Exception {

        //Given
        HttpServletRequestWrapper request = Mockito.mock(HttpServletRequestWrapper.class);
        Collection<Part> filesParts = new ArrayList<>();
        filesParts.add(new PartImpl(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));
        filesParts.add(new PartImpl(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE) + ResourceUtil.FILENAME2)));
        filesParts.add(new PartImpl(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME3))));

        BinaryResource binaryResource1 = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());
        BinaryResource binaryResource2 = new BinaryResource(ResourceUtil.FILENAME2, ResourceUtil.DOCUMENT_SIZE, new Date());
        BinaryResource binaryResource3 = new BinaryResource(ResourceUtil.FILENAME3, ResourceUtil.DOCUMENT_SIZE, new Date());

        File uploadedFile1 = File.createTempFile(ResourceUtil.TARGET_FILE_STORAGE + "new_" + ResourceUtil.FILENAME1, ResourceUtil.TEMP_SUFFIX);
        File uploadedFile2 = File.createTempFile(ResourceUtil.TARGET_FILE_STORAGE + "new_" + ResourceUtil.FILENAME2, ResourceUtil.TEMP_SUFFIX);
        File uploadedFile3 = File.createTempFile(ResourceUtil.TARGET_FILE_STORAGE + "new_" + ResourceUtil.FILENAME3, ResourceUtil.TEMP_SUFFIX);

        OutputStream outputStream1 = new FileOutputStream(uploadedFile1);
        OutputStream outputStream2 = new FileOutputStream(uploadedFile2);
        OutputStream outputStream3 = new FileOutputStream(uploadedFile3);
        Mockito.when(request.getParts()).thenReturn(filesParts);
        Mockito.when(documentService.saveFileInDocument(Matchers.any(DocumentIterationKey.class), Matchers.anyString(), Matchers.anyInt())).thenReturn(binaryResource1, binaryResource1, binaryResource2, binaryResource2, binaryResource3, binaryResource3);
        Mockito.when(storageManager.getBinaryResourceOutputStream(Matchers.any(BinaryResource.class))).thenReturn(outputStream1, outputStream2, outputStream3);
        //When
        Response response = documentBinaryResource.uploadDocumentFiles(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION);

        //Then
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());

        //delete temp files
        uploadedFile1.deleteOnExit();
        uploadedFile2.deleteOnExit();
        uploadedFile3.deleteOnExit();

    }

    /**
     * Test to download a document file as a guest and the document is public
     *
     * @throws Exception
     */
    @Test
    public void downloadDocumentFileAsGuestDocumentIsPublic() throws Exception {

        //Given
        Request request = Mockito.mock(Request.class);
        DocumentRevision documentRevision = new DocumentRevision();

        String fullName = ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.VERSION + "/" + ResourceUtil.ITERATION + "/" + ResourceUtil.FILENAME1;

        BinaryResource binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());
        Mockito.when(documentService.canAccess(new DocumentIterationKey(ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION))).thenReturn(false);
        Mockito.when(documentService.getBinaryResource(fullName)).thenReturn(binaryResource);
        Mockito.when(documentService.getDocumentRevision(Matchers.any(DocumentRevisionKey.class))).thenReturn(documentRevision);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(new FileInputStream(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));
        Mockito.when(publicEntityManager.getPublicDocumentRevision(Matchers.any(DocumentRevisionKey.class))).thenReturn(documentRevision);
        Mockito.when(publicEntityManager.getPublicBinaryResourceForDocument(fullName)).thenReturn(binaryResource);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)).thenReturn(false);
        Mockito.when(publicEntityManager.canAccess(Matchers.any(DocumentIterationKey.class))).thenReturn(true);

        //When
        Response response = documentBinaryResource.downloadDocumentFile(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION, ResourceUtil.FILENAME1, ResourceUtil.FILE_TYPE, null, ResourceUtil.RANGE, null, null, null);

        //Then
        assertNotNull(response);
        assertEquals(response.getStatus(), 206);
        assertEquals(response.getStatusInfo(), Response.Status.PARTIAL_CONTENT);
    }

    /**
     * Test to download a document file as a guest but the document is private
     *
     * @throws Exception
     */
    @Test
    public void downloadDocumentFileAsGuestDocumentIsPrivate() throws Exception {
        //Given
        Request request = Mockito.mock(Request.class);
        //Workspace workspace, User author, Date expireDate, String password, DocumentRevision documentRevision
        Account account = Mockito.spy(new Account("user2", "user2", "user2@docdoku.com", "en", new Date(), null));
        Workspace workspace = new Workspace(ResourceUtil.WORKSPACE_ID, account, "pDescription", false);
        User user = new User(workspace, new Account("user1", "user1", "user1@docdoku.com", "en", new Date(), null));
        DocumentMaster documentMaster = new DocumentMaster(workspace, ResourceUtil.DOCUMENT_ID, user);
        DocumentRevision documentRevision = new DocumentRevision(documentMaster, ResourceUtil.VERSION, user);
        List<DocumentIteration> iterations = new ArrayList<>();
        DocumentIteration documentIteration = new DocumentIteration(documentRevision, user);
        iterations.add(documentIteration);
        documentRevision.setDocumentIterations(iterations);

        SharedDocument sharedEntity = new SharedDocument(workspace, user, ResourceUtil.getFutureDate(), "password", documentRevision);

        String fullName = ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.VERSION + "/" + ResourceUtil.ITERATION + "/" + ResourceUtil.FILENAME1;

        BinaryResource binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());
        Mockito.when(documentService.getBinaryResource(fullName)).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(new FileInputStream(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));
        Mockito.when(publicEntityManager.getBinaryResourceForSharedEntity(fullName)).thenReturn(binaryResource);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)).thenReturn(false);
        String uuid = ResourceUtil.SHARED_DOC_ENTITY_UUID.split("/")[2];
        Mockito.when(shareService.findSharedEntityForGivenUUID(uuid)).thenReturn(sharedEntity);
        //When
        Response response = documentBinaryResource.downloadDocumentFile(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION, ResourceUtil.FILENAME1, ResourceUtil.FILE_TYPE, null, ResourceUtil.RANGE, uuid, "password", null);

        //Then
        assertNotNull(response);
        assertEquals(response.getStatus(), 206);
        assertEquals(response.getStatusInfo(), Response.Status.PARTIAL_CONTENT);


    }

    /**
     * Test to download a document file as a regular user who has read access on it
     *
     * @throws Exception
     */
    @Test
    public void downloadDocumentFileAsRegularUserWithAccessRights() throws Exception {
        //Given
        Request request = Mockito.mock(Request.class);
        //Workspace workspace, User author, Date expireDate, String password, DocumentRevision documentRevision
        Account account = Mockito.spy(new Account("user2", "user2", "user2@docdoku.com", "en", new Date(), null));
        Workspace workspace = new Workspace(ResourceUtil.WORKSPACE_ID, account, "pDescription", false);
        User user = new User(workspace, new Account("user1", "user1", "user1@docdoku.com", "en", new Date(), null));
        DocumentMaster documentMaster = new DocumentMaster(workspace, ResourceUtil.DOCUMENT_ID, user);
        DocumentRevision documentRevision = new DocumentRevision(documentMaster, ResourceUtil.VERSION, user);
        List<DocumentIteration> iterations = new ArrayList<>();
        DocumentIteration documentIteration = new DocumentIteration(documentRevision, user);
        iterations.add(documentIteration);
        documentRevision.setDocumentIterations(iterations);
        SharedDocument sharedEntity = new SharedDocument(workspace, user, ResourceUtil.getFutureDate(), "password", documentRevision);
        String fullName = ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.VERSION + "/" + ResourceUtil.ITERATION + "/" + ResourceUtil.FILENAME1;
        BinaryResource binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());

        Mockito.when(documentService.getBinaryResource(fullName)).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(new FileInputStream(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));
        Mockito.when(publicEntityManager.getBinaryResourceForSharedEntity(fullName)).thenReturn(binaryResource);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)).thenReturn(true);
        String uuid = ResourceUtil.SHARED_DOC_ENTITY_UUID.split("/")[2];
        Mockito.when(shareService.findSharedEntityForGivenUUID(uuid)).thenReturn(sharedEntity);
        //When
        Response response = documentBinaryResource.downloadDocumentFile(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION, ResourceUtil.FILENAME1, ResourceUtil.FILE_TYPE, null, ResourceUtil.RANGE, uuid, "password", null);

        //Then
        assertNotNull(response);
        assertEquals(response.getStatus(), 206);
        assertEquals(response.getStatusInfo(), Response.Status.PARTIAL_CONTENT);


    }

    /**
     * Test to download a document file as a regular user who has no read access on it
     *
     * @throws Exception
     */
    @Test
    public void downloadDocumentFileAsUserWithNoAccessRights() throws Exception {

        //Given
        Request request = Mockito.mock(Request.class);
        String fullName = ResourceUtil.WORKSPACE_ID + "/documents/" + ResourceUtil.DOCUMENT_ID + "/" + ResourceUtil.VERSION + "/" + ResourceUtil.ITERATION + "/" + ResourceUtil.FILENAME1;

        BinaryResource binaryResource = new BinaryResource(ResourceUtil.FILENAME1, ResourceUtil.DOCUMENT_SIZE, new Date());
        Mockito.when(documentService.canAccess(new DocumentIterationKey(ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION))).thenReturn(false);
        Mockito.when(documentService.getBinaryResource(fullName)).thenReturn(binaryResource);
        Mockito.when(storageManager.getBinaryResourceInputStream(binaryResource)).thenReturn(new FileInputStream(new File(ResourceUtil.getFilePath(ResourceUtil.SOURCE_FILE_STORAGE + ResourceUtil.FILENAME1))));
        Mockito.when(publicEntityManager.getBinaryResourceForSharedEntity(fullName)).thenReturn(binaryResource);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)).thenReturn(true);
        Mockito.when(publicEntityManager.canAccess(Matchers.any(DocumentIterationKey.class))).thenReturn(false);

        //When
        try {
            Response response = documentBinaryResource.downloadDocumentFile(request, ResourceUtil.WORKSPACE_ID, ResourceUtil.DOCUMENT_ID, ResourceUtil.VERSION, ResourceUtil.ITERATION, ResourceUtil.FILENAME1, ResourceUtil.FILE_TYPE, null, ResourceUtil.RANGE, null, null, null);
            assertTrue(false);
        } catch (SharedResourceAccessException e) {
            assertTrue(true);
        }

    }

}
