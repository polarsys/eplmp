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
 * @author Florent Garin
 */
@XmlRootElement
@ApiModel(value="UserDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.common.User} entity")
public class UserDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "User login")
    private String login;

    @ApiModelProperty(value = "User name")
    private String name;

    @ApiModelProperty(value = "User email")
    private String email;

    @ApiModelProperty(value = "User language")
    private String language;

    @ApiModelProperty(value = "User workspace membership")
    private WorkspaceMembership membership;

    public UserDTO() {
    }

    public UserDTO(String workspaceId, String login, String name) {
        this.workspaceId = workspaceId;
        this.login = login;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public WorkspaceMembership getMembership() {
        return membership;
    }

    public void setMembership(WorkspaceMembership membership) {
        this.membership = membership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
