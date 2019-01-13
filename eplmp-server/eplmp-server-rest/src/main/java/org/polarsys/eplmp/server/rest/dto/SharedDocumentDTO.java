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
import java.util.Date;

/**
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value="SharedDocumentDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.sharing.SharedDocument} entity")
public class SharedDocumentDTO implements Serializable {

    @ApiModelProperty(value = "Shared document token")
    private String uuid;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Shared document password")
    private String password;

    @ApiModelProperty(value = "Shared document expired date")
    private Date expireDate;

    @ApiModelProperty(value = "Shared document creator")
    private String userLogin;

    @ApiModelProperty(value = "Shared document maser id")
    private String documentMasterId;

    @ApiModelProperty(value = "Shared document version")
    private String documentMasterVersion;

    public SharedDocumentDTO() {

    }

    public SharedDocumentDTO(String uuid, String workspaceId, String password, Date expireDate, String userLogin) {
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

    public String getDocumentMasterId() {
        return documentMasterId;
    }

    public void setDocumentMasterId(String documentMasterId) {
        this.documentMasterId = documentMasterId;
    }

    public String getDocumentMasterVersion() {
        return documentMasterVersion;
    }

    public void setDocumentMasterVersion(String documentMasterVersion) {
        this.documentMasterVersion = documentMasterVersion;
    }
}
