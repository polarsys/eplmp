/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/
package org.polarsys.eplmp.core.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An ordered collection of {@link PartLink} useful to reference in
 * an unambiguous manner a part inside a Product Breakdown Structure.
 *
 *
 * @author Elisabel Généreux
 * @version 2.5, 29/04/15
 * @see PartUsageLink
 * @see PartSubstituteLink
 * @see PartLink
 * @since   V2.5
 */
public class PartLinkList implements Serializable{

    private List<PartLink> path = new ArrayList<>();

    public PartLinkList(List<PartLink> path) {
        this.path = path;
    }

    public List<PartLink> getPath() {
        return path;
    }

    public void setPath(List<PartLink> path) {
        this.path = path;
    }

    public int size() {
        return this.path.size();
    }

    public boolean isEmpty() {
        return this.path.isEmpty();
    }

    public PartLink[] toArray() {
        return path.toArray(new PartLink[this.size()]);
    }

}
