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
import org.polarsys.eplmp.core.util.DateUtils;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.util.Date;


@ApiModel(value="ConversionDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.product.Conversion} entity")
public class ConversionDTO implements Serializable {

    @ApiModelProperty(value = "Conversion end date")
    @JsonbDateFormat(value = DateUtils.GLOBAL_DATE_FORMAT)
    private Date endDate;

    @ApiModelProperty(value = "Conversion start date")
    @JsonbDateFormat(value = DateUtils.GLOBAL_DATE_FORMAT)
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
