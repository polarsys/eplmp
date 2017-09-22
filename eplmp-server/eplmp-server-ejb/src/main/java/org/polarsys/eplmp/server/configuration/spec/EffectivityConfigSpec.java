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

import org.polarsys.eplmp.core.configuration.ProductConfigSpec;
import org.polarsys.eplmp.core.configuration.ProductConfiguration;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A configuration specification used to filter {@link PartMaster}s
 * according to its effectivities.
 * 
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public abstract class EffectivityConfigSpec extends ProductConfigSpec {

    protected ConfigurationItem configurationItem;
    protected ProductConfiguration configuration;


    public EffectivityConfigSpec(ConfigurationItem configurationItem) {
        this.configurationItem=configurationItem;
    }

    public EffectivityConfigSpec(ProductConfiguration configuration) {
        this.configurationItem=configuration.getConfigurationItem();
        this.configuration=configuration;
    }


    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    @Override
    public PartIteration filterPartIteration(PartMaster partMaster) {
        List<PartRevision> revisions = partMaster.getPartRevisions();
        PartRevision pr=null;
        for(int i=revisions.size()-1;i>=0;i--){
            pr=revisions.get(i);
            if(isEffective(pr))
                break;
            else
                pr=null;
        }
        return pr==null?null:pr.getLastIteration();
    }

    @Override
    public PartLink filterPartLink(List<PartLink> path) {
        if(configuration !=null){
            PartLink nominalLink = path.get(path.size()-1);
            if(nominalLink.isOptional() && !configuration.isOptionalLinkRetained(Tools.getPathAsString(path))){
                return null;
            }
            for(PartSubstituteLink substituteLink:nominalLink.getSubstitutes()){

                List<PartLink> substitutePath = new ArrayList<>(path);
                substitutePath.set(substitutePath.size()-1,substituteLink);

                if(configuration.hasSubstituteLink(Tools.getPathAsString(substitutePath))){
                    return substituteLink;
                }
            }
            return nominalLink;
        }else{
            return filterNominalPartLink(path);
        }
    }

    private PartLink filterNominalPartLink(List<PartLink> path) {
        //Default implementation which returns the nominal link if not optional.
        //Hence no substitute link will be retained.

        PartLink nominalLink = path.get(path.size()-1);

        if(nominalLink.isOptional()){
            return null;
        }

        return nominalLink;
    }

    protected boolean isEffective(PartRevision pr) {
        Set<Effectivity> effectivities = pr.getEffectivities();
        for(Effectivity eff:effectivities){
            if(isEffective(eff))
                return true;
        }
        return false;
    }
    protected abstract boolean isEffective(Effectivity eff);

}
