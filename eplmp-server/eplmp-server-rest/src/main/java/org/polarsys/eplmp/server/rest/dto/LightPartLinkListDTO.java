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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@ApiModel(value="LightPartLinkListDTO", description="This class wraps a list of {@link org.polarsys.eplmp.core.product.PartLink} entities")
public class LightPartLinkListDTO implements Serializable {

    @ApiModelProperty(value = "The list of light part links")
    private List<LightPartLinkDTO> partLinks = new ArrayList<>();

    public LightPartLinkListDTO() {
    }

    public LightPartLinkListDTO(List<LightPartLinkDTO> partLinks) {
        this.partLinks = partLinks;
    }

    public List<LightPartLinkDTO> getPartLinks() {
        return partLinks;
    }

    public void setPartLinks(List<LightPartLinkDTO> partLinks) {
        this.partLinks = partLinks;
    }
}
