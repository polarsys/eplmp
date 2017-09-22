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
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Defines a text type custom attribute of a document, part, product
 * and other objects.
 * InstanceLongTextAttribute can store value of almost infinite size but if you
 * are sure that the text is not longer than 255 characters it is recommended
 * for better performance to rely on {@link InstanceTextAttribute}.
 * 
 * @author Florent Garin
 * @version 2.0, 29/08/16
 * @since   V2.0
 */
@Table(name="INSTANCELONGTEXTATTRIBUTE")
@Entity
public class InstanceLongTextAttribute extends InstanceAttribute{


    @Lob
    private String longTextValue;

    public InstanceLongTextAttribute() {
    }

    public InstanceLongTextAttribute(String pName, String pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setLongTextValue(pValue);
    }

    @Override
    public String getValue() {
        return longTextValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        longTextValue = pValue != null ? pValue + "" : "";
        return true;
    }

    public String getLongTextValue() {
        return longTextValue;
    }
    public void setLongTextValue(String longTextValue) {
        this.longTextValue = longTextValue;
    }
}
