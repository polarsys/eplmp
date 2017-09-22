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

import org.polarsys.eplmp.server.rest.dto.PartUsageLinkDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@ApiModel(value="PathChoiceDTO", description="This class is the representation of {@link org.polarsys.eplmp.core.configuration.PathChoice} entity")
public class PathChoiceDTO implements Serializable {

    @ApiModelProperty(value = "Complete path in context")
    private List<ResolvedPartLinkDTO> resolvedPath = new ArrayList<>();

    @ApiModelProperty(value = "Path concerned by the choice")
    private PartUsageLinkDTO partUsageLink;

    public PathChoiceDTO() {
    }

    public List<ResolvedPartLinkDTO> getResolvedPath() {
        return resolvedPath;
    }

    public void setResolvedPath(List<ResolvedPartLinkDTO> resolvedPath) {
        this.resolvedPath = resolvedPath;
    }

    public PartUsageLinkDTO getPartUsageLink() {
        return partUsageLink;
    }

    public void setPartUsageLink(PartUsageLinkDTO partUsageLink) {
        this.partUsageLink = partUsageLink;
    }
}
