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
import org.polarsys.eplmp.core.common.OAuthProvider;
import org.polarsys.eplmp.core.common.ProvidedAccount;
import org.polarsys.eplmp.core.exceptions.OAuthProviderNotFoundException;
import org.polarsys.eplmp.core.exceptions.ProvidedAccountNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

@Stateless(name = "OAuthProviderDAO")
public class OAuthProviderDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public OAuthProviderDAO() {
        mLocale = Locale.getDefault();
    }

    public OAuthProvider findProvider(int pId) throws OAuthProviderNotFoundException {
        OAuthProvider oAuthProvider = em.find(OAuthProvider.class, pId);
        if (oAuthProvider == null) {
            throw new OAuthProviderNotFoundException(mLocale, pId);
        }
        return oAuthProvider;
    }

    public OAuthProvider findProvider(Locale pLocale, int pId) throws OAuthProviderNotFoundException{
        mLocale = pLocale;
        return findProvider(pId);
    }

    public void createProvider(OAuthProvider oAuthProvider) {
        // try catch needed ?
        em.persist(oAuthProvider);
        em.flush();
    }

    public void removeProvider(int id) throws OAuthProviderNotFoundException {
        OAuthProvider provider = findProvider(id);
        em.remove(provider);
        em.flush();
    }

    public List<OAuthProvider> getProviders() {
        return em.createNamedQuery("OAuthProvider.findAll", OAuthProvider.class).getResultList();
    }


    public ProvidedAccount findProvidedAccount(int id, String sub) throws ProvidedAccountNotFoundException {
        try {
            return em.createNamedQuery("ProvidedAccount.getProvidedAccount", ProvidedAccount.class)
                    .setParameter("id", id)
                    .setParameter("sub", sub)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ProvidedAccountNotFoundException(Locale.getDefault(), sub);
        }
    }

    public ProvidedAccount findProvidedAccount(Account account) throws ProvidedAccountNotFoundException {
        try {
            return em.createNamedQuery("ProvidedAccount.getProvidedAccountFromAccount", ProvidedAccount.class)
                    .setParameter("account", account)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ProvidedAccountNotFoundException(Locale.getDefault(), account.getLogin());
        }
    }

    public boolean hasProvidedAccount(Account account) {
        try {
            findProvidedAccount(account);
            return true;
        } catch (ProvidedAccountNotFoundException e) {
            return false;
        }
    }

}
