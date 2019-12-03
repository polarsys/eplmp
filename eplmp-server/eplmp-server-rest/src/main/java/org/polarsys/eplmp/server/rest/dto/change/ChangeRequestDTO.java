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

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.List;


@ApiModel(value="ChangeRequestDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.change.ChangeRequest} entity")
public class ChangeRequestDTO extends ChangeItemDTO implements Serializable {

    @ApiModelProperty(value = "Change request addressed issues")
    private List<ChangeIssueDTO> addressedChangeIssues;

    @ApiModelProperty(value = "Change request due milestone id")
    @JsonbProperty(nillable = true)
    private int milestoneId;

    public ChangeRequestDTO() {

    }

    public List<ChangeIssueDTO> getAddressedChangeIssues() {
        return addressedChangeIssues;
    }

    public void setAddressedChangeIssues(List<ChangeIssueDTO> addressedChangeRequests) {
        this.addressedChangeIssues = addressedChangeRequests;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }
}
