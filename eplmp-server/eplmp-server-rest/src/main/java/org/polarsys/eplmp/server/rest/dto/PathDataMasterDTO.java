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
import java.util.ArrayList;
import java.util.List;


@ApiModel(value="PathDataMasterDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.PathDataMaster} entity")
public class PathDataMasterDTO implements Serializable {

    @ApiModelProperty(value = "Path data master id")
    private Integer id;

    @ApiModelProperty(value = "Complete path in context")
    private String path;

    @ApiModelProperty(value = "Product instance serial number")
    private String serialNumber;

    @ApiModelProperty(value = "List of part links")
    private LightPartLinkListDTO partLinksList;

    @ApiModelProperty(value = "Path data master iterations")
    private List<PathDataIterationDTO> pathDataIterations = new ArrayList<>();

    @ApiModelProperty(value = "Path data master attributes")
    private List<InstanceAttributeDTO> partAttributes;

    @ApiModelProperty(value = "Path data master attribute templates")
    private List<InstanceAttributeTemplateDTO> partAttributeTemplates;

    public PathDataMasterDTO() {
    }

    public PathDataMasterDTO(String path) {
        this.path = path;
    }

    public PathDataMasterDTO(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LightPartLinkListDTO getPartLinksList() {
        return partLinksList;
    }

    public void setPartLinksList(LightPartLinkListDTO partLinksList) {
        this.partLinksList = partLinksList;
    }

    public List<PathDataIterationDTO> getPathDataIterations() {
        return pathDataIterations;
    }

    public void setPathDataIterations(List<PathDataIterationDTO> pathDataIterations) {
        this.pathDataIterations = pathDataIterations;
    }

    public List<InstanceAttributeDTO> getPartAttributes() {
        return partAttributes;
    }

    public void setPartAttributes(List<InstanceAttributeDTO> partAttributes) {
        this.partAttributes = partAttributes;
    }

    public List<InstanceAttributeTemplateDTO> getPartAttributeTemplates() {
        return partAttributeTemplates;
    }

    public void setPartAttributeTemplates(List<InstanceAttributeTemplateDTO> partAttributeTemplates) {
        this.partAttributeTemplates = partAttributeTemplates;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathDataMasterDTO that = (PathDataMasterDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
