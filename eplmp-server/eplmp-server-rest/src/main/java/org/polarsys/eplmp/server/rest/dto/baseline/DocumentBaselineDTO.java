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
import org.polarsys.eplmp.core.configuration.DocumentBaselineType;
import org.polarsys.eplmp.server.rest.dto.UserDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="DocumentBaselineDTO", description="This class is the representation of {@link org.polarsys.eplmp.core.configuration.DocumentBaseline} entity")
public class DocumentBaselineDTO implements Serializable {

    @ApiModelProperty(value = "Baseline id")
    private int id;

    @ApiModelProperty(value = "Baseline name")
    private String name;

    @ApiModelProperty(value = "Baseline description")
    private String description;

    @ApiModelProperty(value = "Baseline creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Baseline type")
    private DocumentBaselineType type;

    @ApiModelProperty(value = "Baselined document list")
    private List<BaselinedDocumentDTO> baselinedDocuments;

    @ApiModelProperty(value = "Baseline author")
    private UserDTO author;

    public DocumentBaselineDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public DocumentBaselineType getType() {
        return type;
    }

    public void setType(DocumentBaselineType type) {
        this.type = type;
    }

    public List<BaselinedDocumentDTO> getBaselinedDocuments() {
        return baselinedDocuments;
    }

    public void setBaselinedDocuments(List<BaselinedDocumentDTO> baselinedDocuments) {
        this.baselinedDocuments = baselinedDocuments;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
