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
@ApiModel(value="WorkspaceUserMemberShipDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.security.WorkspaceUserMemberShip} entity")
public class WorkspaceUserMemberShipDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "User concerned")
    private UserDTO member;

    @ApiModelProperty(value = "Read only flag")
    private boolean readOnly;

    public WorkspaceUserMemberShipDTO() {
    }

    public WorkspaceUserMemberShipDTO(String workspaceId, UserDTO member, boolean readOnly) {
        this.workspaceId = workspaceId;
        this.member = member;
        this.readOnly = readOnly;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public UserDTO getMember() {
        return member;
    }

    public void setMember(UserDTO member) {
        this.member = member;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
