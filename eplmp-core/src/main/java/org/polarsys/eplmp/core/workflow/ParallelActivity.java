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

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * ParallelActivity is a kind of activity where
 * all its tasks start at the same time as the activity itself.
 * Thus, there is no order between the executions of tasks.
 * The <code>tasksToComplete</code> attribute specifies the number of tasks that
 * should be completed so the workflow can progress to the next step. 
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="PARALLELACTIVITY")
@Entity
public class ParallelActivity extends Activity {

    private int tasksToComplete;
    
    public ParallelActivity() {

    }

    public ParallelActivity(int pStep, String pLifeCycleState, int pTasksToComplete) {
        super(pStep, pLifeCycleState);
        tasksToComplete=pTasksToComplete;
    }

    @Override
    public boolean isStopped() {
        return tasks.size() - numberOfRejected() < tasksToComplete;
    }

    private int numberOfApproved(){
        int approved=0;
        for(Task task:tasks){
            if(task.isApproved() || task.isNotToBeDone()) {
                approved++;
            }
        }
        return approved;
    }
    
    private int numberOfRejected(){
        int rejected=0;
        for(Task task:tasks){
            if(task.isRejected()) {
                rejected++;
            }
        }
        return rejected;
    }
    
    @Override
    public Collection<Task> getOpenTasks() {
        Set<Task> runningTasks = new HashSet<>();
        if (!isComplete() && !isStopped()) {           
            for (Task task : tasks) {
                if(task.isInProgress() || task.isNotStarted()) {
                    runningTasks.add(task);
                }
            }    
        }
        return runningTasks;
    }

    public void setTasksToComplete(int tasksToComplete) {
        this.tasksToComplete = tasksToComplete;
    }


    public int getTasksToComplete() {
        return tasksToComplete;
    }

    @Override
    public boolean isComplete() {
        return numberOfApproved() >= tasksToComplete;
    }

    @Override
    public void relaunch(){
        for(Task t : tasks){
            t.start();
        }
    }
}
