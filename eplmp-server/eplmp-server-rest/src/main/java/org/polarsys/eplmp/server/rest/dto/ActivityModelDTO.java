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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@ApiModel(value="ActivityModelDTO", description="This class is the representation of an {@link org.polarsys.eplmp.core.workflow.ActivityModel} entity")
public class ActivityModelDTO implements Serializable {

    @ApiModelProperty(value = "Activity model step")
    private int step;

    @ApiModelProperty(value = "Activity model relaunch step")
    private Integer relaunchStep;

    @XmlElement(nillable = false, required = true)
    @ApiModelProperty(value = "List of task models")
    private List<TaskModelDTO> taskModels;

    @ApiModelProperty(value = "Final lifecycle state")
    private String lifeCycleState;

    @ApiModelProperty(value = "Type of the workflow model")
    private Type type;

    @ApiModelProperty(value = "Amount of tasks to complete")
    private Integer tasksToComplete;

    public ActivityModelDTO() {
        this.taskModels = new ArrayList<>();
    }

    public ActivityModelDTO(int step, List<TaskModelDTO> taskModels, String lifeCycleState, Type type, Integer tasksToComplete, Integer relaunchStep) {
        this.step = step;
        this.relaunchStep = relaunchStep;
        this.taskModels = taskModels;
        this.lifeCycleState = lifeCycleState;
        this.type = type;
        this.tasksToComplete = tasksToComplete;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void addTaskModel(TaskModelDTO m) {
        this.taskModels.add(m);
    }

    public void removeTaskModel(TaskModelDTO m) {
        this.taskModels.remove(m);
    }

    public List<TaskModelDTO> getTaskModels() {
        return this.taskModels;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getTasksToComplete() {
        return tasksToComplete;
    }

    public void setTasksToComplete(Integer tasksToComplete) {
        this.tasksToComplete = tasksToComplete;
    }

    public String getLifeCycleState() {
        return lifeCycleState;
    }

    public void setLifeCycleState(String lifeCycleState) {
        this.lifeCycleState = lifeCycleState;
    }

    public Integer getRelaunchStep() {
        return relaunchStep;
    }

    public void setRelaunchStep(Integer relaunchStep) {
        this.relaunchStep = relaunchStep;
    }

    public enum Type {
        SEQUENTIAL, PARALLEL
    }
}
