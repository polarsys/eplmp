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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan Guimard
 */

@XmlRootElement
@ApiModel(value="RoleMappingDTO", description="Use this class to provide roles mapping for {@link org.polarsys.eplmp.core.workflow.Workflow} entity instantiation")
public class RoleMappingDTO implements Serializable {

    @ApiModelProperty(value = "Role name")
    private String roleName;

    @ApiModelProperty(value = "Mapped users")
    private List<String> userLogins = new ArrayList<>();

    @ApiModelProperty(value = "Mapped groups")
    private List<String> groupIds = new ArrayList<>();

    public RoleMappingDTO() {
    }

    public RoleMappingDTO(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getUserLogins() {
        return userLogins;
    }

    public void setUserLogins(List<String> userLogins) {
        this.userLogins = userLogins;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
