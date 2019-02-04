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

package org.polarsys.eplmp.server.rest.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.server.rest.dto.*;
import org.polarsys.eplmp.server.rest.dto.baseline.BaselinedPartDTO;
import org.polarsys.eplmp.server.rest.dto.baseline.ProductBaselineDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@XmlRootElement
@ApiModel(value="ProductInstanceIterationDTO", description="This class is the representation of {@link org.polarsys.eplmp.core.configuration.ProductInstanceIteration} entity")
public class ProductInstanceIterationDTO implements Serializable {

    @ApiModelProperty(value = "Product instance serial number")
    private String serialNumber;

    @ApiModelProperty(value = "Product instance iteration")
    private int iteration;

    @ApiModelProperty(value = "Product instance iteration note")
    private String iterationNote;

    @ApiModelProperty(value = "Configuration item used")
    private String configurationItemId;

    @ApiModelProperty(value = "Product instance last update author login")
    private String updateAuthor;

    @ApiModelProperty(value = "Product instance last update author name")
    private String updateAuthorName;

    @ApiModelProperty(value = "Product instance last modification date")
    private Date modificationDate;

    @ApiModelProperty(value = "Product instance baselined parts")
    private List<BaselinedPartDTO> baselinedParts;

    @ApiModelProperty(value = "Product instance substitute links retained")
    private List<String> substituteLinks;

    @ApiModelProperty(value = "Product instance optional links retained")
    private List<String> optionalUsageLinks;

    @ApiModelProperty(value = "Product instance author")
    private UserDTO author;

    @ApiModelProperty(value = "Product instance creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Product instance substitute links retained as parts")
    private List<LightPartLinkListDTO> substitutesParts;

    @ApiModelProperty(value = "Product instance optional links retained as parts")
    private List<LightPartLinkListDTO> optionalsParts;

    @ApiModelProperty(value = "Product instance path to path links in structure")
    private List<PathToPathLinkDTO> pathToPathLinks;

    @ApiModelProperty(value = "Product instance baseline in use")
    private ProductBaselineDTO basedOn;

    @ApiModelProperty(value = "Product instance path data list")
    private List<PathDataMasterDTO> pathDataMasterList;

    @ApiModelProperty(value = "Product instance path data locations")
    private List<LightPartLinkListDTO> pathDataPaths;

    @ApiModelProperty(value = "Product instance attributes")
    private List<InstanceAttributeDTO> instanceAttributes = new ArrayList<>();

    @ApiModelProperty(value = "Product instance linked documents")
    private Set<DocumentRevisionDTO> linkedDocuments = new HashSet<>();

    @ApiModelProperty(value = "Product instance attached files")
    private List<BinaryResourceDTO> attachedFiles;

    public ProductInstanceIterationDTO() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String getIterationNote() {
        return iterationNote;
    }

    public void setIterationNote(String iterationNote) {
        this.iterationNote = iterationNote;
    }

    public String getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(String configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

    public String getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(String updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getUpdateAuthorName() {
        return updateAuthorName;
    }

    public void setUpdateAuthorName(String updateAuthorName) {
        this.updateAuthorName = updateAuthorName;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public List<BaselinedPartDTO> getBaselinedParts() {
        return baselinedParts;
    }

    public void setBaselinedParts(List<BaselinedPartDTO> baselinedParts) {
        this.baselinedParts = baselinedParts;
    }

    public List<String> getOptionalUsageLinks() {
        return optionalUsageLinks;
    }

    public void setOptionalUsageLinks(List<String> optionalUsageLinks) {
        this.optionalUsageLinks = optionalUsageLinks;
    }

    public List<InstanceAttributeDTO> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(List<InstanceAttributeDTO> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }

    public Set<DocumentRevisionDTO> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(Set<DocumentRevisionDTO> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public ProductBaselineDTO getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(ProductBaselineDTO basedOn) {
        this.basedOn = basedOn;
    }

    public List<PathDataMasterDTO> getPathDataMasterList() {
        return pathDataMasterList;
    }

    public void setPathDataMasterList(List<PathDataMasterDTO> pathDataMasterList) {
        this.pathDataMasterList = pathDataMasterList;
    }

    public List<String> getSubstituteLinks() {
        return substituteLinks;
    }

    public void setSubstituteLinks(List<String> substituteLinks) {
        this.substituteLinks = substituteLinks;
    }

    public List<LightPartLinkListDTO> getSubstitutesParts() {
        return substitutesParts;
    }

    public void setSubstitutesParts(List<LightPartLinkListDTO> substitutesParts) {
        this.substitutesParts = substitutesParts;
    }

    public List<LightPartLinkListDTO> getOptionalsParts() {
        return optionalsParts;
    }

    public void setOptionalsParts(List<LightPartLinkListDTO> optionalsParts) {
        this.optionalsParts = optionalsParts;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<LightPartLinkListDTO> getPathDataPaths() {
        return pathDataPaths;
    }

    public void setPathDataPaths(List<LightPartLinkListDTO> pathDataPaths) {
        this.pathDataPaths = pathDataPaths;
    }

    public List<PathToPathLinkDTO> getPathToPathLinks() {
        return this.pathToPathLinks;
    }

    public void setPathToPathLinks(List<PathToPathLinkDTO> pathToPathLinks) {
        this.pathToPathLinks = pathToPathLinks;
    }
}
