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

/**
 * @author Morgan Guimard
 */

@ApiModel(value="ChangeRequestListDTO", description="This class holds a list of {@link org.polarsys.eplmp.core.change.ChangeRequest} entities")
public class ChangeRequestListDTO implements Serializable {

    @ApiModelProperty(value = "The list of change requests")
    private List<ChangeRequestDTO> requests;

    public ChangeRequestListDTO() {
    }

    public List<ChangeRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<ChangeRequestDTO> requests) {
        this.requests = requests;
    }
}
