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
@ApiModel(value="LayerDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.Layer} entity")
public class LayerDTO implements Serializable {

    @ApiModelProperty(value = "Layer id")
    private int id;

    @ApiModelProperty(value = "Layer name")
    private String name;

    @ApiModelProperty(value = "Layer color as hexadecimal")
    private String color;

    public LayerDTO() {
    }

    public LayerDTO(String pName) {
        this.name = pName;
    }

    public LayerDTO(int pId, String pName, String color) {
        this.id = pId;
        this.name = pName;
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
