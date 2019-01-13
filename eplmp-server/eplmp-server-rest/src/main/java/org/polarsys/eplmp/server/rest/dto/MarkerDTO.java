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
@ApiModel(value="MarkerDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.Marker} entity")
public class MarkerDTO implements Serializable {

    @ApiModelProperty(value = "Marker id")
    private int id;

    @ApiModelProperty(value = "Marker title")
    private String title;

    @ApiModelProperty(value = "Marker description")
    private String description;

    private double x;
    private double y;
    private double z;

    public MarkerDTO() {
    }

    public MarkerDTO(String pTitle, String pDescription, double pX, double pY, double pZ) {
        this.title = pTitle;
        this.description = pDescription;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    public MarkerDTO(int pId, String pTitle, String pDescription, double pX, double pY, double pZ) {
        this.id = pId;
        this.title = pTitle;
        this.description = pDescription;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
