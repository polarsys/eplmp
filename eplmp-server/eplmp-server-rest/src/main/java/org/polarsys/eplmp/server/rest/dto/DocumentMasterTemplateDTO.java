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
import java.util.Date;
import java.util.List;

/**
 * @author Florent Garin
 */
@XmlRootElement
@ApiModel(value="DocumentMasterTemplateDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.document.DocumentMasterTemplate} entity")
public class DocumentMasterTemplateDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Document master template id")
    private String id;

    @ApiModelProperty(value = "Document master template type")
    private String documentType;

    @ApiModelProperty(value = "Document master template author")
    private UserDTO author;

    @ApiModelProperty(value = "Document master template creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Document master template modification date")
    private Date modificationDate;

    @ApiModelProperty(value = "Is generated flag")
    private boolean idGenerated;

    @ApiModelProperty(value = "Document master template mask")
    private String mask;

    @ApiModelProperty(value = "Workflow id to use when instantiating documents from template")
    private String workflowModelId;

    @ApiModelProperty(value = "Document master template attached files")
    private List<BinaryResourceDTO> attachedFiles;

    @ApiModelProperty(value = "Document master template attribute templates")
    private List<InstanceAttributeTemplateDTO> attributeTemplates;

    @ApiModelProperty(value = "Attribute templates locked flag")
    private boolean attributesLocked;

    @ApiModelProperty(value = "Document master template ACL")
    private ACLDTO acl;

    public DocumentMasterTemplateDTO() {

    }

    public DocumentMasterTemplateDTO(String workspaceId, String id, String documentType) {
        this.workspaceId = workspaceId;
        this.id = id;
        this.documentType = documentType;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
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

    public boolean isAttributesLocked() {
        return attributesLocked;
    }

    public void setAttributesLocked(boolean attributesLocked) {
        this.attributesLocked = attributesLocked;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }
}
