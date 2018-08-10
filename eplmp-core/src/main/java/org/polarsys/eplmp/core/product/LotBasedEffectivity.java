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
 * LotBasedEffectivity indicates that an item is effective while a
 * configuration item is being produced in a specified lot.
 * 
 * @author Florent Garin
 * @version 1.1, 18/10/11
 * @since   V1.1
 */
@Table(name="LOTBASEDEFFECTIVITY")
@Entity
public class LotBasedEffectivity extends Effectivity{

    /**
     * The identification of the first batch of items
     * that the effectivity applies to.
     */
    private String startLotId;
    

    /**
     * The identification of the last batch of items
     * that the effectivity applies to.
     * This value is optional.
     */
    private String endLotId;

    public LotBasedEffectivity() {
    }

    public String getStartLotId() {
        return startLotId;
    }

    public void setStartLotId(String startLotId) {
        this.startLotId = startLotId;
    }

    
    public String getEndLotId() {
        return endLotId;
    }

    public void setEndLotId(String endLotId) {
        this.endLotId = endLotId;
    }
    
    
}
