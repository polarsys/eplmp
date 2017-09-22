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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="TaskDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.workflow.Task} entity")
public class TaskDTO implements Serializable {

    @ApiModelProperty(value = "Task closure comment")
    private String closureComment;

    @ApiModelProperty(value = "Task title")
    private String title;

    @ApiModelProperty(value = "Task instructions")
    private String instructions;

    @ApiModelProperty(value = "Task entity target iteration")
    private int targetIteration;

    @ApiModelProperty(value = "Task closure date")
    private Date closureDate;

    @ApiModelProperty(value = "Task signature")
    private String signature;

    @ApiModelProperty(value = "Task assigned users")
    private List<UserDTO> assignedUsers = new ArrayList<>();

    @ApiModelProperty(value = "Task assigned groups")
    private List<UserGroupDTO> assignedGroups = new ArrayList<>();

    @ApiModelProperty(value = "Task effective worker")
    private UserDTO worker;

    @ApiModelProperty(value = "Task status")
    private Status status;

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Task parent workflow")
    private int workflowId;

    @ApiModelProperty(value = "Task parent activity step")
    private int activityStep;

    @ApiModelProperty(value = "Task num")
    private int num;

    @ApiModelProperty(value = "Task holder type")
    private String holderType;

    @ApiModelProperty(value = "Task holder reference")
    private String holderReference;

    @ApiModelProperty(value = "Task holder version")
    private String holderVersion;

    public TaskDTO() {
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getClosureComment() {
        return closureComment;
    }

    public void setClosureComment(String closureComment) {
        this.closureComment = closureComment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getTargetIteration() {
        return targetIteration;
    }

    public void setTargetIteration(int targetIteration) {
        this.targetIteration = targetIteration;
    }

    public Date getClosureDate() {
        return (closureDate != null) ? (Date) closureDate.clone() : null;
    }

    public void setClosureDate(Date date) {
        this.closureDate = (date != null) ? (Date) date.clone() : null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserDTO getWorker() {
        return worker;
    }

    public void setWorker(UserDTO worker) {
        this.worker = worker;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public int getActivityStep() {
        return activityStep;
    }

    public void setActivityStep(int activityStep) {
        this.activityStep = activityStep;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getHolderType() {
        return holderType;
    }

    public void setHolderType(String holderType) {
        this.holderType = holderType;
    }

    public String getHolderReference() {
        return holderReference;
    }

    public void setHolderReference(String holderReference) {
        this.holderReference = holderReference;
    }

    public String getHolderVersion() {
        return holderVersion;
    }

    public void setHolderVersion(String holderVersion) {
        this.holderVersion = holderVersion;
    }

    public List<UserDTO> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UserDTO> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public List<UserGroupDTO> getAssignedGroups() {
        return assignedGroups;
    }

    public void setAssignedGroups(List<UserGroupDTO> assignedGroups) {
        this.assignedGroups = assignedGroups;
    }

    public enum Status {
        NOT_STARTED, IN_PROGRESS, APPROVED, REJECTED, NOT_TO_BE_DONE
    }
}
