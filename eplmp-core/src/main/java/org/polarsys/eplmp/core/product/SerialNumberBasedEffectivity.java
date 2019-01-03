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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SerialNumberBasedEffectivity indicates that an item is effective while a
 * configuration item is being produced in a range of serial numbered units.
 *
 * @author Florent Garin
 * @version 1.1, 18/10/11
 * @since   V1.1
 */
@Table(name="SERIALNUMBERBASEDEFFECTIVITY")
@Entity
public class SerialNumberBasedEffectivity extends Effectivity{

    /**
     * The serial number of the first item that the effectivity applies to.
     */
    private String startNumber;

    /**
     * The serial number of the last item that the effectivity applies to.
     * This value is optional.
     */
    private String endNumber;

    public SerialNumberBasedEffectivity() {
    }

    public SerialNumberBasedEffectivity(String pName, ConfigurationItem configurationItem, String startNumber, String endNumber) {
        super(pName, configurationItem);
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }


    public String getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
    }



}
