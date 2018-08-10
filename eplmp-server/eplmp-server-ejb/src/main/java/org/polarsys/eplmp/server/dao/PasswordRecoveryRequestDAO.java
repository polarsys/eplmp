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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;


@RequestScoped
public class PasswordRecoveryRequestDAO {

    @Inject
    private EntityManager em;

    public PasswordRecoveryRequestDAO() {
    }

    public PasswordRecoveryRequest loadPasswordRecoveryRequest(String recoveryRequestUUID) throws PasswordRecoveryRequestNotFoundException {
        PasswordRecoveryRequest recoveryRequest = em.find(PasswordRecoveryRequest.class, recoveryRequestUUID);
        if (recoveryRequest == null) {
            throw new PasswordRecoveryRequestNotFoundException(recoveryRequestUUID);
        } else {
            return recoveryRequest;
        }
    }

    public void removePasswordRecoveryRequest(PasswordRecoveryRequest pPasswdRRUuid) {
        em.remove(pPasswdRRUuid);
    }

}
