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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.product.PartIterationKey;

/**
 *
 * @author Florent Garin
 */
public interface IConverterManagerLocal {
    void convertCADFileToOBJ(PartIterationKey pPartIPK, BinaryResource cadFile);
}
