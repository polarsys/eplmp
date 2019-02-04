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
 * Identity class of {@link PartRevision} objects.
 *
 * @author Florent Garin
 */
public class PartRevisionKey implements Serializable, Comparable<PartRevisionKey>, Cloneable {

    private PartMasterKey partMaster;
    private String version;


    public PartRevisionKey() {
    }

    public PartRevisionKey(String pWorkspaceId, String pNumber, String pVersion) {
        partMaster = new PartMasterKey(pWorkspaceId, pNumber);
        version = pVersion;
    }

    public PartRevisionKey(PartMasterKey pPartMasterKey, String pVersion) {
        partMaster = pPartMasterKey;
        version = pVersion;
    }

    public PartMasterKey getPartMaster() {
        return partMaster;
    }

    public void setPartMaster(PartMasterKey partMaster) {
        this.partMaster = partMaster;
    }

    public String getWorkspaceId() {
        return partMaster.getWorkspace();
    }

    public String getPartMasterNumber() {
        return partMaster.getNumber();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String pVersion) {
        version = pVersion;
    }

    @Override
    public String toString() {
        return partMaster + "-" + version;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof PartRevisionKey)) {
            return false;
        }
        PartRevisionKey key = (PartRevisionKey) pObj;
        return key.partMaster.equals(partMaster) && key.version.equals(version);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + partMaster.hashCode();
        hash = 31 * hash + version.hashCode();
        return hash;
    }

    public int compareTo(PartRevisionKey pKey) {
        int wksMaster = partMaster.compareTo(pKey.partMaster);
        if (wksMaster != 0) {
            return wksMaster;
        } else {
            return version.compareTo(pKey.version);
        }
    }

    @Override
    public PartRevisionKey clone() {
        PartRevisionKey clone = null;
        try {
            clone = (PartRevisionKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
}
