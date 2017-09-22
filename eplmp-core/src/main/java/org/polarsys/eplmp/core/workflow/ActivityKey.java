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
 * Identity class of {@link Activity} objects.
 *
 * @author Florent Garin
 */
public class ActivityKey implements Serializable {

    private int workflow;
    private int step;

    public ActivityKey() {
    }

    public ActivityKey(int pWorkflowId, int pStep) {
        workflow = pWorkflowId;
        step = pStep;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workflow;
        hash = 31 * hash + step;
        return hash;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof ActivityKey)) {
            return false;
        }
        ActivityKey key = (ActivityKey) pObj;
        return key.workflow == workflow && key.step == step;
    }

    @Override
    public String toString() {
        return workflow + "-" + step;
    }

    public int getWorkflowId() {
        return workflow;
    }

    public void setWorkflowId(int workflowId) {
        this.workflow = workflowId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int pStep) {
        step = pStep;
    }
}
