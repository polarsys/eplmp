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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.services.IWorkflowManagerLocal;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflow;
import org.polarsys.eplmp.server.rest.dto.RoleMappingDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceWorkflowCreationDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceWorkflowDTO;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Morgan Guimard
 */
public class WorkspaceWorkflowResourceTest {

    @InjectMocks
    private WorkspaceWorkflowResource workspaceWorkflowResource = new WorkspaceWorkflowResource();

    @Mock
    private IWorkflowManagerLocal workflowService;

    private String workspaceId = "wks";

    @Before
    public void init() {
        initMocks(this);
        workspaceWorkflowResource.init();
    }

    @Test
    public void getWorkspaceWorkflowListTest() throws ApplicationException {
        WorkspaceWorkflow workflow = new WorkspaceWorkflow();
        workflow.setId("id");
        WorkspaceWorkflow[] list = new WorkspaceWorkflow[]{workflow};
        Mockito.when(workflowService.getWorkspaceWorkflowList(workspaceId))
                .thenReturn(list);

        Response res = workspaceWorkflowResource.getWorkspaceWorkflowList(workspaceId);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList result = (ArrayList) entity;
        Object o = result.get(0);
        Assert.assertTrue(o.getClass().isAssignableFrom(WorkspaceWorkflowDTO.class));
        WorkspaceWorkflowDTO workflowDTO = (WorkspaceWorkflowDTO) o;
        Assert.assertEquals(workflow.getId(), String.valueOf(workflowDTO.getId()));
    }

    @Test
    public void getWorkspaceWorkflowTest() throws ApplicationException {
        String workspaceWorkflowId = "id";
        WorkspaceWorkflow workflow = new WorkspaceWorkflow();
        Mockito.when(workflowService.getWorkspaceWorkflow(workspaceId, workspaceWorkflowId))
                .thenReturn(workflow);
        WorkspaceWorkflowDTO workspaceWorkflow = workspaceWorkflowResource.getWorkspaceWorkflow(workspaceId,
                workspaceWorkflowId);
        Assert.assertEquals(workflow.getId(), workspaceWorkflow.getId());
    }

    @Test
    public void createWorkspaceWorkflowTest() throws ApplicationException {
        WorkspaceWorkflowCreationDTO workflowCreationDTO = new WorkspaceWorkflowCreationDTO();
        workflowCreationDTO.setId("id");
        RoleMappingDTO roleMappingDTO = new RoleMappingDTO();
        roleMappingDTO.setRoleName("role");
        List<String> loginList = Arrays.asList("foo", "bar");
        roleMappingDTO.setUserLogins(loginList);
        RoleMappingDTO[] roleMapping = new RoleMappingDTO[]{roleMappingDTO};
        workflowCreationDTO.setRoleMapping(roleMapping);
        workflowCreationDTO.setWorkflowModelId("modelId");

        WorkspaceWorkflow workspaceWorkflow = new WorkspaceWorkflow();
        workspaceWorkflow.setId("id");

        Mockito.when(workflowService.instantiateWorkflow(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(workspaceWorkflow);

        WorkspaceWorkflowDTO result = workspaceWorkflowResource.createWorkspaceWorkflow(workspaceId,
                workflowCreationDTO);

        Assert.assertEquals(workflowCreationDTO.getId(), result.getId());

    }

    @Test
    public void deleteWorkspaceWorkflowTest() throws ApplicationException {
        Mockito.doNothing().when(workflowService).deleteWorkspaceWorkflow(workspaceId, "id");
        Response res = workspaceWorkflowResource.deleteWorkspaceWorkflow(workspaceId, "id");
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), res.getStatus());
    }

}
