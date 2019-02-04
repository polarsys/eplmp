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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.product.Import;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;


@RequestScoped
public class ImportDAO {

    @Inject
    private EntityManager em;

    public ImportDAO() {
    }

    public void createImport(Import pImportToPersist) throws CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pImportToPersist);
            em.flush();
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException();
        }
    }

    public Import findImport(User user, String id) {
        TypedQuery<Import> query = em.createQuery("SELECT DISTINCT i FROM Import i WHERE i.id = :id AND i.user = :user", Import.class);
        query.setParameter("user", user);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Import> findImports(User user, String filename) {
        TypedQuery<Import> query = em.createQuery("SELECT DISTINCT i FROM Import i WHERE i.user = :user AND i.fileName = :filename", Import.class);
        query.setParameter("user", user);
        query.setParameter("filename", filename);
        return query.getResultList();
    }

    public void deleteImport(Import importToDelete) {
        em.remove(importToDelete);
        em.flush();
    }

}
