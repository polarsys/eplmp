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
package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.product.PartIteration;

/**
 * ProductBaselineType is an enum representing the type of
 * the {@link ProductBaseline} which informs on the algorithm used
 * (at baseline creation time) in order
 * to select the proper {@link PartIteration}.
 *
 * @author Morgan Guimard
 */
public enum ProductBaselineType {
    LATEST, RELEASED, EFFECTIVE_DATE, EFFECTIVE_SERIAL_NUMBER, EFFECTIVE_LOT_ID
}
