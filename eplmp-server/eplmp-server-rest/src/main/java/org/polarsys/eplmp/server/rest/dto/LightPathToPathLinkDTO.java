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

@XmlRootElement
@ApiModel(value="LightPathToPathLinkDTO", description="This class is a light representation of a {@link org.polarsys.eplmp.core.product.PathToPathLink} entity")
public class LightPathToPathLinkDTO implements Serializable {

    @ApiModelProperty(value = "Path to path link id")
    private Integer id;

    @ApiModelProperty(value = "Path to path link type")
    private String type;

    @ApiModelProperty(value = "Path to path link source path")
    private String sourcePath;

    @ApiModelProperty(value = "Path to path link target path")
    private String targetPath;

    @ApiModelProperty(value = "Path to path link description")
    private String description;

    public LightPathToPathLinkDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
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
