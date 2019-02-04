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

import org.polarsys.eplmp.core.change.ChangeOrder;
import org.polarsys.eplmp.core.change.ChangeRequest;
import org.polarsys.eplmp.core.change.Milestone;
import org.polarsys.eplmp.core.exceptions.MilestoneAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.MilestoneNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;


@RequestScoped
public class MilestoneDAO {

    public static final String WORKSPACE_ID = "workspaceId";
    public static final String MILESTONE_ID = "milestoneId";

    @Inject
    private EntityManager em;

    @Inject
    private ACLDAO aclDAO;

    public MilestoneDAO() {
    }


    public List<Milestone> findAllMilestone(String pWorkspaceId) {
        return em.createNamedQuery("Milestone.findMilestonesByWorkspace", Milestone.class)
                                        .setParameter(WORKSPACE_ID, pWorkspaceId)
                                        .getResultList();
    }
    
    public Milestone loadMilestone(int pId) throws MilestoneNotFoundException {
        Milestone milestone = em.find(Milestone.class, pId);
        if (milestone == null) {
            throw new MilestoneNotFoundException(pId);
        } else {
            return milestone;
        }
    }

    public Milestone loadMilestone(String pTitle, String pWorkspace) throws MilestoneNotFoundException {
        Milestone milestone = em.createNamedQuery("Milestone.findMilestonesByTitleAndWorkspace", Milestone.class)
                .setParameter("title", pTitle)
                .setParameter(WORKSPACE_ID, pWorkspace)
                .getSingleResult();
        if (milestone == null) {
            throw new MilestoneNotFoundException(pTitle);
        } else {
            return milestone;
        }
    }

    public void createMilestone(Milestone pMilestone) throws MilestoneAlreadyExistsException {
        if(!this.checkTitleUniqueness(pMilestone.getTitle(),pMilestone.getWorkspace().getId()))
            throw new MilestoneAlreadyExistsException(pMilestone.getTitle(), null);

        if(pMilestone.getACL()!=null) {
            aclDAO.createACL(pMilestone.getACL());
        }

        em.persist(pMilestone);
        em.flush();
    }

    public void deleteMilestone(Milestone pMilestone) {
        em.remove(pMilestone);
        em.flush();
    }

    public List<ChangeRequest> getAllRequests(int pId,String pWorkspace){
        try{
            return em.createNamedQuery("ChangeRequest.getRequestByMilestonesAndWorkspace",ChangeRequest.class)
                    .setParameter(MILESTONE_ID, pId)
                    .setParameter(WORKSPACE_ID, pWorkspace)
                    .getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public List<ChangeOrder> getAllOrders(int pId,String pWorkspace){
        try{
            return em.createNamedQuery("ChangeOrder.getOrderByMilestonesAndWorkspace",ChangeOrder.class)
                    .setParameter(MILESTONE_ID, pId)
                    .setParameter(WORKSPACE_ID, pWorkspace)
                    .getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public int getNumberOfRequests(int pId,String pWorkspace){
        try{
            return ((Number)em.createNamedQuery("ChangeRequest.countRequestByMilestonesAndWorkspace")
                    .setParameter(MILESTONE_ID, pId)
                    .setParameter(WORKSPACE_ID, pWorkspace)
                    .getSingleResult()).intValue();
        }catch(Exception e){
            return 0;
        }
    }

    public int getNumberOfOrders(int pId,String pWorkspace){
        try{
            return ((Number)em.createNamedQuery("ChangeOrder.countOrderByMilestonesAndWorkspace")
                    .setParameter(MILESTONE_ID, pId)
                    .setParameter(WORKSPACE_ID, pWorkspace)
                    .getSingleResult()).intValue();
        }catch(Exception e){
            return 0;
        }
    }
    
    private boolean checkTitleUniqueness(String pTitle,String pWorkspace){
        try{
            return em.createNamedQuery("Milestone.findMilestonesByTitleAndWorkspace")
                    .setParameter("title", pTitle)
                    .setParameter(WORKSPACE_ID, pWorkspace)
                    .getResultList().isEmpty();
        }catch (NoResultException e){
            return true;
        }
    }
}
