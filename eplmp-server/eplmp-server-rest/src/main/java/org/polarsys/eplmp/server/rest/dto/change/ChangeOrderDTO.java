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

package org.polarsys.eplmp.server.rest.dto.change;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


@ApiModel(value="ChangeOrderDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.change.ChangeOrder} entity")
public class ChangeOrderDTO extends ChangeItemDTO implements Serializable {

    @ApiModelProperty(value = "Change order addressed requests")
    private List<ChangeRequestDTO> addressedChangeRequests;

    @ApiModelProperty(value = "Change order due milestone id")

    private int milestoneId;

    public ChangeOrderDTO() {

    }

    public List<ChangeRequestDTO> getAddressedChangeRequests() {
        return addressedChangeRequests;
    }

    public void setAddressedChangeRequests(List<ChangeRequestDTO> addressedChangeRequests) {
        this.addressedChangeRequests = addressedChangeRequests;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }
}
