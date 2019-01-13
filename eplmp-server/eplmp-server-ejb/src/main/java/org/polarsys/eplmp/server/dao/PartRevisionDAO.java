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
import org.polarsys.eplmp.core.exceptions.PartRevisionAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.PartRevisionNotFoundException;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.workflow.Workflow;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.List;


@RequestScoped
public class PartRevisionDAO {

    public static final String WORKSPACE_ID = "workspaceId";
    @Inject
    private EntityManager em;

    @Inject
    private ACLDAO aclDAO;

    @Inject
    private ConversionDAO conversionDAO;

    @Inject
    private SharedEntityDAO sharedEntityDAO;

    @Inject
    private WorkflowDAO workflowDAO;

    public PartRevisionDAO() {
    }

    public PartRevision loadPartR(PartRevisionKey pKey) {
        return em.find(PartRevision.class, pKey);
    }

    public void updateRevision(PartRevision pPartR) {
        em.merge(pPartR);
    }

    public void removeRevision(PartRevision pPartR) {
        sharedEntityDAO.deleteSharesForPart(pPartR);
        workflowDAO.removeWorkflowConstraints(pPartR);
        em.flush();
        conversionDAO.removePartRevisionConversions(pPartR);
        for (PartIteration partIteration : pPartR.getPartIterations()) {
            for (PartUsageLink partUsageLink : partIteration.getComponents()) {
                em.remove(partUsageLink);
            }
        }
        em.remove(pPartR);
    }

    public List<PartRevision> findAllCheckedOutPartRevisions(String pWorkspaceId) {
        TypedQuery<PartRevision> query = em.createQuery("SELECT DISTINCT p FROM PartRevision p WHERE p.checkOutUser is not null and p.partMaster.workspace.id = :workspaceId", PartRevision.class);
        query.setParameter(WORKSPACE_ID, pWorkspaceId);
        return query.getResultList();
    }

    public List<PartRevision> findCheckedOutPartRevisionsForUser(String pWorkspaceId, String pUserLogin) {
        TypedQuery<PartRevision> query = em.createQuery("SELECT DISTINCT p FROM PartRevision p WHERE p.checkOutUser is not null and p.partMaster.workspace.id = :workspaceId and p.checkOutUser.login = :userLogin", PartRevision.class);
        query.setParameter(WORKSPACE_ID, pWorkspaceId);
        query.setParameter("userLogin", pUserLogin);
        return query.getResultList();
    }

    public List<PartRevision> getPartRevisions(String pWorkspaceId, int pStart, int pMaxResults) {
        return em.createNamedQuery("PartRevision.findByWorkspace", PartRevision.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .setFirstResult(pStart)
                .setMaxResults(pMaxResults)
                .getResultList();
    }

    public List<PartRevision> getAllPartRevisions(String pWorkspaceId) {
        return em.createNamedQuery("PartRevision.findByWorkspace", PartRevision.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .getResultList();
    }

    public int getTotalNumberOfParts(String pWorkspaceId) {
        return ((Number) em.createNamedQuery("PartRevision.countByWorkspace")
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .getSingleResult()).intValue();
    }

    public int getPartRevisionCountFiltered(User caller, String workspaceId) {
        return ((Number) em.createNamedQuery("PartRevision.countByWorkspace.filterACLEntry")
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter("user", caller)
                .getSingleResult()).intValue();
    }

    public void createPartR(PartRevision partR) throws PartRevisionAlreadyExistsException, CreationException {

        try {
            if (partR.getWorkflow() != null) {
                workflowDAO.createWorkflow(partR.getWorkflow());
            }

            if (partR.getACL() != null) {
                aclDAO.createACL(partR.getACL());
            }

            //the EntityExistsException is thrown only when flush occurs
            em.persist(partR);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new PartRevisionAlreadyExistsException(partR);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException();
        }
    }

    public List<PartRevision> findPartsRevisionsWithReferenceOrNameLike(String pWorkspaceId, String reference, int maxResults) {
        return em.createNamedQuery("PartRevision.findByReferenceOrName", PartRevision.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
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
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PartRevision> findPartsWithAssignedTasksForGivenUser(String pWorkspaceId, String assignedUserLogin) {
        return em.createNamedQuery("PartRevision.findWithAssignedTasksForUser", PartRevision.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .setParameter("login", assignedUserLogin)
                .getResultList();
    }

    public List<PartRevision> findPartsWithOpenedTasksForGivenUser(String pWorkspaceId, String assignedUserLogin) {
        return em.createNamedQuery("PartRevision.findWithOpenedTasksForUser", PartRevision.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .setParameter("login", assignedUserLogin)
                .getResultList();
    }

    public void removePartRevisionEffectivity(PartRevision pPartRevision, Effectivity pEffectivity) {
        pPartRevision.removeEffectivity(pEffectivity);
        em.merge(pPartRevision);
        em.flush();
    }
}
