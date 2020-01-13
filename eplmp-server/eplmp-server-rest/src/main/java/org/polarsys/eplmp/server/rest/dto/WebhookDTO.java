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


@ApiModel(value = "WebhookDTO", description = "This class is a representation of a {@link org.polarsys.eplmp.core.hooks.Webhook} entity")
public class WebhookDTO implements Serializable {

    @ApiModelProperty(value = "Webhook id")
    private int id;

    @ApiModelProperty(value = "Webhook name")
    private String name;

    @ApiModelProperty(value = "Webhook active flag")
    private boolean active;

    @ApiModelProperty(value = "Webhook parameters list")
    private List<WebhookAppParameterDTO> parameters;

    @ApiModelProperty(value = "Webhook app name")
    private String appName;

    public WebhookDTO() {
    }

    public WebhookDTO(int id, String name, boolean active, List<WebhookAppParameterDTO> parameters, String appName) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.parameters = parameters;
        this.appName = appName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<WebhookAppParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<WebhookAppParameterDTO> parameters) {
        this.parameters = parameters;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}

