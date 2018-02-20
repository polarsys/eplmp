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
import org.polarsys.eplmp.core.product.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductStructureFilter} implementation
 * which selects the last iteration of the latest released revision.
 *
 * @author Taylor Labejof
 * @version 2.0, 29/08/14
 * @since   V2.0
 */

public class LatestReleasedPSFilter implements ProductStructureFilter, Serializable {

    private User user;
    private boolean diverge = false;

    public LatestReleasedPSFilter() {
    }

    public LatestReleasedPSFilter(User user) {
        this.user = user;
    }
    public LatestReleasedPSFilter(User user, boolean diverge) {
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
        PartRevision partRevision = part.getLastReleasedRevision();
        if(partRevision != null){
            return Collections.singletonList(partRevision.getLastIteration());
        }
        return new ArrayList<>();
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
