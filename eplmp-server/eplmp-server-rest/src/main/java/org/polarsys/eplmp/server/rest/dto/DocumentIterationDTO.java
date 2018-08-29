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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Florent Garin
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value="DocumentIterationDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.document.DocumentIteration} entity")
public class DocumentIterationDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Document iteration id")
    private String id;

    @ApiModelProperty(value = "Document master id")
    private String documentMasterId;

    @ApiModelProperty(value = "Document version")
    private String version;

    @ApiModelProperty(value = "Document iteration")
    private int iteration;

    @ApiModelProperty(value = "Document creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Document modification date")
    private Date modificationDate;

    @ApiModelProperty(value = "Document check in date")
    private Date checkInDate;

    @ApiModelProperty(value = "Document title")
    private String title;

    @ApiModelProperty(value = "Document author")
    private UserDTO author;

    @XmlElement(nillable = true)
    @ApiModelProperty(value = "Revision note")
    private String revisionNote;

    @ApiModelProperty(value = "Document iteration attached files")
    private List<BinaryResourceDTO> attachedFiles;

    @ApiModelProperty(value = "Document iteration attributes")
    private List<InstanceAttributeDTO> instanceAttributes;

    @ApiModelProperty(value = "Document iteration linked documents")
    private List<DocumentRevisionDTO> linkedDocuments;

    public DocumentIterationDTO() {
    }

    public DocumentIterationDTO(String pWorkspaceId, String pDocumentMasterId, String pVersion, int pIteration) {
        workspaceId = pWorkspaceId;
        documentMasterId = pDocumentMasterId;
        version = pVersion;
        iteration = pIteration;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public String getId() {
        return documentMasterId + "-" + version + "-" + iteration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getRevisionNote() {
        return revisionNote;
    }

    public void setRevisionNote(String pRevisionNote) {
        revisionNote = pRevisionNote;
    }

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public List<DocumentRevisionDTO> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(List<DocumentRevisionDTO> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public List<InstanceAttributeDTO> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(List<InstanceAttributeDTO> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }

    @Override
    public String toString() {
        return workspaceId + "-" + documentMasterId + "-" + version + "-" + iteration;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String pWorkspaceId) {
        workspaceId = pWorkspaceId;
    }

    public String getDocumentMasterId() {
        return documentMasterId;
    }

    public void setDocumentMasterId(String pDocumentMasterId) {
        documentMasterId = pDocumentMasterId;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int pIteration) {
        iteration = pIteration;
    }

    public DocumentRevisionDTO getDocumentRevision() {
        return new DocumentRevisionDTO(workspaceId, id + "-" + version, version);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
