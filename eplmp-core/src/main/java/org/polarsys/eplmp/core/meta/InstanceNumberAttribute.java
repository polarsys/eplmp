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
 * Defines a numeric type custom attribute of a document, part,
 * product and other objects.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="INSTANCENUMBERATTRIBUTE")
@Entity
public class InstanceNumberAttribute extends InstanceAttribute{

    
   
    private float numberValue;
    
    public InstanceNumberAttribute() {
    }
    
    public InstanceNumberAttribute(String pName, float pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setNumberValue(pValue);
    }

    @Override
    public Float getValue() {
        return numberValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        try{
            numberValue=Float.parseFloat(pValue + "");
            return true;
        }catch(NumberFormatException ex){
            return false;
        }
    }

    public float getNumberValue() {
        return numberValue;
    }
    public void setNumberValue(float numberValue) {
        this.numberValue = numberValue;
    }
}
