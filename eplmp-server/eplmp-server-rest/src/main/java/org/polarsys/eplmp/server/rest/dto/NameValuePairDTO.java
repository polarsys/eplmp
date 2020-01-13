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

/**
 * @author lebeaujulien on 03/03/15.
 */

@ApiModel(value="NameValuePairDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.meta.NameValuePair} entity")
public class NameValuePairDTO implements Serializable {

    @ApiModelProperty(value = "Pair name")
    private String name;

    @ApiModelProperty(value = "Pair value")
    private String value;

    public NameValuePairDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
