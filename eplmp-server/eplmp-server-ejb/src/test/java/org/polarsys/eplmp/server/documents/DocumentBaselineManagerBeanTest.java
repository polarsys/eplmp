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

package org.polarsys.eplmp.server.documents;

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.DocumentBaseline;
import org.polarsys.eplmp.core.configuration.DocumentBaselineType;
import org.polarsys.eplmp.core.document.DocumentMaster;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.i18n.PropertiesLoader;
import org.polarsys.eplmp.server.BinaryStorageManagerBean;
import org.polarsys.eplmp.server.dao.WorkspaceDAO;
import org.polarsys.eplmp.server.util.DocumentUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentBaselineManagerBeanTest {

    private final static String PROPERTIES_BASE_NAME = "/org/polarsys/eplmp/core/i18n/LocalStrings";

    @InjectMocks
    DocumentBaselineManagerBean docBaselineManagerBean = new DocumentBaselineManagerBean();
    @Mock
    IDocumentManagerLocal documentManagerLocal;
    @Mock
    IUserManagerLocal userManager;
    @Mock
    EntityManager em;
    @Mock
    BinaryStorageManagerBean dataManager;
    @Mock
    TypedQuery<Folder> folderTypedQuery;
    @Mock
    IDocumentManagerLocal documentService;


    private Account account = new Account(DocumentUtil.USER_2_LOGIN, DocumentUtil.USER_2_NAME, DocumentUtil.USER2_MAIL, DocumentUtil.LANGUAGE, new Date(), null);
    private Workspace workspace = new Workspace("workspace01", account, DocumentUtil.WORKSPACE_DESCRIPTION, false);
    private User user = new User(workspace, new Account(DocumentUtil.USER_1_LOGIN, DocumentUtil.USER_1_NAME, DocumentUtil.USER1_MAIL, DocumentUtil.LANGUAGE, new Date(), null));
    private Folder folder = new Folder("workspace01");


    /**
     * test that we cannot baseline an empty collection of documents
     *
     * @throws Exception
     */
    @Test
    public void shouldNotBaselineAnEmptyCollection() throws Exception {
        //Given
        Mockito.when(userManager.checkWorkspaceWriteAccess(workspace.getId())).thenReturn(user);
        Mockito.when(em.find(Workspace.class, workspace.getId())).thenReturn(workspace);
        Mockito.when(em.createQuery("SELECT DISTINCT f FROM Folder f WHERE f.parentFolder.completePath = :completePath", Folder.class)).thenReturn(folderTypedQuery);
        Mockito.when(folderTypedQuery.getResultList()).thenReturn(new ArrayList<>(0));
        Mockito.when(new WorkspaceDAO(new Locale("en"), em).loadWorkspace(workspace.getId())).thenReturn(workspace);
        Mockito.when(em.find(Folder.class, workspace.getId())).thenReturn(folder);
        Mockito.when(documentService.getAllDocumentsInWorkspace(workspace.getId())).thenReturn(new DocumentRevision[0]);

        //when
        try {
            docBaselineManagerBean.createBaseline(workspace.getId(), "name", DocumentBaselineType.RELEASED, "description", new ArrayList<>());
        } catch (NotAllowedException e) {
            Properties properties = PropertiesLoader.loadLocalizedProperties(new Locale(user.getLanguage()), PROPERTIES_BASE_NAME, getClass());
            String expected = properties.getProperty("NotAllowedException66");
            Assert.assertEquals(expected, e.getMessage());
        }

    }


    /**
     * test that we can baseline documents
     *
     * @throws Exception
     */
    @Test
    public void baselineDocuments() throws Exception {
        //Given
        Mockito.when(userManager.checkWorkspaceWriteAccess(workspace.getId())).thenReturn(user);
        Mockito.when(em.find(Workspace.class, workspace.getId())).thenReturn(workspace);
        Mockito.when(em.createQuery("SELECT DISTINCT f FROM Folder f WHERE f.parentFolder.completePath = :completePath", Folder.class)).thenReturn(folderTypedQuery);
        Mockito.when(folderTypedQuery.getResultList()).thenReturn(new ArrayList<>(0));
        Mockito.when(new WorkspaceDAO(new Locale("en"), em).loadWorkspace(workspace.getId())).thenReturn(workspace);
        Mockito.when(em.find(Folder.class, workspace.getId())).thenReturn(folder);
        DocumentRevision[] revisions = new DocumentRevision[2];

        DocumentMaster documentMaster1 = new DocumentMaster(workspace, "doc1", user);
        DocumentMaster documentMaster2 = new DocumentMaster(workspace, "doc2", user);
        documentMaster1.setId("doc001");
        documentMaster2.setId("doc002");

        DocumentRevision documentRevision1 = documentMaster1.createNextRevision(user);
        DocumentRevision documentRevision2 = documentMaster2.createNextRevision(user);

        revisions[0] = documentRevision2;
        revisions[1] = documentRevision1;
        documentRevision1.createNextIteration(user);
        documentRevision2.createNextIteration(user);
        documentRevision1.setLocation(folder);
        documentRevision2.setLocation(folder);
        Mockito.when(em.find(DocumentRevision.class, documentRevision1.getKey())).thenReturn(documentRevision1);
        Mockito.when(em.find(DocumentRevision.class, documentRevision2.getKey())).thenReturn(documentRevision2);

        Mockito.when(documentService.getAllDocumentsInWorkspace(workspace.getId())).thenReturn(revisions);
        Mockito.when(userManager.checkWorkspaceReadAccess(workspace.getId())).thenReturn(user);


        //when
        List<DocumentRevisionKey> documentRevisionKeys = new ArrayList<>();
        documentRevisionKeys.add(new DocumentRevisionKey(workspace.getId(), documentMaster1.getId(), documentMaster1.getLastRevision().getVersion()));
        documentRevisionKeys.add(new DocumentRevisionKey(workspace.getId(), documentMaster2.getId(), documentMaster2.getLastRevision().getVersion()));
        DocumentBaseline documentBaseline = docBaselineManagerBean.createBaseline(workspace.getId(), "name", DocumentBaselineType.LATEST, "description", documentRevisionKeys);

        //Then
        Assert.assertTrue(documentBaseline != null);
        Assert.assertTrue(documentBaseline.hasBaselinedDocument(documentRevision1.getKey()));
        Assert.assertTrue(documentBaseline.hasBaselinedDocument(documentRevision2.getKey()));
        Assert.assertNotNull(documentBaseline.getBaselinedDocument(documentRevision1.getKey()));
        Assert.assertNotNull(documentBaseline.getBaselinedDocument(documentRevision2.getKey()));
    }

}
