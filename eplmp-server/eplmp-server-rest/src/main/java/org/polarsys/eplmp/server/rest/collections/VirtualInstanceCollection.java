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
import org.polarsys.eplmp.core.product.PartRevision;

/**
 * @author Morgan Guimard
 */

public class VirtualInstanceCollection {

    private PartRevision rootPart;
    private ProductStructureFilter filter;

    public VirtualInstanceCollection() {
    }

    public VirtualInstanceCollection(PartRevision rootPart, ProductStructureFilter filter) {
        this.rootPart = rootPart;
        this.filter = filter;
    }

    public PartRevision getRootPart() {
        return rootPart;
    }

    public void setRootPart(PartRevision rootPart) {
        this.rootPart = rootPart;
    }

    public ProductStructureFilter getFilter() {
        return filter;
    }

    public void setFilter(ProductStructureFilter filter) {
        this.filter = filter;
    }
}
