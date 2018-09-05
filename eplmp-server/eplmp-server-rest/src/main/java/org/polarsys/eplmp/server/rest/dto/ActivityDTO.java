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
import java.util.List;

@XmlRootElement
@ApiModel(value="ActivityDTO", description="This class is the representation of an {@link org.polarsys.eplmp.core.workflow.Activity} entity")
public class ActivityDTO implements Serializable {

    @ApiModelProperty(value = "Activity step")
    private int step;

    @ApiModelProperty(value = "Activity relaunch step")
    private Integer relaunchStep;

    @ApiModelProperty(value = "List of the tasks")
    private List<TaskDTO> tasks;

    @ApiModelProperty(value = "Final lifecycle state")
    private String lifeCycleState;

    @ApiModelProperty(value = "Workflow type")
    private ActivityType type;

    @ApiModelProperty(value = "Tasks to complete")
    private Integer tasksToComplete;

    @ApiModelProperty(value = "Complete flag")
    private boolean complete;

    @ApiModelProperty(value = "Stopped flag")
    private boolean stopped;

    @ApiModelProperty(value = "In progress flag")
    private boolean inProgress;

    @ApiModelProperty(value = "Todo flag")
    private boolean toDo;

    public ActivityDTO() {
        tasks = new ArrayList<>();
    }

    public ActivityDTO(int step, List<TaskDTO> tasks, String lifeCycleState, ActivityType type, Integer tasksToComplete, boolean complete, boolean stopped, boolean inProgress, boolean toDo, Integer relaunchStep) {
        this.step = step;
        this.relaunchStep = relaunchStep;
        this.tasks = tasks;
        this.lifeCycleState = lifeCycleState;
        this.type = type;
        this.tasksToComplete = tasksToComplete;
        this.complete = complete;
        this.stopped = stopped;
        this.inProgress = inProgress;
        this.toDo = toDo;
    }

    public Integer getTasksToComplete() {
        return tasksToComplete;
    }

    public void setTasksToComplete(Integer tasksToComplete) {
        this.tasksToComplete = tasksToComplete;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isToDo() {
        return toDo;
    }

    public void setToDo(boolean toDo) {
        this.toDo = toDo;
    }

    public String getLifeCycleState() {
        return lifeCycleState;
    }

    public void setLifeCycleState(String lifeCycleState) {
        this.lifeCycleState = lifeCycleState;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getRelaunchStep() {
        return relaunchStep;
    }

    public void setRelaunchStep(Integer relaunchStep) {
        this.relaunchStep = relaunchStep;
    }

}
