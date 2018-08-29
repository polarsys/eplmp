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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value="PartUsageLinkDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.PartUsageLink} entity")
public class PartUsageLinkDTO implements Serializable {

    @ApiModelProperty(value = "Part usage link id")
    private int id;

    @ApiModelProperty(value = "Complete path in context")
    private String fullId;

    @ApiModelProperty(value = "Amount for usage")
    private double amount;

    @ApiModelProperty(value = "Link comment")
    private String comment;

    @ApiModelProperty(value = "Component used")
    private ComponentDTO component;

    @ApiModelProperty(value = "Link description")
    private String referenceDescription;

    @ApiModelProperty(value = "Unit for amount")
    private String unit;

    @ApiModelProperty(value = "Optional link flag")
    private boolean optional;

    @ApiModelProperty(value = "List of CAD instances")
    private List<CADInstanceDTO> cadInstances;

    @ApiModelProperty(value = "List of substitute links")
    private List<PartSubstituteLinkDTO> substitutes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ComponentDTO getComponent() {
        return component;
    }

    public void setComponent(ComponentDTO component) {
        this.component = component;
    }

    public String getReferenceDescription() {
        return referenceDescription;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<CADInstanceDTO> getCadInstances() {
        return cadInstances;
    }

    public void setCadInstances(List<CADInstanceDTO> cadInstances) {
        this.cadInstances = cadInstances;
    }

    public List<PartSubstituteLinkDTO> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<PartSubstituteLinkDTO> substitutes) {
        this.substitutes = substitutes;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
}
