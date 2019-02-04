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
import org.polarsys.eplmp.server.rest.dto.BinaryResourceDTO;
import org.polarsys.eplmp.server.rest.dto.DocumentRevisionDTO;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement
@ApiModel(value = "ProductInstanceCreationDTO", description = "Use this class to create a new {@link org.polarsys.eplmp.core.configuration.ProductInstanceMaster} entity")
public class ProductInstanceCreationDTO implements Serializable {

    @ApiModelProperty(value = "Product instance serial number")
    private String serialNumber;

    @ApiModelProperty(value = "Configuration item in use")
    private String configurationItemId;

    @ApiModelProperty(value = "Baseline in use")
    private int baselineId;

    @ApiModelProperty(value = "Product instance ACL")
    private ACLDTO acl;

    @ApiModelProperty(value = "Product instance attributes")
    private List<InstanceAttributeDTO> instanceAttributes = new ArrayList<>();

    @ApiModelProperty(value = "Product instance linked documents")
    private Set<DocumentRevisionDTO> linkedDocuments = new HashSet<>();

    @ApiModelProperty(value = "Product instance attached files")
    private List<BinaryResourceDTO> attachedFiles;

    public ProductInstanceCreationDTO() {
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

    public int getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(int baselineId) {
        this.baselineId = baselineId;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }

    public List<InstanceAttributeDTO> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(List<InstanceAttributeDTO> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }

    public Set<DocumentRevisionDTO> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(Set<DocumentRevisionDTO> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public List<BinaryResourceDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<BinaryResourceDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }
}
