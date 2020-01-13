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


@ApiModel(value="FileDTO", description="This class is a helper for files representation")
public class FileDTO implements Serializable {

    @ApiModelProperty(value = "Created flag")
    private boolean created;

    @ApiModelProperty(value = "File path and name")
    private String fullName;

    @ApiModelProperty(value = "File name")
    private String shortName;

    public FileDTO(boolean created, String fullName, String shortName) {
        this.created = created;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public FileDTO() {
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
