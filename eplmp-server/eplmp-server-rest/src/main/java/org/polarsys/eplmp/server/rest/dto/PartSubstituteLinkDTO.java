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
import java.util.List;

@XmlRootElement
@ApiModel(value="PartSubstituteLinkDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.product.PartSubstituteLink} entity")
public class PartSubstituteLinkDTO implements Serializable {

    @ApiModelProperty(value = "Part substitute link id")
    private int id;

    @ApiModelProperty(value = "Complete path in context")
    private String fullId;

    @ApiModelProperty(value = "Amount of component")
    private double amount;

    @ApiModelProperty(value = "Unit for amount")
    private String unit;

    @ApiModelProperty(value = "Link description")
    private String referenceDescription;

    @ApiModelProperty(value = "Link comment")
    private String comment;

    @ApiModelProperty(value = "Substitute component")
    private ComponentDTO substitute;

    @ApiModelProperty(value = "Component CAD instances")
    private List<CADInstanceDTO> cadInstances;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReferenceDescription() {
        return referenceDescription;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ComponentDTO getSubstitute() {
        return substitute;
    }

    public void setSubstitute(ComponentDTO substitute) {
        this.substitute = substitute;
    }

    public List<CADInstanceDTO> getCadInstances() {
        return cadInstances;
    }

    public void setCadInstances(List<CADInstanceDTO> cadInstances) {
        this.cadInstances = cadInstances;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
}
