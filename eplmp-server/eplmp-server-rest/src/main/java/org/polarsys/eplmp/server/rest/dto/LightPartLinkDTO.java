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


@ApiModel(value="LightPartLinkDTO", description="This class is a light representation of a {@link org.polarsys.eplmp.core.product.PartLink} entity")
public class LightPartLinkDTO implements Serializable {

    @ApiModelProperty(value = "Part number")
    private String number;

    @ApiModelProperty(value = "Part name")
    private String name;

    @ApiModelProperty(value = "Link description")
    private String referenceDescription;

    @ApiModelProperty(value = "Complete component path in context")
    private String fullId;

    public LightPartLinkDTO() {
    }

    public LightPartLinkDTO(String number, String name, String referenceDescription, String fullId) {
        this.number = number;
        this.name = name;
        this.referenceDescription = referenceDescription;
        this.fullId = fullId;
    }
/*

    public LightPartLinkDTO(PartLink partLink) {
        number = partLink.getComponent().getNumber();
        name = partLink.getComponent().getName();
        referenceDescription = partLink.getReferenceDescription();
        fullId = partLink.getFullId();
    }
*/

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferenceDescription() {
        return referenceDescription;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
}
