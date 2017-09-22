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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;

/**
 *
 * @author Morgan Guimard
 */
public interface IPSFilterManagerLocal {
    ProductStructureFilter getBaselinePSFilter(int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;
    ProductStructureFilter getProductInstanceConfigSpec(ConfigurationItemKey ciKey, String serialNumber) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;
    ProductStructureFilter getPSFilter(ConfigurationItemKey ciKey, String filterType, boolean diverge) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;
}
