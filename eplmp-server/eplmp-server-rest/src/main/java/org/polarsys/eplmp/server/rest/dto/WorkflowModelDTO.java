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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="WorkflowModelDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.workflow.WorkflowModel} entity")
public class WorkflowModelDTO implements Serializable {

    @ApiModelProperty(value = "Workflow model id")
    private String id;

    @ApiModelProperty(value = "Workflow model reference")
    private String reference;

    @ApiModelProperty(value = "Workflow model final lifecycle state")
    private String finalLifeCycleState;

    @ApiModelProperty(value = "Workflow model author")
    private UserDTO author;

    @ApiModelProperty(value = "Workflow model creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Workflow model ACL")
    private ACLDTO acl;

    @ApiModelProperty(value = "Workflow model activity model list")
    @XmlElement(nillable = false, required = true)
    private List<ActivityModelDTO> activityModels;

    public WorkflowModelDTO() {
        activityModels = new ArrayList<>();
    }

    public WorkflowModelDTO(String id) {
        this.id = id;
    }

    public WorkflowModelDTO(String id, String reference, String finalLifeCycleState, UserDTO author, Date creationDate, List<ActivityModelDTO> activityModels) {
        this.id = id;
        this.reference = reference;
        this.finalLifeCycleState = finalLifeCycleState;
        this.author = author;
        this.creationDate = creationDate;
        this.activityModels = activityModels;

    }

    public WorkflowModelDTO(String id, UserDTO pAuthor) {
        this.id = id;
        this.reference = id;
        this.author = pAuthor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addActivity(ActivityModelDTO activity) {
        this.activityModels.add(activity);
    }

    public void removeActivity(ActivityModelDTO activity) {
        this.activityModels.remove(activity);
    }

    public List<ActivityModelDTO> getActivityModels() {
        return this.activityModels;
    }

    public void setActivityModels(List<ActivityModelDTO> activityModels) {
        this.activityModels = activityModels;
    }

    public String getFinalLifeCycleState() {
        return finalLifeCycleState;
    }

    public void setFinalLifeCycleState(String finalLifeCycleState) {
        this.finalLifeCycleState = finalLifeCycleState;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ACLDTO getAcl() {
        return acl;
    }

    public void setAcl(ACLDTO acl) {
        this.acl = acl;
    }
}
