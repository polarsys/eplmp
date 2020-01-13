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


@ApiModel(value="WorkspaceDetailsDTO", description="This class is light a representation of a {@link org.polarsys.eplmp.core.workflow.Workflow} entity")
public class WorkspaceDetailsDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String id;

    @ApiModelProperty(value = "Workspace admin login")
    private String admin;

    @ApiModelProperty(value = "Workspace description")
    private String description;

    public WorkspaceDetailsDTO() {
    }

    public WorkspaceDetailsDTO(String id, String admin, String description) {
        this.id = id;
        this.admin = admin;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
