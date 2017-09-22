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
import org.polarsys.eplmp.core.product.PartMaster;

import java.util.List;

/**
 * A product structure filter is used to select for a given {@link PartMaster}
 * one or more candidate {@link PartIteration}s.
 *
 * It does the equivalent operation for a given {@link PartLink}.
 *
 * Contrary to {@link ProductConfigSpec} the filtering is not strict in the
 * sens that more than one {@link PartIteration} and {@link PartLink}
 * can be returned.
 *
 * @author Morgan Guimard
 */

public interface ProductStructureFilter {

    /**
     * Selects the retained iteration(s) of the specified {@link PartMaster}.
     *
     * @param partMaster the part to filter
     *
     * @return the list of eligible part iterations
     */
    List<PartIteration> filter(PartMaster partMaster);

    /**
     * From a given {@link PartLink} selects one or many
     * effective links to consider. It should be noticed that the link is
     * supplied into the form of a complete path whereas the selected
     * links are returned individually.
     *
     * A frequent implementation is to return the {@link PartLink} itself,
     * hence the latest item of the list.
     *
     * @param path the path to the part link to filter into the form of an ordered
     *             list of {@link PartLink}s from the root of the structure
     *             to the {@link PartLink} itself.
     *
     * @return the list of eligible part links (unitary form).
     */
    List<PartLink> filter(List<PartLink> path);
}
