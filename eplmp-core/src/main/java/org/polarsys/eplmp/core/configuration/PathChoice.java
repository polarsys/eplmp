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

import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartIteration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a potential link {@code partUsageLink} that should
 * (otherwise why use this class?) probably have
 * one or more substitution links.
 * {@code resolvedPath} is the path that leads to the choice.
 * This path is resolved in the sens that for each step the right
 * {@link PartIteration} is identified.
 *
 * Instances of this class are not persisted.
 *
 * @author Morgan Guimard
 * @version 2.0, 08/28/16
 * @since 2.0
 */
public class PathChoice implements Serializable{

    private List<ResolvedPartLink> resolvedPath = new ArrayList<>();
    private PartLink partUsageLink;

    public PathChoice() {
    }

    public PathChoice(List<ResolvedPartLink> resolvedPath, PartLink partUsageLink) {
        this.resolvedPath = resolvedPath;
        this.partUsageLink = partUsageLink;
    }

    public List<ResolvedPartLink> getResolvedPath() {
        return resolvedPath;
    }

    public void setResolvedPath(List<ResolvedPartLink> resolvedPath) {
        this.resolvedPath = resolvedPath;
    }

    public PartLink getPartUsageLink() {
        return partUsageLink;
    }

    public void setPartUsageLink(PartLink partUsageLink) {
        this.partUsageLink = partUsageLink;
    }

}
