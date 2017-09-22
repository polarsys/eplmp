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
 * Defines an URL type custom attribute of a document, part, product and other objects.
 * 
 * @author Emmanuel Nhan
 * @version 1.0 23/07/2009
 * @since   V1.0
 */
@Table(name="INSTANCEURLATTRIBUTE")
@Entity
public class InstanceURLAttribute extends InstanceAttribute {

    private String urlValue;

    public InstanceURLAttribute() {
    }

    public InstanceURLAttribute(String pName, String pValue, boolean pMandatory) {
        super(pName, pMandatory);
        setUrlValue(pValue);
    }

    @Override
    public String getValue() {
        return urlValue;
    }
    @Override
    public boolean setValue(Object pValue) {
        urlValue = pValue != null ? pValue + "" : "";
        return true;
    }

    public String getUrlValue() {
        return urlValue;
    }
    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }
}
