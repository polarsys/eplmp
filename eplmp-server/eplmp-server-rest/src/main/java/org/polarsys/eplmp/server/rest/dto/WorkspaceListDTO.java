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
import java.util.ArrayList;
import java.util.List;


@ApiModel(value="WorkspaceListDTO", description="This class holds a list of {@link org.polarsys.eplmp.core.common.Workspace} entities")
public class WorkspaceListDTO implements Serializable {

    @ApiModelProperty(value = "Administrated workspace list")
    private List<WorkspaceDTO> administratedWorkspaces = new ArrayList<>();

    @ApiModelProperty(value = "All workspace list")
    private List<WorkspaceDTO> allWorkspaces = new ArrayList<>();

    public WorkspaceListDTO() {
    }

    public void addAdministratedWorkspaces(WorkspaceDTO workspace) {
        this.administratedWorkspaces.add(workspace);
    }

    public List<WorkspaceDTO> getAdministratedWorkspaces() {
        return administratedWorkspaces;
    }

    public void setAdministratedWorkspaces(List<WorkspaceDTO> administratedWorkspaces) {
        this.administratedWorkspaces = administratedWorkspaces;
    }

    public List<WorkspaceDTO> getAllWorkspaces() {
        return allWorkspaces;
    }

    public void setAllWorkspaces(List<WorkspaceDTO> allWorkspaces) {
        this.allWorkspaces = allWorkspaces;
    }

    public void addAllWorkspaces(WorkspaceDTO workspace) {
        this.allWorkspaces.add(workspace);
    }
}
