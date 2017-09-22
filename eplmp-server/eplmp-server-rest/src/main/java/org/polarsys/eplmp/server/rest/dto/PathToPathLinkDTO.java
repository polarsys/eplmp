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

@XmlRootElement
@ApiModel(value="PathToPathLinkDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.PathToPathLink} entity")
public class PathToPathLinkDTO implements Serializable {

    @ApiModelProperty(value = "Path to path link id")
    private Integer id;

    @ApiModelProperty(value = "Path to path link type")
    private String type;

    @ApiModelProperty(value = "Source path")
    private List<LightPartLinkDTO> sourceComponents;

    @ApiModelProperty(value = "Target path")
    private List<LightPartLinkDTO> targetComponents;

    @ApiModelProperty(value = "Link description")
    private String description;

    public PathToPathLinkDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LightPartLinkDTO> getSourceComponents() {
        return sourceComponents;
    }

    public void setSourceComponents(List<LightPartLinkDTO> sourceComponents) {
        this.sourceComponents = sourceComponents;
    }

    public List<LightPartLinkDTO> getTargetComponents() {
        return targetComponents;
    }

    public void setTargetComponents(List<LightPartLinkDTO> targetComponents) {
        this.targetComponents = targetComponents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
