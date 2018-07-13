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

import org.polarsys.eplmp.core.exceptions.PartUsageLinkNotFoundException;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMasterKey;
import org.polarsys.eplmp.core.product.PartSubstituteLink;
import org.polarsys.eplmp.core.product.PartUsageLink;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Stateless(name = "PartUsageLinkDAO")
public class PartUsageLinkDAO {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PathToPathLinkDAO pathToPathLinkDAO;

    private Locale mLocale;
    
    public PartUsageLinkDAO() {
        mLocale = Locale.getDefault();
    }

    
    public List<PartUsageLink[]> findPartUsagePaths(PartMasterKey pPartMKey){
        List<PartUsageLink> usages= findPartUsages(pPartMKey.getWorkspace(),pPartMKey.getNumber()); 
        List<PartUsageLink[]> usagePaths = new ArrayList<>();
        for(PartUsageLink usage:usages){
            List<PartUsageLink> path=new ArrayList<>();
            path.add(usage);
            createPath(usage,path,usagePaths);
        }
        
        return usagePaths;
    }

    private void createPath(PartUsageLink currentUsage, List<PartUsageLink> currentPath, List<PartUsageLink[]> usagePaths){
        
        PartIteration owner = em.createNamedQuery("PartUsageLink.getPartOwner",PartIteration.class)
                .setParameter("usage", currentUsage)
                .getSingleResult();
        List<PartUsageLink> parentUsages = findPartUsages(owner.getWorkspaceId(), owner.getPartNumber());
        
        for(PartUsageLink parentUsage:parentUsages){
            List<PartUsageLink> newPath=new ArrayList<>(currentPath);
            newPath.add(0,parentUsage);
            createPath(parentUsage, newPath, usagePaths);
        }
        if(parentUsages.isEmpty()) {
            usagePaths.add(currentPath.toArray(new PartUsageLink[currentPath.size()]));
        }
              
    }
    
    public List<PartUsageLink> findPartUsages(String workspaceId, String partNumber){
        return em.createNamedQuery("PartUsageLink.findByComponent",PartUsageLink.class)
            .setParameter("partNumber", partNumber)
            .setParameter("workspaceId", workspaceId)
            .getResultList();
    }

    public boolean hasPartUsages(String workspaceId, String partNumber){
        return !findPartUsages(workspaceId,partNumber).isEmpty();
    }

    public List<PartSubstituteLink> findPartSubstitutes(String workspaceId, String partNumber) {
        return em.createNamedQuery("PartSubstituteLink.findBySubstitute", PartSubstituteLink.class)
                .setParameter("partNumber", partNumber)
                .setParameter("workspaceId", workspaceId)
                .getResultList();
    }

    public boolean hasPartSubstitutes(String workspaceId, String partNumber) {
        return !findPartSubstitutes(workspaceId,partNumber).isEmpty();
    }
    
    public PartUsageLink loadPartUsageLink(int pId) throws PartUsageLinkNotFoundException {
        PartUsageLink link = em.find(PartUsageLink.class, pId);
        if (link == null) {
            throw new PartUsageLinkNotFoundException(mLocale, pId);
        } else {
            return link;
        }
    }

    public PartUsageLink loadPartUsageLink(Locale pLocale, int pId) throws PartUsageLinkNotFoundException {
        mLocale = pLocale;
        return loadPartUsageLink(pId);
    }

    public PartSubstituteLink loadPartSubstituteLink(int pId) throws PartUsageLinkNotFoundException {
        PartSubstituteLink link = em.find(PartSubstituteLink.class, pId);
        if (link == null) {
            throw new PartUsageLinkNotFoundException(mLocale, pId);
        } else {
            return link;
        }
    }

    public PartSubstituteLink loadPartSubstituteLink(Locale pLocale, int pId) throws PartUsageLinkNotFoundException {
        mLocale = pLocale;
        return loadPartSubstituteLink(pId);
    }

    public void removeOrphanPartLinks() {
        List<PartUsageLink> partUsageLinks = em.createNamedQuery("PartUsageLink.findOrphans", PartUsageLink.class).getResultList();

        for(PartUsageLink partUsageLink:partUsageLinks){
            pathToPathLinkDAO.removePathToPathLinks(partUsageLink.getFullId());
            for (PartSubstituteLink partSubstituteLink : partUsageLink.getSubstitutes()) {
                pathToPathLinkDAO.removePathToPathLinks(partSubstituteLink.getFullId());
            }
            em.remove(partUsageLink);
        }
    }

    public void removeOrphanPartLinks(Locale pLocale) {
        mLocale = pLocale;
        removeOrphanPartLinks();
    }
}
