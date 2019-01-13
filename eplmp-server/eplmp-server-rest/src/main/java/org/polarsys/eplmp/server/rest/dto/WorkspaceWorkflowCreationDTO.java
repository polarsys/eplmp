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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value="WorkspaceWorkflowCreationDTO", description="Use this class to create a new {@link org.polarsys.eplmp.core.workflow.WorkspaceWorkflow} entity")
public class WorkspaceWorkflowCreationDTO implements Serializable {

    @ApiModelProperty(value = "Workspace workflow id")
    private String id;

    @ApiModelProperty(value = "Workflow model to use")
    private String workflowModelId;

    @ApiModelProperty(value = "Role mapping for instantiated workflow")
    private RoleMappingDTO[] roleMapping;

    public WorkspaceWorkflowCreationDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleMappingDTO[] getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(RoleMappingDTO[] roleMapping) {
        this.roleMapping = roleMapping;
    }

    public String getWorkflowModelId() {
        return workflowModelId;
    }

    public void setWorkflowModelId(String workflowModelId) {
        this.workflowModelId = workflowModelId;
    }
}
