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

package org.polarsys.eplmp.core.sharing;

import java.io.Serializable;

/**
 * Identity class of {@link SharedEntity} objects.
 *
 * @author Morgan Guimard
 */

public class SharedEntityKey implements Serializable{

    private String workspace;
    private String uuid;

    public SharedEntityKey() {
    }

    public SharedEntityKey(String workspace, String uuid) {
        this.workspace = workspace;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String pWorkspace) {
        workspace = pWorkspace;
    }

    @Override
    public String toString() {
        return workspace + "-" + uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SharedEntityKey that = (SharedEntityKey) o;

        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) &&
               !(workspace != null ? !workspace.equals(that.workspace) : that.workspace != null);

    }

    @Override
    public int hashCode() {
        int result = workspace != null ? workspace.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }
}
