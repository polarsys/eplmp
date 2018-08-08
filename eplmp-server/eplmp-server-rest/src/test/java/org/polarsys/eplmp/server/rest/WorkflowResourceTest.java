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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.services.IWorkflowManagerLocal;
import org.polarsys.eplmp.core.workflow.Workflow;
import org.polarsys.eplmp.server.rest.dto.WorkflowDTO;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Morgan Guimard
 */
public class WorkflowResourceTest {

    @InjectMocks
    private WorkflowResource workflowResource = new WorkflowResource();

    @Mock
    private IWorkflowManagerLocal workflowService;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        workflowResource.init();
    }

    @Test
    public void getWorkflowInstanceTest() throws ApplicationException {
        int workflowId = 42;

        Workflow workflow = new Workflow();
        workflow.setId(workflowId);
        Mockito.when(workflowService.getWorkflow(Matchers.anyString(), Matchers.anyInt())).thenReturn(workflow);

        WorkflowDTO workflowDTO = workflowResource.getWorkflowInstance("wks", workflowId);
        Assert.assertNotNull(workflowDTO);
        Assert.assertEquals(workflowId, workflowDTO.getId());
    }

    @Test
    public void getWorkflowAbortedWorkflowListTest() throws ApplicationException {
        int workflowId = 42;
        Workflow workflow = new Workflow();
        workflow.setId(workflowId);
        Workflow[] list = new Workflow[]{workflow};
        Mockito.when(workflowService.getWorkflowAbortedWorkflowList(Matchers.anyString(), Matchers.anyInt())).thenReturn(list);

        Response res = workflowResource.getWorkflowAbortedWorkflowList("wks", workflowId);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        List workflowList = (ArrayList) entity;
        Object workflowEntity = workflowList.get(0);
        Assert.assertTrue(workflowEntity.getClass().isAssignableFrom(WorkflowDTO.class));
        WorkflowDTO workflowDTO = (WorkflowDTO) workflowEntity;
        Assert.assertNotNull(workflowDTO);
        Assert.assertEquals(workflowId, workflowDTO.getId());
    }
}
