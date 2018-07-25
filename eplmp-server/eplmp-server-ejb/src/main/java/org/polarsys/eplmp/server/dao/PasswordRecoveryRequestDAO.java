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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;

@Stateless(name = "PasswordRecoveryRequestDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PasswordRecoveryRequestDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public PasswordRecoveryRequestDAO() {
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

    public PasswordRecoveryRequest loadPasswordRecoveryRequest(Locale pLocale, String recoveryRequestUUID) throws PasswordRecoveryRequestNotFoundException {
        this.mLocale = pLocale;
        return loadPasswordRecoveryRequest(recoveryRequestUUID);
    }

    public void removePasswordRecoveryRequest(PasswordRecoveryRequest pPasswdRRUuid) {
        em.remove(pPasswdRRUuid);
    }

}
