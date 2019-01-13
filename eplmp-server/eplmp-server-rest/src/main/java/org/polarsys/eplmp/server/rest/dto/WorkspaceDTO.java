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

@XmlRootElement
@ApiModel(value="WorkspaceDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.common.Workspace} entity")
public class WorkspaceDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String id;

    @ApiModelProperty(value = "Workspace description")
    private String description;

    @ApiModelProperty(value = "Folder locked flag")
    private boolean folderLocked;

    @ApiModelProperty(value = "Workspace enabled flag")
    private boolean enabled;

    public WorkspaceDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFolderLocked() {
        return folderLocked;
    }

    public void setFolderLocked(boolean folderLocked) {
        this.folderLocked = folderLocked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
