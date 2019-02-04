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

import java.io.Serializable;

/**
 * Identity class of {@link TaskModel} objects.
 *
 * @author Florent Garin
 */
public class TaskModelKey implements Serializable {

    private int activityModel;
    private int num;
    
    public TaskModelKey() {
    }
    
    public TaskModelKey(int pActivityModelId, int pNum) {
        activityModel =pActivityModelId;
        num=pNum;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + activityModel;
        hash = 31 * hash + num;
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof TaskModelKey)) {
            return false;
        }
        TaskModelKey key = (TaskModelKey) pObj;
        return key.activityModel == activityModel &&
               key.num==num;
    }
    
    @Override
    public String toString() {
        return activityModel + "-" + num;
    }

    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }

    public int getActivityModel() {
        return activityModel;
    }

    public void setActivityModel(int activityModel) {
        this.activityModel = activityModel;
    }
}
