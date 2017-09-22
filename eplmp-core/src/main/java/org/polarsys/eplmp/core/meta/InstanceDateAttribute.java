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

package org.polarsys.eplmp.core.meta;

import org.polarsys.eplmp.core.util.DateUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.ParseException;
import java.util.Date;

/**
 * Defines a date type custom attribute of a document, part, product and other objects.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="INSTANCEDATEATTRIBUTE")
@Entity
public class InstanceDateAttribute extends InstanceAttribute{

    
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateValue;
    
    public InstanceDateAttribute() {
    }
    
    public InstanceDateAttribute(String pName, Date pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setDateValue(pValue);
    }

    @Override
    public Date getValue() {
        return dateValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        if(pValue instanceof Date){
            dateValue=(Date)pValue;
            return true;
        }else if(pValue instanceof String){
            try {
                dateValue = DateUtils.parse((String) pValue);
                return true;
            } catch (ParseException pe) {
                try {
                    dateValue = new Date(Long.parseLong((String) pValue));
                    return true;
                }catch(NumberFormatException nfe){
                    return false;
                }
            }

        }else{
            dateValue=null;
            return false;
        }
    }

    public Date getDateValue() {
        return dateValue;
    }
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
    
    
    /**
     * perform a deep clone operation
     */
    @Override
    public InstanceDateAttribute clone() {
        InstanceDateAttribute clone;
        clone = (InstanceDateAttribute) super.clone();
        
        if(dateValue!=null)
            clone.dateValue = (Date) dateValue.clone();
        return clone;
    }
}
