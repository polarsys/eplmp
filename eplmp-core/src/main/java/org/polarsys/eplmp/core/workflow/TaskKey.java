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


import java.io.Serializable;

/**
 * Identity class of {@link Task} objects.
 *
 * @author Florent Garin
 */
public class TaskKey implements Serializable {
    

    private ActivityKey activity;
    private int num;
    
    public TaskKey() {
    }
    
    public TaskKey(ActivityKey pActivityKey, int pNum) {
        activity=pActivityKey;
        num=pNum;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + activity.hashCode();
        hash = 31 * hash + num;
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof TaskKey)) {
            return false;
        }
        TaskKey key = (TaskKey) pObj;
        return key.activity.equals(activity) && key.num==num;
    }
    
    @Override
    public String toString() {
        return activity.toString() + "-" + num;
    }

    public ActivityKey getActivity() {
        return activity;
    }

    public void setActivity(ActivityKey activity) {
        this.activity = activity;
    }

    public int getNum() {
        return num;
    }


    public void setNum(int num) {
        this.num = num;
    }
}
