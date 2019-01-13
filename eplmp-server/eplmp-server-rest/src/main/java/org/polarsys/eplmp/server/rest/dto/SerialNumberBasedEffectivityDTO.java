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

@XmlRootElement
@ApiModel(value = "SerialNumberBasedEffectivityDTO",
        description = "This class is the representation of a {@link org.polarsys.eplmp.core.common.SerialNumberBasedEffectivity} entity",
        parent = EffectivityDTO.class)
public class SerialNumberBasedEffectivityDTO extends EffectivityDTO implements Serializable {

    @ApiModelProperty(value = "Start number of the Serial Number")
    private String startNumber;

    @ApiModelProperty(value = "End number of the Serial Number")
    private String endNumber;

    public SerialNumberBasedEffectivityDTO() {
    }

    public String getStartNumber() { return startNumber; }

    public void setStartNumber(String startNumber) { this.startNumber = startNumber; }

    public String getEndNumber() { return endNumber; }

    public void setEndNumber(String endNumber) { this.endNumber = endNumber; }
}
