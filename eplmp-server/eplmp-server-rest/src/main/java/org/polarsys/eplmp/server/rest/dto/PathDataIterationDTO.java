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
import java.util.Set;

/**
 * @author Chadid Asmae
 */
@XmlRootElement
@ApiModel(value = "PathDataIterationDTO", description = "This class is a representation of a {@link org.polarsys.eplmp.core.product.PathDataIteration} entity")
public class PathDataIterationDTO implements Serializable {

    @ApiModelProperty(value = "Product instance serial number")
    private String serialNumber;

    @ApiModelProperty(value = "Path data master id")
    private int pathDataMasterId;

    @ApiModelProperty(value = "Path data iteration number")
    private int iteration;

    @ApiModelProperty(value = "Path data iteration note")
    private String iterationNote;

    @ApiModelProperty(value = "List of part links")
    private LightPartLinkListDTO partLinksList;

    @ApiModelProperty(value = "Complete path in context")
    private String path;

    @ApiModelProperty(value = "Path data iteration attached files")
    private List<BinaryResourceDTO> attachedFiles;

    @ApiModelProperty(value = "Path data iteration linked documents")
    private Set<DocumentRevisionDTO> linkedDocuments;

    @ApiModelProperty(value = "Path data iteration attributes")
    private List<InstanceAttributeDTO> instanceAttributes;

    public PathDataIterationDTO() {
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

    public LightPartLinkListDTO getPartLinksList() {
        return partLinksList;
    }

    public void setPartLinksList(LightPartLinkListDTO partLinksList) {
        this.partLinksList = partLinksList;
    }

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Set<DocumentRevisionDTO> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(Set<DocumentRevisionDTO> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public List<InstanceAttributeDTO> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(List<InstanceAttributeDTO> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPathDataMasterId() {
        return pathDataMasterId;
    }

    public void setPathDataMasterId(int pathDataMasterId) {
        this.pathDataMasterId = pathDataMasterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathDataIterationDTO dto = (PathDataIterationDTO) o;

        return iteration == dto.iteration && pathDataMasterId == dto.pathDataMasterId;
    }

    @Override
    public int hashCode() {
        int result = pathDataMasterId;
        result = 31 * result + iteration;
        return result;
    }
}
