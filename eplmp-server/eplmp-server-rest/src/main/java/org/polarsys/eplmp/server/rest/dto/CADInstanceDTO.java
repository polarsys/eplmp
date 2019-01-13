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
import org.polarsys.eplmp.core.product.RotationType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@ApiModel(value="CADInstanceDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.product.CADInstance} entity")
public class CADInstanceDTO implements Serializable {

    @ApiModelProperty(value = "Rotation around x Axis")
    private Double rx;

    @ApiModelProperty(value = "Rotation around y Axis")
    private Double ry;

    @ApiModelProperty(value = "Rotation around z Axis")
    private Double rz;

    @ApiModelProperty(value = "Translation on x Axis")
    private Double tx;

    @ApiModelProperty(value = "Translation on y Axis")
    private Double ty;

    @ApiModelProperty(value = "Translation on z Axis")
    private Double tz;

    @ApiModelProperty(value = "4x4 Transformation matrix")
    private double[] matrix;

    @ApiModelProperty(value = "Transformation type")
    private RotationType rotationType;


    public CADInstanceDTO() {
    }

    public CADInstanceDTO(Double rx, Double ry, Double rz, Double tx, Double ty, Double tz) {
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public double[] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[] matrix) {
        this.matrix = matrix;
    }

    public RotationType getRotationType() {
        return rotationType;
    }

    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    public Double getRx() {
        return rx;
    }

    public void setRx(Double rx) {
        this.rx = rx;
    }

    public Double getRy() {
        return ry;
    }

    public void setRy(Double ry) {
        this.ry = ry;
    }

    public Double getRz() {
        return rz;
    }

    public void setRz(Double rz) {
        this.rz = rz;
    }

    public Double getTx() {
        return tx;
    }

    public void setTx(Double tx) {
        this.tx = tx;
    }

    public Double getTy() {
        return ty;
    }

    public void setTy(Double ty) {
        this.ty = ty;
    }

    public Double getTz() {
        return tz;
    }

    public void setTz(Double tz) {
        this.tz = tz;
    }

}
