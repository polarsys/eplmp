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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Defines common attributes and behaviors for activities model.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="ACTIVITYMODEL")
@XmlSeeAlso({SequentialActivityModel.class, ParallelActivityModel.class})
@Inheritance()
@Entity
public abstract class ActivityModel implements Serializable, Cloneable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="WORKFLOWMODEL_ID", referencedColumnName="ID"),
        @JoinColumn(name="WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
    })
    protected WorkflowModel workflowModel;   
    
    @OneToMany(mappedBy = "activityModel", orphanRemoval = true, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @OrderBy("num")
    protected List<TaskModel> taskModels=new LinkedList<>();

    protected int step;

    @ManyToOne(optional = true,fetch=FetchType.EAGER)
    @JoinTable (
            name="ACTIVITYMODEL_RELAUNCH",
            joinColumns={
                    @JoinColumn(name="ACTIVITYMODEL_ID", referencedColumnName="ID")
            },
            inverseJoinColumns={
                    @JoinColumn(name="RELAUNCHACTIVITYMODEL_ID", referencedColumnName="ID")
            }
    )
    private ActivityModel relaunchActivity;
    
    protected String lifeCycleState;

    public ActivityModel(){
    
    }
    
    public ActivityModel(WorkflowModel pWorkflowModel, int pStep, List<TaskModel> pTaskModels, String pLifeCycleState){
        setWorkflowModel(pWorkflowModel);
        taskModels=pTaskModels;
        step=pStep;
        lifeCycleState=pLifeCycleState;
    }
    
    
    public void setWorkflowModel(WorkflowModel pWorkflowModel){
        workflowModel=pWorkflowModel;
    }

    public int getStep(){
        return step;
    }
    
    public void setStep(int pStep){
        step=pStep;
    }
    
    public String getLifeCycleState(){
        return lifeCycleState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTaskModels(List<TaskModel> taskModels) {
        this.taskModels = taskModels;
    }


    @XmlTransient
    public WorkflowModel getWorkflowModel() {
        return workflowModel;
    }

    public void setLifeCycleState(String pLifeCycleState){
        lifeCycleState=pLifeCycleState;
    }
    
    public void addTaskModel(TaskModel pTaskModel) {
        taskModels.add(pTaskModel);
        int index = taskModels.size()-1;
        pTaskModel.setNum(index);
    }
    
    public TaskModel removeTaskModel(int pOrder) {
        TaskModel taskModel = taskModels.remove(pOrder);
        for(int i=pOrder;i<taskModels.size();i++){
            taskModels.get(i).setNum(i);
        }
        return taskModel;
    }
    
    public void removeTaskModel(TaskModel pTaskModel) {
        int index = taskModels.indexOf(pTaskModel);
        removeTaskModel(index);
    }

    public List<TaskModel> getTaskModels() {
        return taskModels;
    }

    @XmlTransient
    public ActivityModel getRelaunchActivity() {
        return relaunchActivity;
    }

    public void setRelaunchActivity(ActivityModel relaunchActivity) {
        this.relaunchActivity = relaunchActivity;
    }

    /**
     * perform a deep clone operation
     */
    @Override
    public ActivityModel clone() {
        ActivityModel clone;
        try {
            clone = (ActivityModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        //perform a deep copy
        List<TaskModel> clonedTaskModels = new LinkedList<>();
        for (TaskModel taskModel : taskModels) {
            TaskModel clonedTaskModel=taskModel.clone();
            clonedTaskModel.setActivityModel(clone);
            clonedTaskModels.add(clonedTaskModel);
        }
        clone.taskModels = clonedTaskModels;
        return clone;
    }

    public abstract Activity createActivity(Map<Role,Collection<User>> roleUserMap, Map<Role,Collection<UserGroup>> roleGroupMap);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityModel that = (ActivityModel) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
