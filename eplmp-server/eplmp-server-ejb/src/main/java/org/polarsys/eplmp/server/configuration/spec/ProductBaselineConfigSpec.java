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
package org.polarsys.eplmp.server.configuration.spec;

import org.polarsys.eplmp.core.configuration.ProductBaseline;
import org.polarsys.eplmp.core.product.PartIteration;

import javax.validation.constraints.NotNull;


/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductConfigSpec} which returns the {@link PartIteration}
 * which belongs to the given baseline.
 *
 * As a baseline should have no ambiguity, if a filter returns null the spec is considered as invalid.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class ProductBaselineConfigSpec extends ResolvedCollectionConfigSpec {
    public ProductBaselineConfigSpec(@NotNull ProductBaseline productBaseline) {
        super(productBaseline.getPartCollection(), productBaseline.getOptionalUsageLinks(), productBaseline.getSubstituteLinks());
    }
}
