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

@XmlRootElement
@ApiModel(value="PartCreationDTO", description="Use this class to create a new {@link org.polarsys.eplmp.core.product.PartMaster} entity")
public class PartCreationDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Part number")
    private String number;

    @ApiModelProperty(value = "Part version")
    private String version;

    @ApiModelProperty(value = "Part name")
    private String name;

    @ApiModelProperty(value = "Part description")
    private String description;

    @ApiModelProperty(value = "Workflow to instantiate")
    private String workflowModelId;

    @ApiModelProperty(value = "Standard part flag")
    private boolean standardPart;

    @ApiModelProperty(value = "Template to use")
    private String templateId;

    @ApiModelProperty(value = "Role mapping for instantiated workflow")
    private RoleMappingDTO[] roleMapping;

    @ApiModelProperty(value = "Part ACL")
    private ACLDTO acl;

    public PartCreationDTO() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStandardPart() {
        return standardPart;
    }

    public void setStandardPart(boolean standardPart) {
        this.standardPart = standardPart;
    }

    public String getWorkflowModelId() {
        return workflowModelId;
    }

    public void setWorkflowModelId(String workflowModelId) {
        this.workflowModelId = workflowModelId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public RoleMappingDTO[] getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(RoleMappingDTO[] roleMapping) {
        this.roleMapping = roleMapping;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }
}
