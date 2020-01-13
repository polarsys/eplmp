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
import java.util.List;

/**
 * @author Morgan Guimard
 */

@ApiModel(value="PartIterationListDTO", description="This class is wraps a list of {@link org.polarsys.eplmp.core.change.PartIteration} entities")
public class PartIterationListDTO implements Serializable {

    @ApiModelProperty(value = "The list of part iterations")
    private List<PartIterationDTO> parts;

    public PartIterationListDTO() {
    }

    public List<PartIterationDTO> getParts() {
        return parts;
    }

    public void setParts(List<PartIterationDTO> parts) {
        this.parts = parts;
    }
}


