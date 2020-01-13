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


@ApiModel(value = "ImportPreviewDTO", description = "This class is a representation of an {@link org.polarsys.eplmp.core.product.ImportPreview} entity")
public class ImportPreviewDTO implements Serializable {

    @ApiModelProperty(value = "Part revisions that will be checked out")
    private List<LightPartRevisionDTO> partRevsToCheckout;


    @ApiModelProperty(value = "Part masters that will be created")
    private List<PartCreationDTO> partsToCreate;

    public ImportPreviewDTO() {
    }

    public ImportPreviewDTO(List<LightPartRevisionDTO> partRevsToCheckout, List<PartCreationDTO> partsToCreate) {
        this.partRevsToCheckout = partRevsToCheckout;
        this.partsToCreate = partsToCreate;
    }

    public List<LightPartRevisionDTO> getPartRevsToCheckout() {
        return partRevsToCheckout;
    }

    public void setPartRevsToCheckout(List<LightPartRevisionDTO> partRevsToCheckout) {
        this.partRevsToCheckout = partRevsToCheckout;
    }

    public List<PartCreationDTO> getPartsToCreate() {
        return partsToCreate;
    }

    public void setPartsToCreate(List<PartCreationDTO> partsToCreate) {
        this.partsToCreate = partsToCreate;
    }
}
