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

package org.polarsys.eplmp.server.rest.dto.baseline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@ApiModel(value="BaselinedPartDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.configuration.BaselinedPart} entity")
public class BaselinedPartDTO implements Serializable {

    @ApiModelProperty(value = "Part number")
    private String number;

    @ApiModelProperty(value = "Part name")
    private String name;

    @ApiModelProperty(value = "Part version")
    private String version;

    @ApiModelProperty(value = "Part iteration")
    private int iteration;

    @ApiModelProperty(value = "Part available iterations")
    private List<BaselinedPartOptionDTO> availableIterations;

    public BaselinedPartDTO() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public List<BaselinedPartOptionDTO> getAvailableIterations() {
        return availableIterations;
    }

    public void setAvailableIterations(List<BaselinedPartOptionDTO> availableIterations) {
        this.availableIterations = availableIterations;
    }

}
