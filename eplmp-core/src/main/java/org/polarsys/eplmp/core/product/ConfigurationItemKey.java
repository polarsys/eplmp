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

package org.polarsys.eplmp.core.product;

import java.io.Serializable;

/**
 * Identity class of {@link ConfigurationItem} objects.
 * objects.
 * 
 * @author Florent Garin
 */
public class ConfigurationItemKey implements Serializable {
    
    private String workspace;
    private String id;
    
    public ConfigurationItemKey() {
    }
    
    public ConfigurationItemKey(String pWorkspaceId, String pId) {
        workspace=pWorkspaceId;
        id=pId;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspace.hashCode();
        hash = 31 * hash + id.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof ConfigurationItemKey)) {
            return false;
        }
        ConfigurationItemKey key = (ConfigurationItemKey) pObj;
        return key.id.equals(id) && key.workspace.equals(workspace);
    }
    
    @Override
    public String toString() {
        return workspace + "-" + id;
    }
    
    public String getWorkspace() {
        return workspace;
    }
    
    public void setWorkspace(String pWorkspace) {
        workspace = pWorkspace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
