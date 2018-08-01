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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * DateBasedEffectivity indicates that an item is effective while a
 * configuration item is being produced during a date range.
 * 
 * @author Florent Garin
 * @version 1.1, 18/10/11
 * @since   V1.1
 */
@Table(name="DATEBASEDEFFECTIVITY")
@Entity
public class DateBasedEffectivity extends Effectivity{

    /**
     * The date and/or time when the effectivity starts.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    
    /**
     * The date and/or time when the effectivity ends.
     * If a value for this attribute is not set, 
     * then the effectivity has no defined end.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public DateBasedEffectivity() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
