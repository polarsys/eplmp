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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.exceptions.SharedEntityNotFoundException;
import org.polarsys.eplmp.core.services.IShareManagerLocal;
import org.polarsys.eplmp.core.sharing.SharedEntity;
import org.polarsys.eplmp.server.dao.SharedEntityDAO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

/**
 * @author Morgan Guimard
 */

@Local(IShareManagerLocal.class)
@Stateless(name = "ShareManagerBean")
public class ShareManagerBean implements IShareManagerLocal {

    @Inject
    private SharedEntityDAO sharedEntityDAO;

    @Override
    public SharedEntity findSharedEntityForGivenUUID(String pUuid) throws SharedEntityNotFoundException {
        return sharedEntityDAO.loadSharedEntity(pUuid);
    }

    @Override
    public void deleteSharedEntityIfExpired(SharedEntity pSharedEntity) {
        // insure the entity is really expired
        if(pSharedEntity.getExpireDate() != null){
            Date now = new Date();
            if(pSharedEntity.getExpireDate().getTime() < now.getTime()){
                sharedEntityDAO.deleteSharedEntity(pSharedEntity);
            }

        }
    }
}
