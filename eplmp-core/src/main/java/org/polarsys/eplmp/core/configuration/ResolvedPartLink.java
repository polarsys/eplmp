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

package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;

import java.io.Serializable;

/**
 * Class that wraps a {@link PartLink} with the correct version
 * of {@link PartIteration} that has been selected according to a config spec.
 *
 * Instances of this class are not persisted.
 *
 * @author Morgan Guimard
 * @version 2.0, 07/21/15
 * @since 2.0
 */
public class ResolvedPartLink implements Serializable {

    private PartIteration partIteration;
    private PartLink partLink;

    public ResolvedPartLink(PartIteration partIteration, PartLink partLink) {
        this.partIteration = partIteration;
        this.partLink = partLink;
    }

    public PartIteration getPartIteration() {
        return partIteration;
    }

    public void setPartIteration(PartIteration partIteration) {
        this.partIteration = partIteration;
    }

    public PartLink getPartLink() {
        return partLink;
    }

    public void setPartLink(PartLink partLink) {
        this.partLink = partLink;
    }
}
