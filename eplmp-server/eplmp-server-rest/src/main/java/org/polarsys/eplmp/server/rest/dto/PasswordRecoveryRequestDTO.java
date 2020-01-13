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

import java.io.Serializable;


@ApiModel(value="PasswordRecoveryRequestDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.security.PasswordRecoveryRequest} entity")
public class PasswordRecoveryRequestDTO implements Serializable {

    @ApiModelProperty(value = "Account login for password recovery")
    private String login;

    public PasswordRecoveryRequestDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
