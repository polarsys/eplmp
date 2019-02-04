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

package org.polarsys.eplmp.core.meta;

import java.io.Serializable;

/**
 * Identity class of {@link Tag} objects.
 *
 * @author Florent Garin
 */

public class TagKey implements Serializable {
    
    private String workspace;
    private String label;
    
    public TagKey() {
    }
    
    public TagKey(String pWorkspaceId, String pLabel) {
        workspace=pWorkspaceId;
        label=pLabel;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspace.hashCode();
        hash = 31 * hash + label.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof TagKey)) {
            return false;
        }
        TagKey key = (TagKey) pObj;
        return key.workspace.equals(workspace) && key.label.equals(label);
    }
    
    @Override
    public String toString() {
        return label;
    }
    
    public String getWorkspace() {
        return workspace;
    }
    
    public void setWorkspace(String pWorkspaceId) {
        workspace = pWorkspaceId;
    }
    
    
    public String getLabel(){
        return label;
    }
    
    public void setLabel(String pLabel){
        label=pLabel;
    }
}
