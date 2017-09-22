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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.GCMAccountAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.GCMAccountNotFoundException;
import org.polarsys.eplmp.core.gcm.GCMAccount;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Locale;

public class GCMAccountDAO {

    private EntityManager em;
    private Locale mLocale;

    public GCMAccountDAO(Locale pLocale, EntityManager pEM) {
        mLocale=pLocale;
        em=pEM;
    }

    public GCMAccountDAO(EntityManager pEM) {
        mLocale=Locale.getDefault();
        em=pEM;
    }

    public GCMAccount loadGCMAccount(Account account) throws GCMAccountNotFoundException {
        GCMAccount gcmAccount = em.find(GCMAccount.class, account.getLogin());
        if(gcmAccount == null){
            throw new GCMAccountNotFoundException(mLocale,account.getLogin());
        }
        return gcmAccount;
    }

    public void createGCMAccount(GCMAccount gcmAccount) throws GCMAccountAlreadyExistsException, CreationException {
        try{
            //the EntityExistsException is thrown only when flush occurs
            em.persist(gcmAccount);
            em.flush();
        }catch(EntityExistsException pEEEx){
            throw new GCMAccountAlreadyExistsException(mLocale, gcmAccount.getAccount().getLogin());
        }catch(PersistenceException pPEx){
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void deleteGCMAccount(GCMAccount gcmAccount){
        em.remove(gcmAccount);
        em.flush();
    }

}
