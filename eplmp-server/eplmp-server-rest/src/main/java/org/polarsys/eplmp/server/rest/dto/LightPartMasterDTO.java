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

@XmlRootElement
@ApiModel(value="LightPartMasterDTO", description="This class is a light representation of a {@link org.polarsys.eplmp.core.product.PartMaster} entity")
public class LightPartMasterDTO implements Serializable {

    @ApiModelProperty(value = "Part number")
    private String partNumber;

    @ApiModelProperty(value = "Part name")
    private String partName;

    public LightPartMasterDTO() {
    }

    public LightPartMasterDTO(String partNumber, String partName) {
        this.partNumber = partNumber;
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
