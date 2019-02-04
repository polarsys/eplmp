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
@ApiModel(value="InstanceAttributeTemplateDTO", description="This class is the representation of an {@link org.polarsys.eplmp.core.meta.InstanceAttributeTemplate} entity")
public class InstanceAttributeTemplateDTO implements Serializable {

    @ApiModelProperty(value = "Attribute template name")
    private String name;

    @ApiModelProperty(value = "Mandatory flag")
    private boolean mandatory;

    @ApiModelProperty(value = "Attribute template type")
    private InstanceAttributeType attributeType;

    @ApiModelProperty(value = "Attribute template LOV name")
    private String lovName;

    @ApiModelProperty(value = "Locked flag")
    private boolean locked;

    public InstanceAttributeTemplateDTO() {
    }

    public InstanceAttributeTemplateDTO(String pName, InstanceAttributeType pAttributeType, boolean pMandatory, boolean locked) {
        name = pName;
        attributeType = pAttributeType;
        mandatory = pMandatory;
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public InstanceAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(InstanceAttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getLovName() {
        return lovName;
    }

    public void setLovName(String lovName) {
        this.lovName = lovName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof InstanceAttributeTemplateDTO)) {
            return false;
        }
        InstanceAttributeTemplateDTO attr = (InstanceAttributeTemplateDTO) pObj;
        return attr.name.equals(name);
    }

    @Override
    public String toString() {
        return name + "-" + attributeType;
    }


}
