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

import org.polarsys.eplmp.core.change.ChangeOrder;
import org.polarsys.eplmp.core.change.ChangeRequest;
import org.polarsys.eplmp.core.change.Milestone;
import org.polarsys.eplmp.core.exceptions.MilestoneAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.MilestoneNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Locale;

public class MilestoneDAO {

    private EntityManager em;
    private Locale mLocale;

    public MilestoneDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public MilestoneDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }


    public List<Milestone> findAllMilestone(String pWorkspaceId) {
        List<Milestone> milestones = em.createNamedQuery("Milestone.findMilestonesByWorkspace", Milestone.class)
                                        .setParameter("workspaceId", pWorkspaceId)
                                        .getResultList();
        return milestones;
    }
    
    public Milestone loadMilestone(int pId) throws MilestoneNotFoundException {
        Milestone milestone = em.find(Milestone.class, pId);
        if (milestone == null) {
            throw new MilestoneNotFoundException(mLocale, pId);
        } else {
            return milestone;
        }
    }

    public Milestone loadMilestone(String pTitle, String pWorkspace) throws MilestoneNotFoundException {
        Milestone milestone = em.createNamedQuery("Milestone.findMilestonesByTitleAndWorkspace", Milestone.class)
                .setParameter("title", pTitle)
                .setParameter("workspaceId", pWorkspace)
                .getSingleResult();
        if (milestone == null) {
            throw new MilestoneNotFoundException(mLocale, pTitle);
        } else {
            return milestone;
        }
    }

    public void createMilestone(Milestone pMilestone) throws MilestoneAlreadyExistsException {
        if(!this.checkTitleUniqueness(pMilestone.getTitle(),pMilestone.getWorkspace().getId()))
            throw new MilestoneAlreadyExistsException(mLocale,pMilestone.getTitle());

        if(pMilestone.getACL()!=null){
            ACLDAO aclDAO = new ACLDAO(em);
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
                    .setParameter("milestoneId", pId)
                    .setParameter("workspaceId", pWorkspace)
                    .getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public List<ChangeOrder> getAllOrders(int pId,String pWorkspace){
        try{
            return em.createNamedQuery("ChangeOrder.getOrderByMilestonesAndWorkspace",ChangeOrder.class)
                    .setParameter("milestoneId", pId)
                    .setParameter("workspaceId", pWorkspace)
                    .getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public int getNumberOfRequests(int pId,String pWorkspace){
        try{
            return ((Number)em.createNamedQuery("ChangeRequest.countRequestByMilestonesAndWorkspace")
                    .setParameter("milestoneId", pId)
                    .setParameter("workspaceId", pWorkspace)
                    .getSingleResult()).intValue();
        }catch(Exception e){
            return 0;
        }
    }

    public int getNumberOfOrders(int pId,String pWorkspace){
        try{
            return ((Number)em.createNamedQuery("ChangeOrder.countOrderByMilestonesAndWorkspace")
                    .setParameter("milestoneId", pId)
                    .setParameter("workspaceId", pWorkspace)
                    .getSingleResult()).intValue();
        }catch(Exception e){
            return 0;
        }
    }
    
    private boolean checkTitleUniqueness(String pTitle,String pWorkspace){
        try{
            return em.createNamedQuery("Milestone.findMilestonesByTitleAndWorkspace")
                    .setParameter("title", pTitle)
                    .setParameter("workspaceId", pWorkspace)
                    .getResultList().isEmpty();
        }catch (NoResultException e){
            return true;
        }
    }
}
