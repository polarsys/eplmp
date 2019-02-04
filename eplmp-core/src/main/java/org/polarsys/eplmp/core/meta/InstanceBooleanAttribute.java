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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a boolean type custom attribute of a document, part, product and other objects.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="INSTANCEBOOLEANATTRIBUTE")
@Entity
public class InstanceBooleanAttribute extends InstanceAttribute{
   
    private boolean booleanValue;
    
    public InstanceBooleanAttribute() {
    }
    
    public InstanceBooleanAttribute(String pName, boolean pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setBooleanValue(pValue);
    }

    @Override
    public Boolean getValue() {
        return booleanValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        booleanValue=Boolean.parseBoolean(pValue + "");
        return true;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }
    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}
