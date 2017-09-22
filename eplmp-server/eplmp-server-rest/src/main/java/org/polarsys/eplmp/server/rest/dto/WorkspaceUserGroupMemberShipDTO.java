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

@XmlRootElement
@ApiModel(value="WorkspaceUserGroupMemberShipDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.security.WorkspaceUserGroupMemberShip} entity")
public class WorkspaceUserGroupMemberShipDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Group concerned")
    private String memberId;

    @ApiModelProperty(value = "Read only flag")
    private boolean readOnly;

    public WorkspaceUserGroupMemberShipDTO() {
    }

    public WorkspaceUserGroupMemberShipDTO(String workspaceId, String memberId, boolean readOnly) {
        this.workspaceId = workspaceId;
        this.memberId = memberId;
        this.readOnly = readOnly;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
