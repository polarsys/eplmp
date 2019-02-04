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
import java.util.List;

/**
 * @author Yassine Belouad
 */
@XmlRootElement
@ApiModel(value="RoleDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.workflow.Role} entity")
public class RoleDTO implements Serializable {

    @ApiModelProperty(value = "Role id")
    private String id;

    @ApiModelProperty(value = "Role name")
    private String name;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Default assigned users for role")
    private List<UserDTO> defaultAssignedUsers;

    @ApiModelProperty(value = "Default assigned groups for role")
    private List<UserGroupDTO> defaultAssignedGroups;

    public RoleDTO() {
    }

    public RoleDTO(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public List<UserDTO> getDefaultAssignedUsers() {
        return defaultAssignedUsers;
    }

    public void setDefaultAssignedUsers(List<UserDTO> defaultAssignedUsers) {
        this.defaultAssignedUsers = defaultAssignedUsers;
    }

    public List<UserGroupDTO> getDefaultAssignedGroups() {
        return defaultAssignedGroups;
    }

    public void setDefaultAssignedGroups(List<UserGroupDTO> defaultAssignedGroups) {
        this.defaultAssignedGroups = defaultAssignedGroups;
    }
}
