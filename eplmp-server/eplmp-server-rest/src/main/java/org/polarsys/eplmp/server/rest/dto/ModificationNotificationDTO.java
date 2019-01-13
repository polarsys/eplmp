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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@ApiModel(value="ModificationNotificationDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.change.ModificationNotification} entity")
public class ModificationNotificationDTO implements Serializable {

    @ApiModelProperty(value = "Modification notification id")
    private int id;

    @ApiModelProperty(value = "Impacted part number")
    private String impactedPartNumber;

    @ApiModelProperty(value = "Impacted part version")
    private String impactedPartVersion;

    @ApiModelProperty(value = "Modified part number")
    private String modifiedPartNumber;

    @ApiModelProperty(value = "Modified part name")
    private String modifiedPartName;

    @ApiModelProperty(value = "Modified part version")
    private String modifiedPartVersion;

    @ApiModelProperty(value = "Modified part iteration")
    private int modifiedPartIteration;

    @ApiModelProperty(value = "Modified part check in date")
    private Date checkInDate;

    @ApiModelProperty(value = "Modified part iteration note")
    private String iterationNote;

    @ApiModelProperty(value = "Modifications author")
    private UserDTO author;

    @ApiModelProperty(value = "Acknowledged flag")
    @XmlElement(nillable = true)
    private boolean acknowledged;

    @ApiModelProperty(value = "Acknowledged comment")
    private String ackComment;

    @ApiModelProperty(value = "Acknowledged author")
    private UserDTO ackAuthor;

    @ApiModelProperty(value = "Acknowledged date")
    private Date ackDate;

    public ModificationNotificationDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImpactedPartNumber() {
        return impactedPartNumber;
    }

    public void setImpactedPartNumber(String impactedPartNumber) {
        this.impactedPartNumber = impactedPartNumber;
    }

    public String getImpactedPartVersion() {
        return impactedPartVersion;
    }

    public void setImpactedPartVersion(String impactedPartVersion) {
        this.impactedPartVersion = impactedPartVersion;
    }

    public String getModifiedPartNumber() {
        return modifiedPartNumber;
    }

    public void setModifiedPartNumber(String modifiedPartNumber) {
        this.modifiedPartNumber = modifiedPartNumber;
    }

    public String getModifiedPartVersion() {
        return modifiedPartVersion;
    }

    public void setModifiedPartVersion(String modifiedPartVersion) {
        this.modifiedPartVersion = modifiedPartVersion;
    }

    public int getModifiedPartIteration() {
        return modifiedPartIteration;
    }

    public void setModifiedPartIteration(int modifiedPartIteration) {
        this.modifiedPartIteration = modifiedPartIteration;
    }

    public String getModifiedPartName() {
        return modifiedPartName;
    }

    public void setModifiedPartName(String modifiedPartName) {
        this.modifiedPartName = modifiedPartName;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getIterationNote() {
        return iterationNote;
    }

    public void setIterationNote(String iterationNote) {
        this.iterationNote = iterationNote;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getAckComment() {
        return ackComment;
    }

    public void setAckComment(String ackComment) {
        this.ackComment = ackComment;
    }

    public UserDTO getAckAuthor() {
        return ackAuthor;
    }

    public void setAckAuthor(UserDTO ackAuthor) {
        this.ackAuthor = ackAuthor;
    }

    public Date getAckDate() {
        return ackDate;
    }

    public void setAckDate(Date ackDate) {
        this.ackDate = ackDate;
    }

}
