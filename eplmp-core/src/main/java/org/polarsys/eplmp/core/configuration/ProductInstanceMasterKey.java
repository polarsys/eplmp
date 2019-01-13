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

package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.product.ConfigurationItemKey;

import java.io.Serializable;

/**
 * Identity class of {@link ProductInstanceMaster} objects.
 * 
 * @author Florent Garin
 */
public class ProductInstanceMasterKey implements Serializable {

    private ConfigurationItemKey instanceOf;
    private String serialNumber;


    public ProductInstanceMasterKey() {
    }

    public ProductInstanceMasterKey(String serialNumber, String pWorkspaceId, String pId) {
        this.serialNumber=serialNumber;
        this.instanceOf=new ConfigurationItemKey(pWorkspaceId,pId);
    }

    public ProductInstanceMasterKey(String serialNumber, ConfigurationItemKey ciKey) {
        this.serialNumber=serialNumber;
        this.instanceOf=ciKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ConfigurationItemKey getInstanceOf() {
        return instanceOf;
    }

    public void setInstanceOf(ConfigurationItemKey instanceOf) {
        this.instanceOf = instanceOf;
    }

    @Override
    public String toString() {
        return instanceOf + "-" + serialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductInstanceMasterKey that = (ProductInstanceMasterKey) o;

        return instanceOf.equals(that.instanceOf) && serialNumber.equals(that.serialNumber);

    }

    @Override
    public int hashCode() {
        int result = instanceOf.hashCode();
        result = 31 * result + serialNumber.hashCode();
        return result;
    }
}
