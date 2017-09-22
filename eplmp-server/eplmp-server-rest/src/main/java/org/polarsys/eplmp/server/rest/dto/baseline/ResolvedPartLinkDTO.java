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

package org.polarsys.eplmp.server.rest.dto.baseline;

import org.polarsys.eplmp.server.rest.dto.LightPartLinkDTO;
import org.polarsys.eplmp.server.rest.dto.PartIterationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@ApiModel(value="ResolvedPartLinkDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.product.PartLink} and its resolved {@link org.polarsys.eplmp.core.product.PartIteration} in context")
public class ResolvedPartLinkDTO implements Serializable {

    @ApiModelProperty(value = "Resolved part iteration")
    private PartIterationDTO partIteration;

    @ApiModelProperty(value = "Usage Link")
    private LightPartLinkDTO partLink;

    public ResolvedPartLinkDTO() {
    }

    public PartIterationDTO getPartIteration() {
        return partIteration;
    }

    public void setPartIteration(PartIterationDTO partIteration) {
        this.partIteration = partIteration;
    }

    public LightPartLinkDTO getPartLink() {
        return partLink;
    }

    public void setPartLink(LightPartLinkDTO partLink) {
        this.partLink = partLink;
    }
}
