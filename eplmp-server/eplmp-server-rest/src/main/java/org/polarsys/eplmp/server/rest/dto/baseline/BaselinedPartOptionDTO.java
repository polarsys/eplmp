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

import java.io.Serializable;


@ApiModel(value="BaselinedPartOptionDTO", description="This class holds status and iteration information of a {@link org.polarsys.eplmp.core.configuration.BaselinedPart} entity")
public class BaselinedPartOptionDTO implements Serializable {

    @ApiModelProperty(value = "Part version")
    private String version;

    @ApiModelProperty(value = "Part last iteration")
    private int lastIteration;

    @ApiModelProperty(value = "Part released flag")
    private boolean released;

    public BaselinedPartOptionDTO() {
    }

    public BaselinedPartOptionDTO(String version, int lastIteration, boolean released) {
        this.version = version;
        this.lastIteration = lastIteration;
        this.released = released;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLastIteration() {
        return lastIteration;
    }

    public void setLastIteration(int lastIteration) {
        this.lastIteration = lastIteration;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }
}
