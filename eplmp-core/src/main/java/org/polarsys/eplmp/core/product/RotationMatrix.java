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

package org.polarsys.eplmp.core.product;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * A double precision floating point 3 by 3 matrix to support 3D rotations.
 *
 * @author Charles Fallourd
 * @version 2.5, 01/03/16
 * @since V2.5
 */
@Embeddable
public class RotationMatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    private double m00, m01, m02, m10, m11, m12, m20, m21, m22;

    public RotationMatrix() {

    }

    public RotationMatrix(double[] values) {
        if (values != null) {
            m00 = values[0];
            m01 = values[3];
            m02 = values[6];
            m10 = values[1];
            m11 = values[4];
            m12 = values[7];
            m20 = values[2];
            m21 = values[5];
            m22 = values[8];
        }
    }

    public double getM00() {
        return m00;
    }

    public void setM00(double m00) {
        this.m00 = m00;
    }

    public double getM01() {
        return m01;
    }

    public void setM01(double m01) {
        this.m01 = m01;
    }

    public double getM02() {
        return m02;
    }

    public void setM02(double m02) {
        this.m02 = m02;
    }

    public double getM10() {
        return m10;
    }

    public void setM10(double m10) {
        this.m10 = m10;
    }

    public double getM11() {
        return m11;
    }

    public void setM11(double m11) {
        this.m11 = m11;
    }

    public double getM12() {
        return m12;
    }

    public void setM12(double m12) {
        this.m12 = m12;
    }

    public double getM20() {
        return m20;
    }

    public void setM20(double m20) {
        this.m20 = m20;
    }

    public double getM21() {
        return m21;
    }

    public void setM21(double m21) {
        this.m21 = m21;
    }

    public double getM22() {
        return m22;
    }

    public void setM22(double m22) {
        this.m22 = m22;
    }

    @Transient
    public double[] getValues() {
        return new double[]{m00, m01, m02, m10, m11, m12, m20, m21, m22};
    }
}
