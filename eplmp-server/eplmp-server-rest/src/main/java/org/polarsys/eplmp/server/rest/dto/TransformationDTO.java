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

/**
 * @author Florent Garin
 */

@XmlRootElement
@ApiModel(value = "TransformationDTO", description = "This class is a representation of a geometric transformation")
public class TransformationDTO implements Serializable, Cloneable {

    @ApiModelProperty(value = "Translation on x Axis")
    private double tx;

    @ApiModelProperty(value = "Translation on y Axis")
    private double ty;

    @ApiModelProperty(value = "Translation on z Axis")
    private double tz;

    @ApiModelProperty(value = "Rotation around x Axis")
    private double rx;

    @ApiModelProperty(value = "Rotation around y Axis")
    private double ry;

    @ApiModelProperty(value = "Rotation around z Axis")
    private double rz;


    public TransformationDTO() {

    }

    public TransformationDTO(double tx, double ty, double tz, double rx, double ry, double rz) {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public void setTy(double ty) {
        this.ty = ty;
    }

    public double getTz() {
        return tz;
    }

    public void setTz(double tz) {
        this.tz = tz;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public double getRz() {
        return rz;
    }

    public void setRz(double rz) {
        this.rz = rz;
    }

    @Override
    public TransformationDTO clone() {
        TransformationDTO clone;
        try {
            clone = (TransformationDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
}
