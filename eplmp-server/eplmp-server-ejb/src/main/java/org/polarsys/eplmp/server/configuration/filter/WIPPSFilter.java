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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductStructureFilter} implementation
 * which selects the latest iteration (checked in or not).
 *
 * @author Morgan Guimard
 */
public class WIPPSFilter implements ProductStructureFilter, Serializable {

    private User user;
    private boolean diverge = false;

    public WIPPSFilter(User user) {
        this.user = user;
    }

    public WIPPSFilter(User user, boolean diverge) {
        this.user = user;
        this.diverge = diverge;
    }

    @Override
    public List<PartIteration> filter(PartMaster part) {
        List<PartIteration> partIterations = new ArrayList<>();
        PartIteration partIteration = getLastAccessibleIteration(part);
        if(partIteration != null) {
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
            links.addAll(link.getSubstitutes().stream().collect(Collectors.toList()));
        }

        return links;
    }

    private PartIteration getLastAccessibleIteration(PartMaster partMaster) {
        PartIteration iteration = null;
        for(int i = partMaster.getPartRevisions().size()-1; i >= 0 && iteration == null; i--) {
            iteration = partMaster.getPartRevisions().get(i).getLastAccessibleIteration(user);
        }
        return iteration;
    }


}
