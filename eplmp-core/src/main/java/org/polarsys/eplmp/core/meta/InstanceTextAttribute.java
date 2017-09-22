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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a text type custom attribute of a document, part, product and other objects.
 * Be aware that this type cannot accept text longer than 255 characters.
 * {@link InstanceLongTextAttribute} has not that constraint.
 *
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="INSTANCETEXTATTRIBUTE")
@Entity
public class InstanceTextAttribute extends InstanceAttribute{

    

    private String textValue;
    
    public InstanceTextAttribute() {
    }
    
    public InstanceTextAttribute(String pName, String pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setTextValue(pValue);
    }

    @Override
    public String getValue() {
        return textValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        textValue = pValue != null ? pValue + "" : "";
        return true;
    }

    public String getTextValue() {
        return textValue;
    }
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
}
