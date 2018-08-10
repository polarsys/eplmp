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

package org.polarsys.eplmp.server.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.configuration.DocumentBaseline;
import org.polarsys.eplmp.core.configuration.DocumentBaselineType;
import org.polarsys.eplmp.core.configuration.DocumentCollection;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.services.IDocumentBaselineManagerLocal;
import org.polarsys.eplmp.server.rest.dto.baseline.BaselinedDocumentDTO;
import org.polarsys.eplmp.server.rest.dto.baseline.DocumentBaselineDTO;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.MockitoAnnotations.initMocks;

public class DocumentBaselinesResourceTest {

    @InjectMocks
    private DocumentBaselinesResource documentBaselinesResource = new DocumentBaselinesResource();

    @Mock
    private IDocumentBaselineManagerLocal documentBaselineService;

    private String workspaceId = "wks";

    @Before
    public void setup() throws Exception {
        initMocks(this);
        documentBaselinesResource.init();
    }

    @Test
    public void getDocumentBaselinesTest() throws ApplicationException {
        DocumentBaseline baseline1 = new DocumentBaseline();
        DocumentBaseline baseline2 = new DocumentBaseline();
        List<DocumentBaseline> list = Arrays.asList(baseline1, baseline2);
        Mockito.when(documentBaselineService.getBaselines(workspaceId))
                .thenReturn(list);
        Response res = documentBaselinesResource.getDocumentBaselines(workspaceId);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList result = (ArrayList) entity;
        Assert.assertEquals(list.size(), result.size());
    }

    @Test
    public void createDocumentBaselineTest() throws ApplicationException {
        DocumentBaseline baseline = new DocumentBaseline();
        baseline.setId(42);
        DocumentBaselineDTO documentBaselineDTO = new DocumentBaselineDTO();
        documentBaselineDTO.setName("foo");
        documentBaselineDTO.setType(DocumentBaselineType.LATEST);
        BaselinedDocumentDTO doc = new BaselinedDocumentDTO();
        doc.setDocumentMasterId("blah");
        doc.setVersion("A");
        doc.setIteration(1);
        List<BaselinedDocumentDTO> docs = Collections.singletonList(doc);
        documentBaselineDTO.setBaselinedDocuments(docs);
        List<BaselinedDocumentDTO> baselinedDocumentsDTO = documentBaselineDTO.getBaselinedDocuments();
        List<DocumentRevisionKey> documentRevisionKeys = baselinedDocumentsDTO.stream()
                .map(document -> new DocumentRevisionKey(workspaceId, document.getDocumentMasterId(), document.getVersion()))
                .collect(Collectors.toList());
        DocumentCollection docCollection = new DocumentCollection();

        Mockito.when(documentBaselineService.createBaseline(workspaceId, documentBaselineDTO.getName(),
                documentBaselineDTO.getType(), documentBaselineDTO.getDescription(), documentRevisionKeys))
                .thenReturn(baseline);

        Mockito.when(documentBaselineService.getBaselineLight(workspaceId, baseline.getId()))
                .thenReturn(baseline);

        Mockito.when(documentBaselineService.getACLFilteredDocumentCollection(workspaceId, baseline.getId()))
                .thenReturn(docCollection);


        Response res = documentBaselinesResource.createDocumentBaseline(workspaceId, documentBaselineDTO);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    }

    @Test
    public void deleteBaselineTest() throws ApplicationException {
        int baselineId = 42;
        Mockito.doNothing().when(documentBaselineService).deleteBaseline(workspaceId, baselineId);
        Response res = documentBaselinesResource.deleteBaseline(workspaceId, baselineId);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), res.getStatus());
    }

    @Test
    public void getBaselineTest() throws ApplicationException {
        DocumentBaseline baseline = new DocumentBaseline();
        baseline.setId(42);
        DocumentBaselineDTO documentBaselineDTO = new DocumentBaselineDTO();
        documentBaselineDTO.setName("foo");
        documentBaselineDTO.setType(DocumentBaselineType.LATEST);
        BaselinedDocumentDTO doc = new BaselinedDocumentDTO();
        doc.setDocumentMasterId("blah");
        doc.setVersion("A");
        doc.setIteration(1);
        List<BaselinedDocumentDTO> docs = Collections.singletonList(doc);
        documentBaselineDTO.setBaselinedDocuments(docs);

        DocumentCollection docCollection = new DocumentCollection();

        Mockito.when(documentBaselineService.getBaselineLight(workspaceId, baseline.getId()))
                .thenReturn(baseline);

        Mockito.when(documentBaselineService.getACLFilteredDocumentCollection(workspaceId, baseline.getId()))
                .thenReturn(docCollection);
        DocumentBaselineDTO result = documentBaselinesResource.getBaseline(workspaceId, baseline.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), baseline.getId());


    }

    @Test
    public void getBaselineLightTest() throws ApplicationException {
        DocumentBaseline baseline = new DocumentBaseline();
        baseline.setId(42);
        Mockito.when(documentBaselineService.getBaselineLight(workspaceId, baseline.getId()))
                .thenReturn(baseline);
        DocumentBaselineDTO baselineLight = documentBaselinesResource.getBaselineLight(workspaceId, baseline.getId());
        Assert.assertNotNull(baselineLight);

    }

    @Test
    public void exportDocumentFilesTest() throws ApplicationException {
        int baselineId = 42;
        DocumentBaseline baseline = new DocumentBaseline();
        baseline.setId(baselineId);
        Mockito.when(documentBaselineService.getBaselineLight(workspaceId, baselineId))
                .thenReturn(baseline);
        Response res = documentBaselinesResource.exportDocumentFiles(workspaceId, baselineId);
        Assert.assertNotNull(res.getEntity());
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
    }

}
