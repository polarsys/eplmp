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

package org.polarsys.eplmp.server.dao;

import org.polarsys.eplmp.core.exceptions.PasswordRecoveryRequestNotFoundException;
import org.polarsys.eplmp.core.security.PasswordRecoveryRequest;

import javax.persistence.EntityManager;
import java.util.Locale;

public class PasswordRecoveryRequestDAO {

    private EntityManager em;
    private Locale mLocale;

    public PasswordRecoveryRequestDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public PasswordRecoveryRequestDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }

    public PasswordRecoveryRequest loadPasswordRecoveryRequest(String recoveryRequestUUID) throws PasswordRecoveryRequestNotFoundException {
        PasswordRecoveryRequest recoveryRequest = em.find(PasswordRecoveryRequest.class, recoveryRequestUUID);
        if (recoveryRequest == null) {
            throw new PasswordRecoveryRequestNotFoundException(mLocale, recoveryRequestUUID);
        } else {
            return recoveryRequest;
        }
    }


    public void removePasswordRecoveryRequest(PasswordRecoveryRequest pPasswdRRUuid) {
        em.remove(pPasswdRRUuid);
    }

}
