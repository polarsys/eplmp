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

/**
 *
 * @author laurentlevan
 */
@XmlRootElement
@ApiModel(value="LightPartRevisionDTO", description="This class is a light representation of a {@link org.polarsys.eplmp.core.product.PartRevision} entity")
public class LightPartRevisionDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Part number")
    private String partNumber;

    @ApiModelProperty(value = "Part version")
    private String version;

    public LightPartRevisionDTO() {
    }

    public LightPartRevisionDTO(String workspaceId, String partNumber, String version){
        this.workspaceId = workspaceId;
        this.partNumber = partNumber;
        this.version = version;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
