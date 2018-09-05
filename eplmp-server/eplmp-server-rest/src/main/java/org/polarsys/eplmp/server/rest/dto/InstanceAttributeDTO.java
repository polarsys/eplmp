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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.core.meta.InstanceAttribute;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Yassine Belouad
 */
@XmlRootElement
@ApiModel(value="InstanceAttributeDTO", description="This class is the representation of an {@link org.polarsys.eplmp.core.meta.InstanceAttribute} entity")
public class InstanceAttributeDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Instance attribute name")
    private String name;

    @ApiModelProperty(value = "Mandatory flag")
    private boolean mandatory;

    @ApiModelProperty(value = "Locked flag")
    private boolean locked;

    @ApiModelProperty(value = "Instance attribute type")
    private InstanceAttributeType type;

    @ApiModelProperty(value = "Instance attribute value")
    private String value;

    @ApiModelProperty(value = "Instance attribute LOV name")
    private String lovName;

    @ApiModelProperty(value = "Instance attribute LOV items")
    private List<NameValuePairDTO> items;

    public InstanceAttributeDTO() {

    }

    public InstanceAttributeDTO(String pName, InstanceAttributeType pType, String pValue, Boolean pMandatory, Boolean pLocked) {
        this.name = pName;
        this.type = pType;
        this.value = pValue;
        this.mandatory = pMandatory;
        this.locked = pLocked;
    }

    public InstanceAttributeDTO(String pName, String pType, String pValue, Boolean pMandatory, Boolean pLocked) {
        this(pName, InstanceAttributeType.valueOf(pType), pValue, pMandatory, pLocked);
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

    public InstanceAttributeType getType() {
        return type;
    }

    public void setType(InstanceAttributeType type) {
        this.type = type;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getLovName() {
        return lovName;
    }

    public void setLovName(String lovName) {
        this.lovName = lovName;
    }

    public List<NameValuePairDTO> getItems() {
        return items;
    }

    public void setItems(List<NameValuePairDTO> items) {
        this.items = items;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

}
