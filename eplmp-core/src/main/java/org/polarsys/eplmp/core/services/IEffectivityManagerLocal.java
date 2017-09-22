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

import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.*;

import java.util.Date;

/**
 * @author Frédéric Maury
 */
public interface IEffectivityManagerLocal {

    SerialNumberBasedEffectivity createSerialNumberBasedEffectivity(String workspaceId, String partNumber, String version, String pName, String pDescription, String pConfigurationItemId, String pStartNumber, String pEndNumber) throws EffectivityAlreadyExistsException, CreationException, ConfigurationItemNotFoundException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException, UserNotActiveException;

    DateBasedEffectivity createDateBasedEffectivity(String workspaceId, String partNumber, String version, String pName, String pDescription, String pConfigurationItemId, Date pStartDate, Date pEndDate) throws EffectivityAlreadyExistsException, CreationException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException, UserNotActiveException, ConfigurationItemNotFoundException;

    LotBasedEffectivity createLotBasedEffectivity(String workspaceId, String partNumber, String version, String pName, String pDescription,  String pConfigurationItemId, String pStartLotId, String pEndLotId) throws EffectivityAlreadyExistsException, CreationException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, ConfigurationItemNotFoundException, PartRevisionNotFoundException, UserNotActiveException;

    Effectivity getEffectivity(String workspaceId, int pId) throws EffectivityNotFoundException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;

    Effectivity updateEffectivity(String workspaceId, int pId, String pName, String pDescription) throws EffectivityNotFoundException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException;

    SerialNumberBasedEffectivity updateSerialNumberBasedEffectivity(String workspaceId, int pId, String pName, String pDescription, String pStartNumber, String pEndNumber) throws EffectivityNotFoundException, UpdateException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, CreationException;

    DateBasedEffectivity updateDateBasedEffectivity(String workspaceId, int pId, String pName, String pDescription, Date pStartDate, Date pEndDate) throws EffectivityNotFoundException, UpdateException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, CreationException;

    LotBasedEffectivity updateLotBasedEffectivity(String workspaceId, int pId, String pName, String pDescription, String pStartLotId, String pEndLotId) throws UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, CreationException, EffectivityNotFoundException;

    void deleteEffectivity(String workspaceId, String partNumber, String version, int pId) throws EffectivityNotFoundException, UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, PartRevisionNotFoundException, UserNotActiveException;
}
