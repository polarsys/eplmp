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
 * @author Charles Fallourd on 23/03/16.
 */
@XmlRootElement
@ApiModel(value="UserGroupDTO", description="This class is a representation of an {@link org.polarsys.eplmp.core.common.UserGroup} entity")
public class UserGroupDTO implements Serializable{

    @ApiModelProperty(value = "User group id")
    private String id;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    public UserGroupDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
}
