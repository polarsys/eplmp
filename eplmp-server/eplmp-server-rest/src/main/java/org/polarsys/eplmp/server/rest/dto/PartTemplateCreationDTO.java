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
import java.util.List;

@XmlRootElement
@ApiModel(value="PartTemplateCreationDTO", description="Use this class to create a new {@link org.polarsys.eplmp.core.product.PartTemplateMaster} entity")
public class PartTemplateCreationDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Part template reference")
    private String reference;

    @ApiModelProperty(value = "Part template type")
    private String partType;

    @ApiModelProperty(value = "Generate id flag")
    private boolean idGenerated;

    @ApiModelProperty(value = "Part template mask")
    private String mask;

    @ApiModelProperty(value = "Part template attached files")
    private String attachedFiles;

    @ApiModelProperty(value = "Part template attributes")
    private List<InstanceAttributeTemplateDTO> attributeTemplates;

    @ApiModelProperty(value = "Part template attribute templates")
    private List<InstanceAttributeTemplateDTO> attributeInstanceTemplates;

    @ApiModelProperty(value = "Attributes locked flag")
    private boolean attributesLocked;

    @ApiModelProperty(value = "Workflow to instantiate for part creation")
    private String workflowModelId;

    public PartTemplateCreationDTO() {
    }

    public PartTemplateCreationDTO(String workspaceId, String partType) {
        this.workspaceId = workspaceId;
        this.partType = partType;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
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

    public String getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(String attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public boolean isIdGenerated() {
        return idGenerated;
    }

    public void setIdGenerated(boolean idGenerated) {
        this.idGenerated = idGenerated;
    }

    public List<InstanceAttributeTemplateDTO> getAttributeTemplates() {
        return attributeTemplates;
    }

    public void setAttributeTemplates(List<InstanceAttributeTemplateDTO> attributeTemplates) {
        this.attributeTemplates = attributeTemplates;
    }

    public List<InstanceAttributeTemplateDTO> getAttributeInstanceTemplates() {
        return attributeInstanceTemplates;
    }

    public void setAttributeInstanceTemplates(List<InstanceAttributeTemplateDTO> attributeInstanceTemplates) {
        this.attributeInstanceTemplates = attributeInstanceTemplates;
    }

    public boolean isAttributesLocked() {
        return attributesLocked;
    }

    public void setAttributesLocked(boolean attributesLocked) {
        this.attributesLocked = attributesLocked;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
