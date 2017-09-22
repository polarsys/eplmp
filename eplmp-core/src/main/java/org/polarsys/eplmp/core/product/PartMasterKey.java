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
 * Identity class of {@link PartMaster} objects.
 * 
 * @author Florent Garin
 */
public class PartMasterKey implements Serializable, Comparable<PartMasterKey>, Cloneable {

    private String workspace;
    private String number;


    public PartMasterKey() {
    }
    
    public PartMasterKey(String pWorkspaceId, String pNumber) {
        workspace=pWorkspaceId;
        number=pNumber;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
  
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String pNumber) {
        number = pNumber;
    }
    
    
    @Override
    public String toString() {
        return workspace + "-" + number;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof PartMasterKey)) {
            return false;
        }
        PartMasterKey key = (PartMasterKey) pObj;
        return key.number.equals(number) && key.workspace.equals(workspace);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspace.hashCode();
        hash = 31 * hash + number.hashCode();
        return hash;
    }

    public int compareTo(PartMasterKey pKey) {
        int wksComp = workspace.compareTo(pKey.workspace);
        if (wksComp != 0) {
            return wksComp;
        }else {
            return number.compareTo(pKey.number);
        }
    }
    
    @Override
    public PartMasterKey clone() {
        PartMasterKey clone;
        try {
            clone = (PartMasterKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
}
