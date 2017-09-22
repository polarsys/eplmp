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

package org.polarsys.eplmp.core.workflow;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SequentialActivity is an activity where
 * all tasks are launched subsequently in a specific order.
 * For the workflow to proceed to the next step, all tasks of 
 * SequentialActivity should have been completed.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="SEQUENTIALACTIVITY")
@Entity
public class SequentialActivity extends Activity {
    public SequentialActivity() {

    }

    public SequentialActivity(int pStep, String pLifeCycleState) {
        super(pStep, pLifeCycleState);
    }
    
    @Override
    public boolean isStopped() {
        for(Task task:tasks)
            if(task.isRejected()) {
                return true;
            }
        
        return false;
    }

    @Override
    public Collection<Task> getOpenTasks() {
        List<Task> runningTasks = new ArrayList<>();
        if (!isComplete() && !isStopped()) {
            for(Task task:tasks){
                if (task.isInProgress() || task.isNotStarted()) {
                    runningTasks.add(task);
                    break;
                }
            }
        }
        return runningTasks;
    }
    
    @Override
    public boolean isComplete() {
        for(Task task:tasks) {
            if (!(task.isApproved() || task.isNotToBeDone())) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void relaunch(){
        tasks.get(0).start();
    }
}
