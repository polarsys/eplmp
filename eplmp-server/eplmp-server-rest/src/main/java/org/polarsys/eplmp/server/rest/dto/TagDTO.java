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

/**
 * @author Yassine Belouad
 */


@ApiModel(value="TagDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.meta.Tag} entity")
public class TagDTO implements Serializable {

    @ApiModelProperty(value = "Tag id")

    private String id;

    @ApiModelProperty(value = "Tag label")

    private String label;

    @ApiModelProperty(value = "Workspace id")

    private String workspaceId;

    public TagDTO() {
    }

    public TagDTO(String label) {
        this.label = label;
    }

    public TagDTO(String label, String workspaceId) {
        this.id = label;
        this.label = label;
        this.workspaceId = workspaceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getId() {
        id = this.label;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
