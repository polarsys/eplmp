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

import org.polarsys.eplmp.core.configuration.CascadeResult;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;

/**
 * @author Charles Fallourd
 * @version 2.5, 09/03/16
 * @since   V2.5
 */
public interface ICascadeActionManagerLocal {

    CascadeResult cascadeCheckOut(ConfigurationItemKey configurationItemKey, String path) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, EntityConstraintException, PartMasterNotFoundException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException;

    CascadeResult cascadeUndoCheckOut(ConfigurationItemKey configurationItemKey, String path) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, EntityConstraintException, PartMasterNotFoundException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException;

    CascadeResult cascadeCheckIn(ConfigurationItemKey configurationItemKey, String path, String iterationNote) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartMasterNotFoundException, EntityConstraintException, NotAllowedException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException;
}
