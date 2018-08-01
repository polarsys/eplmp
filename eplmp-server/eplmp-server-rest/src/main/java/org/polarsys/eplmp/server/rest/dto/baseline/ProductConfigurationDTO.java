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

package org.polarsys.eplmp.server.rest.dto.baseline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.server.rest.dto.ACLDTO;
import org.polarsys.eplmp.server.rest.dto.LightPartLinkListDTO;
import org.polarsys.eplmp.server.rest.dto.UserDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="ProductConfigurationDTO", description="This class is the representation of {@link org.polarsys.eplmp.core.configuration.ProductConfiguration} entity")
public class ProductConfigurationDTO implements Serializable {

    @ApiModelProperty(value = "Product configuration id")
    @XmlElement(nillable = true)
    private int id;

    @ApiModelProperty(value = "Configuration item id")
    private String configurationItemId;

    @ApiModelProperty(value = "Product configuration name")
    private String name;

    @ApiModelProperty(value = "Product configuration description")
    private String description;

    @ApiModelProperty(value = "Product configuration substitute links list")
    private List<String> substituteLinks;

    @ApiModelProperty(value = "Product configuration optional links retained list")
    private List<String> optionalUsageLinks;

    @ApiModelProperty(value = "Product configuration creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Product configuration author")
    private UserDTO author;

    @ApiModelProperty(value = "Product configuration substitute links list")
    private List<LightPartLinkListDTO> substitutesParts;

    @ApiModelProperty(value = "Product configuration optional links retained list")
    private List<LightPartLinkListDTO> optionalsParts;

    @ApiModelProperty(value = "Product configuration ACL")
    private ACLDTO acl;

    public ProductConfigurationDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(String configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public List<LightPartLinkListDTO> getSubstitutesParts() {
        return substitutesParts;
    }

    public void setSubstitutesParts(List<LightPartLinkListDTO> substitutesParts) {
        this.substitutesParts = substitutesParts;
    }

    public List<LightPartLinkListDTO> getOptionalsParts() {
        return optionalsParts;
    }

    public void setOptionalsParts(List<LightPartLinkListDTO> optionalsParts) {
        this.optionalsParts = optionalsParts;
    }
}
