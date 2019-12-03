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

package org.polarsys.eplmp.server.rest;

import org.jose4j.keys.HmacKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PartRevisionKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.core.sharing.SharedDocument;
import org.polarsys.eplmp.core.sharing.SharedEntity;
import org.polarsys.eplmp.core.sharing.SharedPart;
import org.polarsys.eplmp.core.util.HashUtils;
import org.polarsys.eplmp.server.config.AuthConfig;

import javax.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;

public class SharedResourceTest {

    @InjectMocks
    private SharedResource sharedResource = new SharedResource();

    @Mock
    private IPublicEntityManagerLocal publicEntityManager;
    @Mock
    private IDocumentManagerLocal documentManager;
    @Mock
    private IProductManagerLocal productManager;
    @Mock
    private IShareManagerLocal shareManager;
    @Mock
    private IContextManagerLocal contextManager;
    @Mock
    private AuthConfig authConfig;

    private String workspaceId = "wks";
    private String partNumber = "partM";
    private String documentId = "docM";
    private String documentVersion = "A";
    private String partVersion = "A";

    @Before
    public void setup() throws Exception {
        initMocks(this);
        sharedResource.init();
        Key key = new HmacKey("verySecretPhrase".getBytes("UTF-8"));
        Mockito.when(authConfig.getJWTKey()).thenReturn(key);
    }

    @Test
    public void getPublicSharedDocumentRevisionTest() throws ApplicationException {

        DocumentRevisionKey docKey = new DocumentRevisionKey(workspaceId, documentId, documentVersion);
        DocumentRevision documentRevision = new DocumentRevision();
        documentRevision.setLocation(new Folder(workspaceId, "somepath"));

        Mockito.when(publicEntityManager.getPublicDocumentRevision(docKey))
            .thenReturn(null);
        Response response = sharedResource.getPublicSharedDocumentRevision(workspaceId, documentId, documentVersion);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

        Mockito.when(documentManager.getDocumentRevision(docKey))
                .thenReturn(null);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID))
                .thenReturn(true);

        response = sharedResource.getPublicSharedDocumentRevision(workspaceId, documentId, documentVersion);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());


        Mockito.when(publicEntityManager.getPublicDocumentRevision(docKey))
                .thenReturn(documentRevision);

        response = sharedResource.getPublicSharedDocumentRevision(workspaceId, documentId, documentVersion);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    @Test
    public void getPublicSharedPartRevisionTest() throws ApplicationException {
        PartRevisionKey partKey = new PartRevisionKey(workspaceId, partNumber, partVersion);
        PartRevision partRevision = new PartRevision();
        partRevision.setPartMasterNumber(partNumber);
        partRevision.setVersion(partVersion);
        PartMaster partMaster = new PartMaster();
        partMaster.setType("type");
        partRevision.setPartMaster(partMaster);
        User author = new User();
        partRevision.setAuthor(author);

        Mockito.when(publicEntityManager.getPublicPartRevision(partKey))
                .thenReturn(null);
        Response response = sharedResource.getPublicSharedPartRevision(workspaceId, partNumber, partVersion);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

        Mockito.when(productManager.getPartRevision(partKey))
                .thenReturn(null);
        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID))
                .thenReturn(true);

        response = sharedResource.getPublicSharedPartRevision(workspaceId, partNumber, partVersion);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());


        Mockito.when(publicEntityManager.getPublicPartRevision(partKey))
                .thenReturn(partRevision);

        response = sharedResource.getPublicSharedPartRevision(workspaceId, partNumber, partVersion);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    @Test
    public void getDocumentWithSharedEntityTest() throws ApplicationException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String uuid = "uuid";
        Workspace workspace = new Workspace(workspaceId);

        User user = new User();
        DocumentRevision document = new DocumentRevision();
        SharedEntity sharedEntity = new SharedDocument(workspace, user, document);
        sharedEntity.setUuid(uuid);

        Mockito.when(shareManager.findSharedEntityForGivenUUID(uuid))
                        .thenReturn(sharedEntity);

        Response response = sharedResource.getDocumentWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, -2);

        sharedEntity.setExpireDate(c.getTime());
        response = sharedResource.getDocumentWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        Assert.assertEquals("{\"forbidden\":\"entity-expired\"}", response.getEntity());

        c.setTime(new Date());
        c.add(Calendar.HOUR, 4);

        sharedEntity.setExpireDate(c.getTime());
        response = sharedResource.getDocumentWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());

        sharedEntity.setPassword(HashUtils.md5Sum("foo"));
        response = sharedResource.getDocumentWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

        response = sharedResource.getDocumentWithSharedEntity("foo", uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());

    }

    @Test
    public void getPartWithSharedEntityTest() throws ApplicationException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String uuid = "uuid";
        Workspace workspace = new Workspace(workspaceId);

        User user = new User();
        PartRevision partRevision = new PartRevision();
        partRevision.setPartMasterNumber(partNumber);
        partRevision.setVersion(partVersion);
        PartMaster partMaster = new PartMaster();
        partMaster.setType("type");
        partRevision.setPartMaster(partMaster);
        User author = new User();
        partRevision.setAuthor(author);
        SharedEntity sharedEntity = new SharedPart(workspace, user, partRevision);
        sharedEntity.setUuid(uuid);

        Mockito.when(shareManager.findSharedEntityForGivenUUID(uuid))
                .thenReturn(sharedEntity);

        Response response = sharedResource.getPartWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, -2);

        sharedEntity.setExpireDate(c.getTime());
        response = sharedResource.getPartWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        Assert.assertEquals("{\"forbidden\":\"entity-expired\"}", response.getEntity());

        c.setTime(new Date());
        c.add(Calendar.HOUR, 4);

        sharedEntity.setExpireDate(c.getTime());
        response = sharedResource.getPartWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());

        sharedEntity.setPassword(HashUtils.md5Sum("foo"));
        response = sharedResource.getPartWithSharedEntity(null, uuid);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

        response = sharedResource.getPartWithSharedEntity("foo", uuid);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());

    }

}
