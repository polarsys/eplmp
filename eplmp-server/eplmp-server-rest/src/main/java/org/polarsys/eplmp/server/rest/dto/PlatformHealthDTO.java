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
@ApiModel(value = "PlatformHealthDTO",
        description = "This class represents the server's health-check result")
public class PlatformHealthDTO implements Serializable {

    @ApiModelProperty(value = "Platform status code")
    private String status;

    @ApiModelProperty(value = "Health check duration")
    private long executionTime;

    public PlatformHealthDTO() {
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
