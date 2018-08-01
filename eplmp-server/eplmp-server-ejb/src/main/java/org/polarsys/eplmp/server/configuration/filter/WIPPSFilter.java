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
import org.polarsys.eplmp.core.product.PartSubstituteLink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductStructureFilter} implementation
 * which selects the latest iteration (checked in or not).
 *
 * @author Morgan Guimard
 */
public class WIPPSFilter implements ProductStructureFilter, Serializable {

    private User user;
    private boolean diverge = false;

    public WIPPSFilter() {
    }

    public WIPPSFilter(User user) {
        this.user = user;
    }
    public WIPPSFilter(User user, boolean diverge) {
        this.user = user;
        this.diverge = diverge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
            for(PartSubstituteLink substituteLink: link.getSubstitutes()){
                links.add(substituteLink);
            }
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
