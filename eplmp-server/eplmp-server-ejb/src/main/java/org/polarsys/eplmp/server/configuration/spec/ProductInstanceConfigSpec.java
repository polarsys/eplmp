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

import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;

import javax.validation.constraints.NotNull;


/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductConfigSpec} which returns the {@link org.polarsys.eplmp.core.product.PartIteration} and {@link org.polarsys.eplmp.core.document.DocumentIteration}
 * which belong to the given baseline.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class ProductInstanceConfigSpec extends ResolvedCollectionConfigSpec {
    public ProductInstanceConfigSpec(@NotNull ProductInstanceIteration productInstanceIteration) {
        super(productInstanceIteration.getPartCollection(),productInstanceIteration.getOptionalUsageLinks(),productInstanceIteration.getSubstituteLinks());
    }
}
