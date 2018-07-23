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
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.AccountAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.security.Credential;
import org.polarsys.eplmp.core.security.UserGroupMapping;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Stateless(name = "AccountDAO")
public class AccountDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public AccountDAO() {
        mLocale=Locale.getDefault();
    }

    public void createAccount(Account pAccount, String pPassword, String pAlgorithm) throws AccountAlreadyExistsException, CreationException {
        try{
            //the EntityExistsException is thrown only when flush occurs 
            em.persist(pAccount);
            em.flush();
            Credential credential = Credential.createCredential(pAccount.getLogin(), pPassword, pAlgorithm);
            em.persist(credential);
            em.persist(new UserGroupMapping(pAccount.getLogin()));            
        }catch(EntityExistsException pEEEx){
            throw new AccountAlreadyExistsException(mLocale, pAccount.getLogin());
        }catch(PersistenceException pPEx){
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createAccount(Locale pLocale, Account pAccount, String pPassword, String pAlgorithm) throws AccountAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createAccount(pAccount, pPassword, pAlgorithm);
    }
    
    public void updateAccount(Account pAccount, String pPassword, String pAlgorithm){
        em.merge(pAccount);
        if(pPassword!=null){
            updateCredential(pAccount.getLogin(),pPassword, pAlgorithm);
        }
    }
    
    public void updateCredential(String pLogin, String pPassword, String pAlgorithm){
        Credential credential = Credential.createCredential(pLogin,pPassword, pAlgorithm);
        em.merge(credential);
    }
    
    public Account loadAccount(String pLogin) throws AccountNotFoundException {
        Account account = em.find(Account.class,pLogin);
        if (account == null) {
            throw new AccountNotFoundException(mLocale, pLogin);
        } else {
            return account;
        }
    }

    public Account loadAccount(Locale pLocale, String pLogin) throws AccountNotFoundException {
        mLocale = pLocale;
        return loadAccount(pLogin);
    }
    
    public Workspace[] getAdministratedWorkspaces(Account pAdmin) {
        Workspace[] workspaces;
        TypedQuery<Workspace> query = em.createQuery("SELECT DISTINCT w FROM Workspace w WHERE w.admin = :admin", Workspace.class);
        List<Workspace> listWorkspaces = query.setParameter("admin",pAdmin).getResultList();
        workspaces = new Workspace[listWorkspaces.size()];
        for(int i=0;i<listWorkspaces.size();i++) {
            workspaces[i] = listWorkspaces.get(i);
        }
        
        return workspaces;    
    }

    public Workspace[] getAllWorkspaces() {
        Workspace[] workspaces;
        TypedQuery<Workspace> query = em.createQuery("SELECT DISTINCT w FROM Workspace w", Workspace.class);
        List<Workspace> listWorkspaces = query.getResultList();
        workspaces = new Workspace[listWorkspaces.size()];
        for(int i=0;i<listWorkspaces.size();i++) {
            workspaces[i] = listWorkspaces.get(i);
        }

        return workspaces;
    }

    public List<Account> getAccounts(){
        TypedQuery<Account> query = em.createQuery("SELECT DISTINCT a FROM Account a", Account.class);
        return query.getResultList();
    }


    public boolean authenticate(String login, String password, String pAlgorithm){
        Credential credential = em.find(Credential.class, login);
        return credential != null && credential.authenticate(password, pAlgorithm);
    }
}
