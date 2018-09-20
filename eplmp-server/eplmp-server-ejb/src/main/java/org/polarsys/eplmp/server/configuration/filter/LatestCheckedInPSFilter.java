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


package org.polarsys.eplmp.server.configuration.filter;

import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartSubstituteLink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductStructureFilter} implementation
 * which selects the latest checked in iteration.
 *
 * Filters the usage link to nominal, filters the iteration to the latest checked in.
 * This filter is strict, and will return only one result for the iteration.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since V1.1
 *
 */

public class LatestCheckedInPSFilter implements ProductStructureFilter, Serializable {

    private boolean diverge = false;

    public LatestCheckedInPSFilter(boolean diverge) {
        this.diverge = diverge;
    }

    @Override
    public List<PartIteration> filter(PartMaster partMaster) {
        List<PartIteration> partIterations = new ArrayList<>();
        PartIteration partIteration = partMaster.getLastRevision().getLastCheckedInIteration();

        if (partIteration != null) {
            partIterations.add(partIteration);
        }

        return partIterations;
    }

    @Override
    public List<PartLink> filter(List<PartLink> path) {

        List<PartLink> links = new ArrayList<>();

        PartLink link = path.get(path.size()-1);
        links.add(link);

        if(diverge){
            for(PartSubstituteLink substituteLink: link.getSubstitutes()){
                links.add(substituteLink);
            }
        }

        return links;
    }

}
