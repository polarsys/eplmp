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
 * Identity class of {@link ListOfValues} objects.
 *
 * @author Florent Garin
 */
public class ListOfValuesKey implements Serializable {

    private String workspaceId;
    private String name;

    public ListOfValuesKey() {
    }

    public ListOfValuesKey(String pWorkspaceId, String pName) {
        workspaceId=pWorkspaceId;
        name =pName;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + name.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof ListOfValuesKey)) {
            return false;
        }
        ListOfValuesKey key = (ListOfValuesKey) pObj;
        return key.workspaceId.equals(workspaceId) && key.name.equals(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public String getWorkspaceId() {
        return workspaceId;
    }
    
    public void setWorkspaceId(String pWorkspaceId) {
        workspaceId = pWorkspaceId;
    }
    
    
    public String getName(){
        return name;
    }
    
    public void setName(String pName){
        name =pName;
    }
}
