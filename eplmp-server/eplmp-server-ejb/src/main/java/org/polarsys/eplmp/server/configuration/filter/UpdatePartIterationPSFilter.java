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
import org.polarsys.eplmp.core.product.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Morgan Guimard
 *
 * Check for cyclic assembly after part iteration update: must check on the wip and on the latest.
 * We also need to walk every substitute branches.
 *
 */
public class UpdatePartIterationPSFilter implements ProductStructureFilter, Serializable {

    private PartMasterKey rootKey;
    private PartIteration partIteration;

    public UpdatePartIterationPSFilter(PartIteration partIteration) {
        this.partIteration = partIteration;
        rootKey = partIteration.getKey().getPartRevision().getPartMaster();
    }

    @Override
    public List<PartIteration> filter(PartMaster part) {

        // Return wip on updated part iteration
        if(part.getKey().equals(rootKey)){
            return Collections.singletonList(partIteration);
        }

        // Return wip and last
        List<PartIteration> partIterations = new ArrayList<>();
        PartRevision partRevision = part.getLastRevision();
        PartIteration lastIteration = partRevision.getLastIteration();
        PartIteration lastCheckedInIteration = partRevision.getLastCheckedInIteration();

        if(partRevision.isCheckedOut() && lastCheckedInIteration != null){
            partIterations.add(lastCheckedInIteration);
        }

        partIterations.add(lastIteration);
        return partIterations;
    }

    @Override
    public List<PartLink> filter(List<PartLink> path) {

        List<PartLink> links = new ArrayList<>();
        PartLink link = path.get(path.size()-1);
        links.add(link);

        for(PartSubstituteLink substituteLink: link.getSubstitutes()){
            links.add(substituteLink);
        }

        return links;
    }

}
