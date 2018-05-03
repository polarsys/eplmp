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
import java.util.Date;

@XmlRootElement
@ApiModel(value = "DateBasedEffectivityDTO",
        description = "This class is the representation of a {@link org.polarsys.eplmp.core.common.DateBasedEffectivity} entity",
        parent = EffectivityDTO.class)
public class DateBasedEffectivityDTO extends EffectivityDTO implements Serializable {

    @ApiModelProperty(value = "Start date of the Effectivity")
    private Date startDate;

    @ApiModelProperty(value = "End date of the Effectivity")
    private Date endDate;

    public DateBasedEffectivityDTO() {
    }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
