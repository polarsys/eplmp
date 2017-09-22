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

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Workflows organize tasks around documents, parts (or other objects)
 * on which they are applied.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="WORKFLOW")
@javax.persistence.Entity
public class Workflow implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToMany(mappedBy = "workflow", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("step ASC")
    private List<Activity> activities = new LinkedList<>();

    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date abortedDate;

    private String finalLifeCycleState;

    public Workflow() {
    }
    public Workflow(String pFinalLifeCycleState) {
        finalLifeCycleState = pFinalLifeCycleState;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<Activity> getActivities() {
        return activities;
    }
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
        for(Activity activity : activities){
            activity.setWorkflow(this);
        }
    }

    public Activity getActivity(int pIndex) {
        return activities.get(pIndex);
    }
    public Activity getCurrentActivity() {
        if (getCurrentStep() < activities.size()) {
            return getActivity(getCurrentStep());
        } else {
            return null;
        }
    }

    public int getCurrentStep() {
        int i = 0;
        for (Activity activity : activities) {
            if (activity.isComplete()) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    public String getFinalLifeCycleState() {
        return finalLifeCycleState;
    }
    public void setFinalLifeCycleState(String finalLifeCycleState) {
        this.finalLifeCycleState = finalLifeCycleState;
    }

    public Date getAbortedDate() {
        return (abortedDate!=null) ? (Date) abortedDate.clone() : null;
    }
    public void setAbortedDate(Date abortedDate) {
        this.abortedDate = (abortedDate!=null) ? (Date) abortedDate.clone() : null;
    }

    public Collection<Task> getRunningTasks() {

        Activity current = getCurrentActivity();
        if (current != null) {
            return current.getOpenTasks();
        } else {
            return new ArrayList<>();
        }
    }

    public Collection<Task> getTasks(){
        Collection<Task> tasks = new ArrayList<>();
        for(Activity activity:activities){
            tasks.addAll(activity.getTasks());
        }
        return tasks;
    }

    public int numberOfSteps() {
        return activities.size();
    }

    public List<String> getLifeCycle() {
        List<String> lc = new LinkedList<>();
        for (Activity activity : activities) {
            lc.add(activity.getLifeCycleState());
        }

        return lc;
    }
    public String getLifeCycleState() {
        Activity current = getCurrentActivity();
        return current == null ? finalLifeCycleState : current.getLifeCycleState();
    }

    public void abort() {
        for (Activity activity : activities) {
            for(Task task : activity.getTasks()){
                task.stop();
            }
        }
        this.setAbortedDate(new Date());
    }
    public void relaunch(int relaunchActivityStep) {
        for(Activity a :activities){
            if(a.getStep() < relaunchActivityStep){ 
                for(Task t : a.getTasks()){
                    t.reset(Task.Status.NOT_TO_BE_DONE);
                }
            }
            if(a.getStep() >= relaunchActivityStep){
                for(Task t : a.getTasks()){
                    t.reset(Task.Status.NOT_STARTED);
                }
            }
        }

        Activity currentActivity = activities.get(relaunchActivityStep);
        currentActivity.relaunch();

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Workflow)) {
            return false;
        }
        Workflow workflow = (Workflow) obj;
        return workflow.id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
