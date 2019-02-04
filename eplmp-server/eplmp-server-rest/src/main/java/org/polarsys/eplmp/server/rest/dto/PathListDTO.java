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

@XmlRootElement
@ApiModel(value="PathListDTO", description="This class holds a list of strings representing multiple sequences of {@link org.polarsys.eplmp.core.product.PartLink} entities")
public class PathListDTO implements Serializable {

    @ApiModelProperty(value = "Config spec in use")
    private String configSpec;

    @ApiModelProperty(value = "Path values")
    private String[] paths;

    public PathListDTO() {
    }

    public PathListDTO(String configSpec, String[] paths) {
        this.configSpec = configSpec;
        this.paths = paths;
    }

    public String getConfigSpec() {
        return configSpec;
    }

    public void setConfigSpec(String configSpec) {
        this.configSpec = configSpec;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }
}
