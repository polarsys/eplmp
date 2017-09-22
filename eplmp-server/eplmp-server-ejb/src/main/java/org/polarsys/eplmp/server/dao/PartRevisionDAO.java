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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.PartRevisionAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.PartRevisionNotFoundException;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.workflow.Workflow;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

public class PartRevisionDAO {

    private EntityManager em;
    private Locale mLocale;

    public PartRevisionDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }
    public PartRevisionDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }


    public PartRevision loadPartR(PartRevisionKey pKey) throws PartRevisionNotFoundException {
        PartRevision partR = em.find(PartRevision.class, pKey);
        if (partR == null) {
            throw new PartRevisionNotFoundException(mLocale, pKey);
        } else {
            return partR;
        }
    }

    public void updateRevision(PartRevision pPartR) {
        em.merge(pPartR);
    }

    public void removeRevision(PartRevision pPartR) {
        new SharedEntityDAO(em).deleteSharesForPart(pPartR);
        new WorkflowDAO(em).removeWorkflowConstraints(pPartR);
        em.flush();
        new ConversionDAO(em).removePartRevisionConversions(pPartR);
        for(PartIteration partIteration:pPartR.getPartIterations()){
            for(PartUsageLink partUsageLink:partIteration.getComponents()){
                em.remove(partUsageLink);
            }
        }
        em.remove(pPartR);
    }

    public List<PartRevision> findAllCheckedOutPartRevisions(String pWorkspaceId) {
        TypedQuery<PartRevision> query = em.createQuery("SELECT DISTINCT p FROM PartRevision p WHERE p.checkOutUser is not null and p.partMaster.workspace.id = :workspaceId", PartRevision.class);
        query.setParameter("workspaceId", pWorkspaceId);
        return query.getResultList();
    }

    public List<PartRevision> findCheckedOutPartRevisionsForUser(String pWorkspaceId, String pUserLogin) {
        TypedQuery<PartRevision> query = em.createQuery("SELECT DISTINCT p FROM PartRevision p WHERE p.checkOutUser is not null and p.partMaster.workspace.id = :workspaceId and p.checkOutUser.login = :userLogin", PartRevision.class);
        query.setParameter("workspaceId", pWorkspaceId);
        query.setParameter("userLogin", pUserLogin);
        return query.getResultList();
    }

    public List<PartRevision> getPartRevisions(String pWorkspaceId, int pStart, int pMaxResults) {
        return em.createNamedQuery("PartRevision.findByWorkspace", PartRevision.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setFirstResult(pStart)
                .setMaxResults(pMaxResults)
                .getResultList();
    }

    public List<PartRevision> getAllPartRevisions(String pWorkspaceId) {
        return em.createNamedQuery("PartRevision.findByWorkspace", PartRevision.class)
                .setParameter("workspaceId", pWorkspaceId)
                .getResultList();
    }

    public int getTotalNumberOfParts(String pWorkspaceId) {
        return ((Number)em.createNamedQuery("PartRevision.countByWorkspace")
                .setParameter("workspaceId", pWorkspaceId)
                .getSingleResult()).intValue();
    }

    public int getPartRevisionCountFiltered(User caller, String workspaceId) {
        return ((Number) em.createNamedQuery("PartRevision.countByWorkspace.filterACLEntry")
                .setParameter("workspaceId", workspaceId)
                .setParameter("user", caller)
                .getSingleResult()).intValue();
    }

    public void createPartR(PartRevision partR) throws PartRevisionAlreadyExistsException, CreationException {

        try {
            if (partR.getWorkflow() != null) {
                WorkflowDAO workflowDAO = new WorkflowDAO(em);
                workflowDAO.createWorkflow(partR.getWorkflow());
            }

            if (partR.getACL() != null) {
                ACLDAO aclDAO = new ACLDAO(em);
                aclDAO.createACL(partR.getACL());
            }

            //the EntityExistsException is thrown only when flush occurs
            em.persist(partR);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new PartRevisionAlreadyExistsException(mLocale, partR);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }

    }


    public List<PartRevision> findPartsRevisionsWithReferenceOrNameLike(String pWorkspaceId, String reference, int maxResults) {
        return em.createNamedQuery("PartRevision.findByReferenceOrName",PartRevision.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setParameter("partNumber", "%" + reference + "%")
                .setParameter("partName", "%" + reference + "%")
                .setMaxResults(maxResults)
                .getResultList();
    }

    public boolean isCheckedOutIteration(PartIterationKey partIKey) throws PartRevisionNotFoundException {
        PartRevision partR = loadPartR(partIKey.getPartRevision());
        return partR.isCheckedOut() && (partIKey.getIteration() == partR.getLastIterationNumber());
    }

    public List<PartRevision> findPartByTag(Tag tag) {
        TypedQuery<PartRevision> query = em.createQuery("SELECT DISTINCT d FROM PartRevision d WHERE :tag MEMBER OF d.tags", PartRevision.class);
        query.setParameter("tag", tag);
        return query.getResultList();
    }

    public PartRevision getWorkflowHolder(Workflow workflow) {
        try {
            return em.createNamedQuery("PartRevision.findByWorkflow", PartRevision.class).
                    setParameter("workflow", workflow).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<PartRevision> findPartsWithAssignedTasksForGivenUser(String pWorkspaceId, String assignedUserLogin) {
        return em.createNamedQuery("PartRevision.findWithAssignedTasksForUser", PartRevision.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setParameter("login", assignedUserLogin)
                .getResultList();
    }

    public List<PartRevision> findPartsWithOpenedTasksForGivenUser(String pWorkspaceId, String assignedUserLogin) {
        return em.createNamedQuery("PartRevision.findWithOpenedTasksForUser", PartRevision.class)
                .setParameter("workspaceId", pWorkspaceId)
                .setParameter("login", assignedUserLogin)
                .getResultList();
    }

    public void removePartRevisionEffectivity(PartRevision pPartRevision, Effectivity pEffectivity) {
        pPartRevision.removeEffectivity(pEffectivity);
        em.merge(pPartRevision);
        em.flush();
    }
}
