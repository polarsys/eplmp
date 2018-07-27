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
import org.polarsys.eplmp.core.exceptions.PartMasterAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.PartMasterNotFoundException;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartMasterKey;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class PartMasterDAO {

    public static final String WORKSPACE_ID = "workspaceId";
    @PersistenceContext
    private EntityManager em;

    @Inject
    private PartRevisionDAO partRevisionDAO;

    @Inject
    private WorkflowDAO workflowDAO;

    private Locale mLocale;
    private static final Logger LOGGER = Logger.getLogger(PartMasterDAO.class.getName());

    public PartMasterDAO() {
        mLocale = Locale.getDefault();
    }

    public PartMaster loadPartM(PartMasterKey pKey) throws PartMasterNotFoundException {
        PartMaster partM = em.find(PartMaster.class, pKey);
        if (partM == null) {
            throw new PartMasterNotFoundException(mLocale, pKey.getNumber());
        } else {
            return partM;
        }
    }

    public PartMaster loadPartM(Locale pLocale, PartMasterKey pKey) throws PartMasterNotFoundException {
        mLocale = pLocale;
        return loadPartM(pKey);
    }

    public PartMaster getPartMRef(PartMasterKey pKey) throws PartMasterNotFoundException {
        try {
            return em.getReference(PartMaster.class, pKey);
        } catch (EntityNotFoundException pENFEx) {
            LOGGER.log(Level.FINEST,null,pENFEx);
            throw new PartMasterNotFoundException(mLocale, pKey.getNumber());
        }
    }

    public PartMaster getPartMRef(Locale pLocale, PartMasterKey pKey) throws PartMasterNotFoundException {
        mLocale = pLocale;
        return getPartMRef(pKey);
    }

    public void createPartM(PartMaster pPartM) throws PartMasterAlreadyExistsException, CreationException {
        try {
            PartRevision firstRev = pPartM.getLastRevision();
            if(firstRev!=null && firstRev.getWorkflow()!=null) {
                workflowDAO.createWorkflow(firstRev.getWorkflow());
            }
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pPartM);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINEST,null,pEEEx);
            throw new PartMasterAlreadyExistsException(mLocale, pPartM);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            LOGGER.log(Level.FINEST,null,pPEx);
            throw new CreationException(mLocale);
        }
    }

    public void createPartM(Locale pLocale, PartMaster pPartM) throws PartMasterAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createPartM(pPartM);
    }

    public void removePartM(PartMaster pPartM) {
        for(PartRevision partRevision:pPartM.getPartRevisions()){
            partRevisionDAO.removeRevision(partRevision);
        }
        em.remove(pPartM);
    }

    public void removePartM(Locale pLocale, PartMaster pPartM) {
        mLocale = pLocale;
        removePartM(pPartM);
    }

    public List<PartMaster> findPartMasters(String workspaceId, String partNumber, String partName, int maxResults){
        return em.createNamedQuery("PartMaster.findByNameOrNumber", PartMaster.class)
            .setParameter("partNumber", partNumber)
            .setParameter("partName", partName)
            .setParameter(WORKSPACE_ID, workspaceId)
            .setMaxResults(maxResults)
            .getResultList();
    }

    public String findLatestPartMId(String pWorkspaceId, String pType) {
        String partMId;
        TypedQuery<String> query = em.createQuery("SELECT m.number FROM PartMaster m "
                + "WHERE m.workspace.id = :workspaceId "
                + "AND m.type = :type "
                + "AND m.creationDate = ("
                + "SELECT MAX(m2.creationDate) FROM PartMaster m2 "
                + "WHERE m2.workspace.id = :workspaceId "
                + "AND m2.type = :type "
                + ")", String.class);
        query.setParameter(WORKSPACE_ID, pWorkspaceId);
        query.setParameter("type", pType);
        partMId = query.getSingleResult();
        return partMId;
    }

    public List<PartMaster> getPartMasters(String pWorkspaceId, int pStart, int pMaxResults) {
        return em.createNamedQuery("PartMaster.findByWorkspace", PartMaster.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .setFirstResult(pStart)
                .setMaxResults(pMaxResults)
                .getResultList();
    }

    public long getDiskUsageForPartsInWorkspace(String pWorkspaceId) {
        Number result = (Number)em.createNamedQuery("BinaryResource.diskUsageInPath")
                .setParameter("path", pWorkspaceId+"/parts/%")
                .getSingleResult();

        return result != null ? result.longValue() : 0L;
    }

    public long getDiskUsageForPartTemplatesInWorkspace(String pWorkspaceId) {
        Number result = (Number)em.createNamedQuery("BinaryResource.diskUsageInPath")
                .setParameter("path", pWorkspaceId+"/part-templates/%")
                .getSingleResult();

        return result != null ? result.longValue() : 0L;
    }

    public List<PartMaster> getPaginatedByWorkspace(String workspaceId, int limit, int offset) {
        return em.createNamedQuery("PartMaster.findByWorkspace",PartMaster.class)
                .setParameter(WORKSPACE_ID,workspaceId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long getCountByWorkspace(String workspaceId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(PartMaster.class)));
        return em.createQuery(countQuery).getSingleResult();
    }
}
