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

/**
 * @author Florent Garin
 */
@XmlRootElement
@ApiModel(value="ConfigurationItemDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.product.ConfigurationItem} entity")
public class ConfigurationItemDTO implements Serializable {

    @ApiModelProperty(value = "Configuration item id")
    private String id;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Configuration item description")
    private String description;

    @ApiModelProperty(value = "Configuration item root part number")
    private String designItemNumber;

    @ApiModelProperty(value = "Configuration item root part name")
    private String designItemName;

    @ApiModelProperty(value = "Configuration item root part latest version")
    private String designItemLatestVersion;

    @ApiModelProperty(value = "Configuration item author")
    private UserDTO author;

    @ApiModelProperty(value = "Hooked modification notifications")
    private boolean hasModificationNotification;

    @ApiModelProperty(value = "List of structure path links")
    private List<PathToPathLinkDTO> pathToPathLinks;


    public ConfigurationItemDTO() {
    }

    public ConfigurationItemDTO(UserDTO author, String id, String workspaceId, String description, String designItemNumber,
                                String designItemName, String designItemLatestVersion) {
        this.id = id;
        this.author = author;
        this.workspaceId = workspaceId;
        this.description = description;
        this.designItemNumber = designItemNumber;
        this.designItemName = designItemName;
        this.designItemLatestVersion = designItemLatestVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getDesignItemNumber() {
        return designItemNumber;
    }

    public void setDesignItemNumber(String designItemNumber) {
        this.designItemNumber = designItemNumber;
    }

    public String getDesignItemName() {
        return designItemName;
    }

    public void setDesignItemName(String designItemName) {
        this.designItemName = designItemName;
    }

    public String getDesignItemLatestVersion() {
        return designItemLatestVersion;
    }

    public void setDesignItemLatestVersion(String designItemLatestVersion) {
        this.designItemLatestVersion = designItemLatestVersion;
    }

    public List<PathToPathLinkDTO> getPathToPathLinks() {
        return pathToPathLinks;
    }

    public void setPathToPathLinks(List<PathToPathLinkDTO> pathToPathLinks) {
        this.pathToPathLinks = pathToPathLinks;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public boolean isHasModificationNotification() {
        return hasModificationNotification;
    }

    public void setHasModificationNotification(boolean hasModificationNotification) {
        this.hasModificationNotification = hasModificationNotification;
    }
}
