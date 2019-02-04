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
package org.polarsys.eplmp.server.dao;


import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.SharedEntityNotFoundException;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.sharing.SharedDocument;
import org.polarsys.eplmp.core.sharing.SharedEntity;
import org.polarsys.eplmp.core.sharing.SharedPart;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;


/**
 * @author Morgan Guimard
 */

@RequestScoped
public class SharedEntityDAO {

    @Inject
    private EntityManager em;

    public SharedEntityDAO() {
    }

    public boolean isSharedDocument(String pUuid){
        return em.find(SharedDocument.class, pUuid) != null;
    }

    public boolean isSharedPart(String pUuid){
        return em.find(SharedPart.class, pUuid) != null;
    }

    public SharedDocument loadSharedDocument(String pUuid) throws SharedEntityNotFoundException {

        SharedDocument sharedDocument = em.find(SharedDocument.class, pUuid);
        if (sharedDocument == null) {
            throw new SharedEntityNotFoundException(pUuid);
        } else {
            return sharedDocument;
        }

    }

    public SharedPart loadSharedPart(String pUuid) throws SharedEntityNotFoundException {

        SharedPart sharedPart = em.find(SharedPart.class, pUuid);
        if (sharedPart == null) {
            throw new SharedEntityNotFoundException(pUuid);
        } else {
            return sharedPart;
        }
    }

    public void createSharedDocument(SharedDocument pSharedDocument) {
        em.persist(pSharedDocument);
        em.flush();
    }

    public void createSharedPart(SharedPart pSharedPart) {
        em.persist(pSharedPart);
        em.flush();
    }

    public void deleteSharedDocument(SharedDocument pSharedDocument){
        em.remove(pSharedDocument);
        em.flush();
    }

    public void deleteSharedPart(SharedPart pSharedPart){
        em.remove(pSharedPart);
        em.flush();
    }

    public void deleteSharesForDocument(DocumentRevision pDocR) {
        TypedQuery<SharedDocument> query = em.createNamedQuery("SharedDocument.deleteSharesForGivenDocument", SharedDocument.class);
        query.setParameter("pDocR", pDocR).executeUpdate();
    }

    public void deleteSharesForPart(PartRevision pPartR) {
        TypedQuery<SharedPart> query = em.createNamedQuery("SharedPart.deleteSharesForGivenPart", SharedPart.class);
        query.setParameter("pPartR", pPartR).executeUpdate();
    }

    public SharedEntity loadSharedEntity(String pUuid) throws SharedEntityNotFoundException {
        TypedQuery<SharedEntity> query = em.createNamedQuery("SharedEntity.findSharedEntityForGivenUuid", SharedEntity.class);
        try {
            return query.setParameter("pUuid", pUuid).getSingleResult();
        }catch(NoResultException ex){
            throw new SharedEntityNotFoundException(pUuid);
        }

    }

    public void deleteSharedEntity(SharedEntity pSharedEntity) {
        try {
            SharedEntity sharedEntity = loadSharedEntity(pSharedEntity.getUuid());
            if(pSharedEntity instanceof SharedDocument){
                deleteSharedDocument((SharedDocument) sharedEntity);
            }else if(pSharedEntity instanceof SharedPart){
                deleteSharedPart((SharedPart) sharedEntity);
            }
        } catch (SharedEntityNotFoundException e) {

        }

    }
}
