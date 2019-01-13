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

package org.polarsys.eplmp.core.product;

import java.io.Serializable;

/**
 * Identity class of {@link PartIteration} objects.
 * 
 * @author Florent Garin
 */
public class PartIterationKey implements Serializable {
    
    private PartRevisionKey partRevision;
    private int iteration;
    
    public PartIterationKey() {
    }

    public PartIterationKey(String pWorkspaceId, String pNumber, String pVersion, int pIteration) {
        partRevision= new PartRevisionKey(pWorkspaceId, pNumber, pVersion);
        iteration=pIteration;
    }

    public PartIterationKey(PartRevisionKey pPartRevisionKey, int pIteration) {
        partRevision=pPartRevisionKey;
        iteration=pIteration;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + partRevision.hashCode();
        hash = 31 * hash + iteration;
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof PartIterationKey)) {
            return false;
        }
        PartIterationKey key = (PartIterationKey) pObj;
        return key.partRevision.equals(partRevision) && key.iteration==iteration;
    }
    
    @Override
    public String toString() {
        return partRevision + "-" + iteration;
    }

    public PartRevisionKey getPartRevision() {
        return partRevision;
    }
    public void setPartRevision(PartRevisionKey partRevision) {
        this.partRevision = partRevision;
    }

    public int getIteration(){
        return iteration;
    }
    public void setIteration(int pIteration){
        iteration=pIteration;
    }

    public String getWorkspaceId() {
        return partRevision.getPartMaster().getWorkspace();
    }
    public String getPartMasterNumber() {
        return partRevision.getPartMaster().getNumber();
    }

    public String getPartRevisionVersion() {
        return partRevision.getVersion();
    }
}
