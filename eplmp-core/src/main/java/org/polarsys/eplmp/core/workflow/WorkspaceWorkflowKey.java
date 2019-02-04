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
 * Identity class of {@link WorkspaceWorkflow} objects.
 *
 * @author Florent Garin
 */
public class WorkspaceWorkflowKey implements Serializable, Comparable<WorkspaceWorkflowKey>, Cloneable {

    private String workspace;
    private String id;


    public WorkspaceWorkflowKey() {
    }

    public WorkspaceWorkflowKey(String pWorkspaceId, String pId) {
        workspace = pWorkspaceId;
        id = pId;
    }


    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String pWorkspaceId) {
        workspace = pWorkspaceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }


    @Override
    public String toString() {
        return workspace + "-" + id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkspaceWorkflowKey that = (WorkspaceWorkflowKey) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!workspace.equals(that.workspace)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = workspace.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }


    public int compareTo(WorkspaceWorkflowKey pKey) {
        int wksComp = workspace.compareTo(pKey.workspace);
        if (wksComp != 0) {
            return wksComp;
        } else {
            return id.compareTo(pKey.id);
        }
    }

    @Override
    public WorkspaceWorkflowKey clone() {
        WorkspaceWorkflowKey clone;
        try {
            clone = (WorkspaceWorkflowKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
}
