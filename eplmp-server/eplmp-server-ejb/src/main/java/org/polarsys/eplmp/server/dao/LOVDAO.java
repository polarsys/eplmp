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

import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.ListOfValuesAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.ListOfValuesNotFoundException;
import org.polarsys.eplmp.core.meta.ListOfValues;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;

import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class LOVDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public LOVDAO() {
        mLocale = Locale.getDefault();
    }
    
    public ListOfValues loadLOV(ListOfValuesKey pLovKey) throws ListOfValuesNotFoundException {
        ListOfValues lov=em.find(ListOfValues.class,pLovKey);
        if (lov == null) {
            throw new ListOfValuesNotFoundException(mLocale, pLovKey.getName());
        } else {
            return lov;
        }
    }

    public ListOfValues loadLOV(Locale pLocale, ListOfValuesKey pLovKey) throws ListOfValuesNotFoundException{
        mLocale = pLocale;
        return loadLOV(pLovKey);
    }

    public List<ListOfValues> loadLOVList(String pWorkspaceId){
        TypedQuery<ListOfValues> query = em.createQuery("SELECT DISTINCT l FROM ListOfValues l WHERE l.workspaceId = :workspaceId", ListOfValues.class);
        query.setParameter("workspaceId", pWorkspaceId);
        return query.getResultList();
    }

    public void createLOV(ListOfValues pLov) throws CreationException, ListOfValuesAlreadyExistsException {
        try{
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pLov);
            em.flush();
        }catch(EntityExistsException pEEEx){
            throw new ListOfValuesAlreadyExistsException(mLocale, pLov);
        }catch(PersistenceException pPEx){
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createLOV(Locale pLocale, ListOfValues pLov) throws CreationException, ListOfValuesAlreadyExistsException{
        mLocale = pLocale;
        createLOV(pLov);
    }

    public void deleteLOV(ListOfValues pLov) {
        em.remove(pLov);
        em.flush();
    }

    public ListOfValues updateLOV(ListOfValues pLov){
        return em.merge(pLov);
    }
}
