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
import org.polarsys.eplmp.core.util.DateUtils;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ApiModel(value = "WorkflowDTO", description = "This class is a representation of a {@link org.polarsys.eplmp.core.workflow.Workflow} entity")
public class WorkflowDTO implements Serializable, Comparable<WorkflowDTO> {

    @ApiModelProperty(value = "Workflow id")
    private int id;

    @ApiModelProperty(value = "Workflow final lifecycle state")
    private String finalLifeCycleState;

    @ApiModelProperty(value = "Workflow activity list")
    private List<ActivityDTO> activities;

    @ApiModelProperty(value = "Workflow aborted date if aborted")
    @JsonbDateFormat(value = DateUtils.GLOBAL_DATE_FORMAT)
    private Date abortedDate;

    public WorkflowDTO() {
        activities = new ArrayList<>();
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFinalLifeCycleState() {
        return finalLifeCycleState;
    }

    public void setFinalLifeCycleState(String finalLifeCycleState) {
        this.finalLifeCycleState = finalLifeCycleState;
    }

    public String getLifeCycleState() {
        ActivityDTO current = getCurrentActivity();
        return (current == null) ? finalLifeCycleState : current.getLifeCycleState();
    }

    public Date getAbortedDate() {
        return (abortedDate != null) ? (Date) abortedDate.clone() : null;
    }

    public void setAbortedDate(Date abortedDate) {
        this.abortedDate = (abortedDate != null) ? (Date) abortedDate.clone() : null;
    }

    public ActivityDTO getCurrentActivity() {
        if (getCurrentStep() < activities.size()) {
            return activities.get(getCurrentStep());
        } else {
            return null;
        }
    }

    public int getCurrentStep() {
        int i = 0;
        for (ActivityDTO activity : activities) {
            if (activity.isComplete()) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    @Override
    public int compareTo(WorkflowDTO o) {
        return o.getAbortedDate().compareTo(this.getAbortedDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowDTO that = (WorkflowDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (finalLifeCycleState != null ? finalLifeCycleState.hashCode() : 0);
        result = 31 * result + (activities != null ? activities.hashCode() : 0);
        result = 31 * result + (abortedDate != null ? abortedDate.hashCode() : 0);
        return result;
    }
}
