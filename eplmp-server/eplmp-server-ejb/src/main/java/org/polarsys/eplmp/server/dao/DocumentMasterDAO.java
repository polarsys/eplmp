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

import org.polarsys.eplmp.core.document.DocumentMaster;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.DocumentMasterAlreadyExistsException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class DocumentMasterDAO {

    private static final Logger LOGGER = Logger.getLogger(DocumentMasterDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private DocumentRevisionDAO documentRevisionDAO;

    public DocumentMasterDAO() {
    }

    public void createDocM(DocumentMaster pDocumentMaster) throws DocumentMasterAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pDocumentMaster);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINER,null,pEEEx);
            throw new DocumentMasterAlreadyExistsException(pDocumentMaster);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            LOGGER.log(Level.FINER,null,pPEx);
            throw new CreationException("");
        }
    }

    public void removeDocM(DocumentMaster pDocM) {
        List<DocumentRevision> docRs = new ArrayList<>(pDocM.getDocumentRevisions());
        for(DocumentRevision documentRevision:docRs){
            documentRevisionDAO.removeRevision(documentRevision);
        }
        em.remove(pDocM);
    }

    public List<DocumentMaster> getPaginatedByWorkspace(String workspaceId, int limit, int offset) {
        return em.createNamedQuery("DocumentMaster.findByWorkspace",DocumentMaster.class)
                .setParameter("workspaceId",workspaceId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long getCountByWorkspace(String workspaceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DocumentMaster> dm = countQuery.from(DocumentMaster.class);
        countQuery.select(cb.count(dm)).where(cb.equal(dm.get("workspace").get("id"), workspaceId));
        return em.createQuery(countQuery).getSingleResult();
    }
}
