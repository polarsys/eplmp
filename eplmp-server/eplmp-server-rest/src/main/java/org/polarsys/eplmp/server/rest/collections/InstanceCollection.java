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

package org.polarsys.eplmp.server.rest.collections;

import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.util.Tools;

import java.util.List;

/**
 * @author Florent Garin
 */

public class InstanceCollection {

    // Used for services call
    private ConfigurationItemKey ciKey;

    // Used to walk the structure
    private ProductStructureFilter filter;

    // All instances under these paths
    private List<List<PartLink>> paths;

    public InstanceCollection(ConfigurationItemKey ciKey, ProductStructureFilter filter, List<List<PartLink>> paths) {
        this.ciKey = ciKey;
        this.filter = filter;
        this.paths = paths;
    }


    public ProductStructureFilter getFilter() {
        return filter;
    }

    public ConfigurationItemKey getCiKey() {
        return ciKey;
    }

    public List<List<PartLink>> getPaths() {
        return paths;
    }

    public boolean isFiltered(List<PartLink> currentPath) {
        for (List<PartLink> path : paths) {
            if (filter(path, currentPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean filter(List<PartLink> path, List<PartLink> currentPath) {
        return Tools.getPathAsString(currentPath).startsWith(Tools.getPathAsString(path));
    }
}
