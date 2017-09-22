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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Florent Garin
 */
@XmlRootElement
@ApiModel(value="DocumentTemplateCreationDTO", description="Use this class to create a new {@link org.polarsys.eplmp.core.document.DocumentTemplateMaster} entity")
public class DocumentTemplateCreationDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Document template id")
    private String reference;

    @ApiModelProperty(value = "Document template type")
    private String documentType;

    @ApiModelProperty(value = "Document template generate mode")
    private boolean idGenerated;

    @ApiModelProperty(value = "Document template mask")
    private String mask;

    @ApiModelProperty(value = "Document template associated workflow id")
    private String workflowModelId;

    @ApiModelProperty(value = "Document template attached files")
    private List<BinaryResourceDTO> attachedFiles;

    @ApiModelProperty(value = "Document template attribute templates")
    private List<InstanceAttributeTemplateDTO> attributeTemplates;

    @ApiModelProperty(value = "Document template attributes locked flag")
    private boolean attributesLocked;

    public DocumentTemplateCreationDTO() {

    }

    public DocumentTemplateCreationDTO(String workspaceId, String documentType) {
        this.workspaceId = workspaceId;
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isAttributesLocked() {
        return attributesLocked;
    }

    public void setAttributesLocked(boolean attributesLocked) {
        this.attributesLocked = attributesLocked;
    }
}
