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
import org.polarsys.eplmp.core.product.LotBasedEffectivity;
import org.polarsys.eplmp.core.util.AlphanumericComparator;

import java.util.Comparator;

/**
 * A kind of {@link EffectivityConfigSpec} based on a specific lot.
 * 
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class LotBasedEffectivityConfigSpec extends EffectivityConfigSpec {

    /**
     * The lot id of the particular batch of items specified by the context.
     */
    private String lotId;

    private final static Comparator<CharSequence> STRING_COMPARATOR = new AlphanumericComparator();

    public LotBasedEffectivityConfigSpec(String lotId, ConfigurationItem configurationItem) {
        super(configurationItem);
        this.lotId=lotId;
    }
    public LotBasedEffectivityConfigSpec(String lotId, ProductConfiguration configuration) {
        super(configuration);
        this.lotId=lotId;
    }


    @Override
    protected boolean isEffective(Effectivity eff){
        if(eff instanceof LotBasedEffectivity){
            LotBasedEffectivity lotEff=(LotBasedEffectivity) eff;
            return isEffective(lotEff);
        }else
            return false;
    }
    private boolean isEffective(LotBasedEffectivity lotEff){
        ConfigurationItem ci = lotEff.getConfigurationItem();
        if(!configurationItem.equals(ci))
            return false;

        if(STRING_COMPARATOR.compare(lotId, lotEff.getStartLotId())<0)
            return false;

        if(lotEff.getEndLotId()!=null && STRING_COMPARATOR.compare(lotId, lotEff.getEndLotId())>0)
            return false;

        return true;
    }


    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }
    
}
