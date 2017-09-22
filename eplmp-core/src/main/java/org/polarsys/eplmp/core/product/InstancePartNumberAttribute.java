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

import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.meta.InstanceAttribute;

import javax.persistence.*;

/**
 * Defines a custom attribute which holds a reference to an existing
 * {@link PartMaster} object.
 * 
 * @author Florent Garin
 * @version 2.5, 26/09/16
 * @since   V2.5
 */
@Table(name="INSTANCEPARTNUMBERATTRIBUTE")
@Entity
public class InstancePartNumberAttribute extends InstanceAttribute{


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="PARTMASTER_PARTNUMBER", referencedColumnName="PARTNUMBER"),
            @JoinColumn(name="PARTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
    })
    private PartMaster partMasterValue;

    public InstancePartNumberAttribute() {
    }

    public InstancePartNumberAttribute(String pName, PartMaster pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setPartMasterValue(pValue);
    }

    @Override
    public PartMaster getValue() {
        return partMasterValue;
    }

    @Override
    public boolean setValue(Object pValue) {
        if(pValue instanceof PartMaster){
            partMasterValue=(PartMaster)pValue;
            return true;
        }else if(pValue instanceof PartMasterKey){
            PartMasterKey key=(PartMasterKey)pValue;
            partMasterValue=new PartMaster(new Workspace(key.getWorkspace()),key.getNumber());
            return true;
        }else{
            partMasterValue=null;
            return false;
        }

    }

    public PartMaster getPartMasterValue() {
        return partMasterValue;
    }
    public void setPartMasterValue(PartMaster partMasterValue) {
        this.partMasterValue = partMasterValue;
    }
}
