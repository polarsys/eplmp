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
import org.polarsys.eplmp.core.product.ConfigurationItem;
import org.polarsys.eplmp.core.product.Effectivity;
import org.polarsys.eplmp.core.product.SerialNumberBasedEffectivity;
import org.polarsys.eplmp.core.util.AlphanumericComparator;

import java.util.Comparator;

/**
 * A kind of {@link EffectivityConfigSpec} based on serial number.
 * 
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class SerialNumberBasedEffectivityConfigSpec extends EffectivityConfigSpec {

    /**
     * The serial number of the particular item specified by the context.
     */
    private String number;

    private static final Comparator<CharSequence> STRING_COMPARATOR = new AlphanumericComparator();


    public SerialNumberBasedEffectivityConfigSpec(String number, ConfigurationItem configurationItem) {
        super(configurationItem);
        this.number=number;
    }
    public SerialNumberBasedEffectivityConfigSpec(String number, ProductConfiguration configuration) {
        super(configuration);
        this.number=number;
    }


    @Override
    protected boolean isEffective(Effectivity eff){
        if(eff instanceof SerialNumberBasedEffectivity){
            SerialNumberBasedEffectivity serialEff=(SerialNumberBasedEffectivity) eff;
            return isEffective(serialEff);
        }else
            return false;
    }
    private boolean isEffective(SerialNumberBasedEffectivity serialEff){
        ConfigurationItem ci = serialEff.getConfigurationItem();
        if(!configurationItem.equals(ci))
            return false;

        if(STRING_COMPARATOR.compare(number, serialEff.getStartNumber())<0)
            return false;

        if(serialEff.getEndNumber()!=null && STRING_COMPARATOR.compare(number, serialEff.getEndNumber())>0)
            return false;

        return true;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
    
}
