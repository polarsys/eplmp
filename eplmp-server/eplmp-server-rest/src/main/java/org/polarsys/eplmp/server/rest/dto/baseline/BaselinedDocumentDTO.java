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

package org.polarsys.eplmp.server.rest.dto.baseline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@ApiModel(value="BaselinedDocumentDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.configuration.BaselinedDocument} entity")
public class BaselinedDocumentDTO implements Serializable {

    @ApiModelProperty(value = "Document master id")
    private String documentMasterId;

    @ApiModelProperty(value = "Document title")
    private String title;

    @ApiModelProperty(value = "Document version")
    private String version;

    @ApiModelProperty(value = "Document iteration")
    private int iteration;

    @ApiModelProperty(value = "Document available iterations")
    private List<BaselinedDocumentOptionDTO> availableIterations;

    public BaselinedDocumentDTO() {
    }

    public BaselinedDocumentDTO(String documentMasterId, String version, int iteration, String title) {
        this.documentMasterId = documentMasterId;
        this.version = version;
        this.title = title;
        this.iteration = iteration;
    }

    public String getDocumentMasterId() {
        return documentMasterId;
    }

    public void setDocumentMasterId(String documentMasterId) {
        this.documentMasterId = documentMasterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public List<BaselinedDocumentOptionDTO> getAvailableIterations() {
        return availableIterations;
    }

    public void setAvailableIterations(List<BaselinedDocumentOptionDTO> availableIterations) {
        this.availableIterations = availableIterations;
    }
}
