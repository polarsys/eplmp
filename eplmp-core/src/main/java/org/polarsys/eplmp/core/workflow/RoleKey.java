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
 * Identity class of {@link Role} objects.
 *
 * @author Morgan Guimard
 */
public class RoleKey implements Serializable {


    private String workspace;
    private String name;

    public RoleKey() {
    }

    public RoleKey(String pWorkspaceId, String pName) {
        workspace = pWorkspaceId;
        name = pName;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleKey roleKey = (RoleKey) o;

        return name.equals(roleKey.name) && workspace.equals(roleKey.workspace);

    }

    @Override
    public int hashCode() {
        int result = workspace.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
