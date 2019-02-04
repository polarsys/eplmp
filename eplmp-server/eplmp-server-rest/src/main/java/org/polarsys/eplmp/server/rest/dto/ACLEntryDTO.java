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
import org.polarsys.eplmp.core.security.ACLPermission;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@ApiModel(value = "ACLEntryDTO", description = "This class holds permission data")
public class ACLEntryDTO implements Serializable {

    @ApiModelProperty(value = "Member id")
    private String key;

    @ApiModelProperty(value = "ACL permission value")
    private ACLPermission value;

    public ACLEntryDTO() {
    }

    public ACLEntryDTO(String key, ACLPermission value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ACLPermission getValue() {
        return value;
    }

    public void setValue(ACLPermission value) {
        this.value = value;
    }
}
