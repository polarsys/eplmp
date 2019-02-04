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

package org.polarsys.eplmp.server.rest.dto.baseline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="ProductBaselineCreationDTO", description="DTO creation class for {@link org.polarsys.eplmp.core.configuration.ProductBaseline} entity")
public class ProductBaselineCreationDTO implements Serializable {

    @ApiModelProperty(value = "Baseline name")
    private String name;

    @ApiModelProperty(value = "Baseline description")
    private String description;

    @ApiModelProperty(value = "Configuration item in use")
    private String configurationItemId;

    @ApiModelProperty(value = "Baseline type")
    private ProductBaselineType type;

    @ApiModelProperty(value = "Baselined part list")
    private List<BaselinedPartDTO> baselinedParts;

    @ApiModelProperty(value = "Baseline substitute links used, as id list")
    private List<String> substituteLinks;

    @ApiModelProperty(value = "Baseline optional links retained, as id list")
    private List<String> optionalUsageLinks;

    @ApiModelProperty(value = "Effective date")
    private Date effectiveDate;

    @ApiModelProperty(value = "Effective serial number")
    private String effectiveSerialNumber;

    @ApiModelProperty(value = "Effective lot id")
    private String effectiveLotId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(String configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

    public ProductBaselineType getType() {
        return type;
    }

    public void setType(ProductBaselineType type) {
        this.type = type;
    }

    public List<BaselinedPartDTO> getBaselinedParts() {
        return baselinedParts;
    }

    public void setBaselinedParts(List<BaselinedPartDTO> baselinedParts) {
        this.baselinedParts = baselinedParts;
    }

    public List<String> getSubstituteLinks() {
        return substituteLinks;
    }

    public void setSubstituteLinks(List<String> substituteLinks) {
        this.substituteLinks = substituteLinks;
    }

    public List<String> getOptionalUsageLinks() {
        return optionalUsageLinks;
    }

    public void setOptionalUsageLinks(List<String> optionalUsageLinks) {
        this.optionalUsageLinks = optionalUsageLinks;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveSerialNumber() {
        return effectiveSerialNumber;
    }

    public void setEffectiveSerialNumber(String effectiveSerialNumber) {
        this.effectiveSerialNumber = effectiveSerialNumber;
    }

    public String getEffectiveLotId() {
        return effectiveLotId;
    }

    public void setEffectiveLotId(String effectiveLotId) {
        this.effectiveLotId = effectiveLotId;
    }
}
