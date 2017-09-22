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


package org.polarsys.eplmp.server.configuration.spec;

import org.polarsys.eplmp.core.configuration.ProductConfiguration;
import org.polarsys.eplmp.core.product.*;

import java.util.Date;
/**
 * A kind of {@link EffectivityConfigSpec} expressed by date and time.
 * 
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */

public class DateBasedEffectivityConfigSpec extends EffectivityConfigSpec {

    /**
     * The date and/or time of the context.
     */
    private Date date;


    public DateBasedEffectivityConfigSpec(Date date, ConfigurationItem configurationItem) {
        super(configurationItem);
        this.date=date;
    }
    public DateBasedEffectivityConfigSpec(Date date, ProductConfiguration configuration) {
        super(configuration);
        this.date=date;
    }

    @Override
    protected boolean isEffective(Effectivity eff){
        if(eff instanceof DateBasedEffectivity){
            DateBasedEffectivity dateEff=(DateBasedEffectivity) eff;
            return isEffective(dateEff);
        }else
            return false;
    }
    private boolean isEffective(DateBasedEffectivity dateEff){
        ConfigurationItem ci = dateEff.getConfigurationItem();
        if(ci != null && !ci.equals(configurationItem))
            return false;

        if(dateEff.getStartDate().after(date))
            return false;

        if(dateEff.getEndDate()!=null && dateEff.getEndDate().before(date))
            return false;

        return true;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
