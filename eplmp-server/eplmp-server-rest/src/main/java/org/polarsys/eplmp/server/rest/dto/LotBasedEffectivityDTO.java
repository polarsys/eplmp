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


@ApiModel(value = "LotBasedEffectivityDTO",
        description = "This class is the representation of a {@link org.polarsys.eplmp.core.common.LotBasedEffectivity} entity",
        parent = EffectivityDTO.class)
public class LotBasedEffectivityDTO extends EffectivityDTO implements Serializable {

    @ApiModelProperty(value = "Start number of the Lot")
    private String startLotId;

    @ApiModelProperty(value = "End number of the Lot")
    private String endLotId;

    public LotBasedEffectivityDTO() {
    }

    public String getStartLotId() {
        return startLotId;
    }

    public void setStartLotId(String startLotId) {
        this.startLotId = startLotId;
    }

    public String getEndLotId() {
        return endLotId;
    }

    public void setEndLotId(String endLotId) {
        this.endLotId = endLotId;
    }
}
