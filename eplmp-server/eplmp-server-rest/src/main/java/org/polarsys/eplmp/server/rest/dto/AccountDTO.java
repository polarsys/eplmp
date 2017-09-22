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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement
@ApiModel(value = "AccountDTO", description = "This class is the representation of an {@link org.polarsys.eplmp.core.common.Account} entity")
public class AccountDTO implements Serializable {

    @ApiModelProperty(value = "Login of the account")
    private String login;

    @ApiModelProperty(value = "Password of the account")
    private String password;

    @ApiModelProperty(value = "Name of the account")
    private String name;

    @ApiModelProperty(value = "Email of the account")
    private String email;

    @ApiModelProperty(value = "Language of the account")
    private String language;

    @ApiModelProperty(value = "Timezone of the account")
    private String timeZone;

    @ApiModelProperty(value = "Account administrator flag")
    private boolean admin;

    @ApiModelProperty(value = "Account enabled flag")
    private boolean enabled;

    @ApiModelProperty(value = "Password for password change or create action")
    private String newPassword;

    public AccountDTO() {
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getPassword() {
	return this.password;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getLanguage() {
	return language;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    public boolean isAdmin() {
	return admin;
    }

    public void setAdmin(boolean admin) {
	this.admin = admin;
    }

    public String getNewPassword() {
	return newPassword;
    }

    public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }
}
