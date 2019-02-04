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
import java.util.Date;

@XmlRootElement
@ApiModel(value="ConversionDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.product.Conversion} entity")
public class ConversionDTO implements Serializable {

    @ApiModelProperty(value = "Conversion end date")
    private Date endDate;

    @ApiModelProperty(value = "Conversion start date")
    private Date startDate;

    @ApiModelProperty(value = "Success flag")
    private boolean succeed;

    @ApiModelProperty(value = "Pending flag")
    private boolean pending;

    public ConversionDTO() {
    }

    public ConversionDTO(Date endDate, Date startDate, boolean succeed, boolean pending) {
        this.endDate = endDate;
        this.startDate = startDate;
        this.succeed = succeed;
        this.pending = pending;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
