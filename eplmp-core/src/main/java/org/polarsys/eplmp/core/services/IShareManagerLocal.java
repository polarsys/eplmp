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

import org.polarsys.eplmp.core.exceptions.SharedEntityNotFoundException;
import org.polarsys.eplmp.core.sharing.SharedEntity;

/**
 *
 * @author Morgan Guimard
 */
public interface IShareManagerLocal {

    SharedEntity findSharedEntityForGivenUUID(String pUuid) throws SharedEntityNotFoundException;

    void deleteSharedEntityIfExpired(SharedEntity pSharedEntity);

}
