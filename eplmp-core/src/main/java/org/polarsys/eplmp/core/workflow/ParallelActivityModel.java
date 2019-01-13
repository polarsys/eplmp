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

package org.polarsys.eplmp.core.workflow;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is the model used to create instances
 * of {@link ParallelActivity} attached to workflows.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="PARALLELACTIVITYMODEL")
@Entity
public class ParallelActivityModel extends ActivityModel {

        
    private int tasksToComplete;

    public ParallelActivityModel() {
    }
    
    public ParallelActivityModel(WorkflowModel pWorkflowModel, int pStep, List<TaskModel> pTaskModels, String pLifeCycleState, int pTasksToComplete) {
        super(pWorkflowModel, pStep, pTaskModels, pLifeCycleState);
        tasksToComplete=pTasksToComplete;       
    }
    
    public ParallelActivityModel(WorkflowModel pWorkflowModel, String pLifeCycleState, int pTasksToComplete) {
        this(pWorkflowModel, 0,  new LinkedList<>(), pLifeCycleState,pTasksToComplete);
    }

 
    public int getTasksToComplete() {
        return tasksToComplete;
    }

    public void setTasksToComplete(int pTasksToComplete) {
        if (pTasksToComplete < this.getTaskModels().size()) {
            tasksToComplete = pTasksToComplete;
        } else {
            tasksToComplete = this.getTaskModels().size();
        }
    }

    @Override
    public void removeTaskModel(TaskModel pTaskModel) {
        super.removeTaskModel(pTaskModel);
        if (tasksToComplete > taskModels.size()) {
            tasksToComplete--;
        }
    }

    @Override
    public Activity createActivity(Map<Role,Collection<User>> roleUserMap, Map<Role,Collection<UserGroup>> roleGroupMap) {
        Activity activity = new ParallelActivity(step, lifeCycleState, tasksToComplete);
        List<Task> tasks = activity.getTasks();
        for(TaskModel model:taskModels){
            Task task = model.createTask(roleUserMap, roleGroupMap);
            task.setActivity(activity);
            tasks.add(task);
        }
        return activity;
    }

    @Override
    public String toString() {
        return taskModels + " (" + getTasksToComplete() + "/" + taskModels.size() + ")";
    }
    
    @Override
    public ParallelActivityModel clone() {
        return (ParallelActivityModel) super.clone();
    }
}
