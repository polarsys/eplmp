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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value="UserStatsDTO",
        description="This class is a representation of user stats in workspace")
public class UserStatsDTO implements Serializable {

    @ApiModelProperty(value = "Users count")
    private Integer users;

    @ApiModelProperty(value = "Active users count")
    private Integer activeusers;

    @ApiModelProperty(value = "Inactive users count")
    private Integer inactiveusers;

    @ApiModelProperty(value = "Groups count")
    private Integer groups;

    @ApiModelProperty(value = "Active groups count")
    private Integer activegroups;

    @ApiModelProperty(value = "Inactive groups count")
    private Integer inactivegroups;

    public UserStatsDTO() {
    }


    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getActiveusers() {
        return activeusers;
    }

    public void setActiveusers(Integer activeusers) {
        this.activeusers = activeusers;
    }

    public Integer getInactiveusers() {
        return inactiveusers;
    }

    public void setInactiveusers(Integer inactiveusers) {
        this.inactiveusers = inactiveusers;
    }

    public Integer getGroups() {
        return groups;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public Integer getActivegroups() {
        return activegroups;
    }

    public void setActivegroups(Integer activegroups) {
        this.activegroups = activegroups;
    }

    public Integer getInactivegroups() {
        return inactivegroups;
    }

    public void setInactivegroups(Integer inactivegroups) {
        this.inactivegroups = inactivegroups;
    }
}
