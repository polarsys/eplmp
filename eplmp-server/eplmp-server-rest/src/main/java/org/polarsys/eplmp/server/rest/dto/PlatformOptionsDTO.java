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

import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@ApiModel(value="PlatformOptionsDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.admin.PlatformOptions} entity")
public class PlatformOptionsDTO implements Serializable {

    @ApiModelProperty(value = "Registration strategy")
    private OperationSecurityStrategy registrationStrategy;

    @ApiModelProperty(value = "Workspace creation strategy")
    private OperationSecurityStrategy workspaceCreationStrategy;

    public PlatformOptionsDTO() {
    }

    public OperationSecurityStrategy getRegistrationStrategy() {
        return registrationStrategy;
    }

    public void setRegistrationStrategy(OperationSecurityStrategy registrationStrategy) {
        this.registrationStrategy = registrationStrategy;
    }

    public OperationSecurityStrategy getWorkspaceCreationStrategy() {
        return workspaceCreationStrategy;
    }

    public void setWorkspaceCreationStrategy(OperationSecurityStrategy workspaceCreationStrategy) {
        this.workspaceCreationStrategy = workspaceCreationStrategy;
    }
}
