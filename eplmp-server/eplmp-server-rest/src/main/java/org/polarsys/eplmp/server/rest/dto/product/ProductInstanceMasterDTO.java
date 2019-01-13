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

package org.polarsys.eplmp.server.rest.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.server.rest.dto.ACLDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@ApiModel(value="ProductInstanceMasterDTO", description="This class is the representation of {@link org.polarsys.eplmp.core.configuration.ProductInstanceMaster} entity")
public class ProductInstanceMasterDTO implements Serializable {

    @ApiModelProperty(value = "Product instance identifier")
    private String identifier;

    @ApiModelProperty(value = "Product instance serial number")
    private String serialNumber;

    @ApiModelProperty(value = "Configuration item used")
    private String configurationItemId;

    @ApiModelProperty(value = "Product instance iterations")
    private List<ProductInstanceIterationDTO> productInstanceIterations;

    @ApiModelProperty(value = "Product instance ACL")
    private ACLDTO acl;

    public ProductInstanceMasterDTO() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(String configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

    public List<ProductInstanceIterationDTO> getProductInstanceIterations() {
        return productInstanceIterations;
    }

    public void setProductInstanceIterations(List<ProductInstanceIterationDTO> productInstanceIterations) {
        this.productInstanceIterations = productInstanceIterations;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }

}
