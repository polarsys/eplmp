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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Chadid Asmae
 */

@ApiModel(value="PathDataIterationCreationDTO", description="Use this class to create a new {@link org.polarsys.eplmp.core.product.PathDataIteration} entity")
public class PathDataIterationCreationDTO implements Serializable {

    @ApiModelProperty(value = "Path data iteration id")
    private int id;

    @ApiModelProperty(value = "Complete path in context")
    private String path;

    @ApiModelProperty(value = "Path data iteration number")
    private int iteration;

    @ApiModelProperty(value = "Path data iteration note")
    private String iterationNote;

    @ApiModelProperty(value = "Path data iteration attached files")
    private List<String> attachedFiles;

    @ApiModelProperty(value = "Path data iteration linked documents")
    private Set<DocumentRevisionDTO> linkedDocuments;

    @ApiModelProperty(value = "Path data iteration attributes")
    private List<InstanceAttributeDTO> instanceAttributes;

    public PathDataIterationCreationDTO() {
    }

    public PathDataIterationCreationDTO(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public List<String> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<String> attachedFiles) {
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

}
