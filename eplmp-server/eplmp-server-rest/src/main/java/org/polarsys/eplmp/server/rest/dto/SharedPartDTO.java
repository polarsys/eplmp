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
import org.polarsys.eplmp.core.util.DateUtils;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Morgan Guimard
 */

@ApiModel(value = "SharedPartDTO", description = "This class is a representation of a {@link org.polarsys.eplmp.core.sharing.SharedPart} entity")
public class SharedPartDTO implements Serializable {

    @ApiModelProperty(value = "Shared part token")
    private String uuid;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Shared part password")
    private String password;

    @ApiModelProperty(value = "Shared part expire date")
    @JsonbDateFormat(value = DateUtils.GLOBAL_DATE_FORMAT)
    private Date expireDate;

    @ApiModelProperty(value = "Shared part login creator")
    private String userLogin;

    @ApiModelProperty(value = "Shared part number")
    private String partMasterNumber;

    @ApiModelProperty(value = "Shared part version")
    private String partMasterVersion;

    public SharedPartDTO() {
    }

    public SharedPartDTO(String uuid, String workspaceId, String password, Date expireDate, String userLogin) {
        this.uuid = uuid;
        this.workspaceId = workspaceId;
        this.password = password;
        this.expireDate = expireDate;
        this.userLogin = userLogin;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPartMasterNumber() {
        return partMasterNumber;
    }

    public void setPartMasterNumber(String partMasterNumber) {
        this.partMasterNumber = partMasterNumber;
    }

    public String getPartMasterVersion() {
        return partMasterVersion;
    }

    public void setPartMasterVersion(String partMasterVersion) {
        this.partMasterVersion = partMasterVersion;
    }
}
