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

import org.polarsys.eplmp.core.exceptions.PartIterationNotFoundException;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;
import org.polarsys.eplmp.core.product.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

@Stateless(name = "PartIterationDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PartIterationDAO {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConversionDAO conversionDAO;

    private Locale mLocale;

    public PartIterationDAO() {
        mLocale = Locale.getDefault();
    }

    public PartIteration loadPartI(PartIterationKey pKey) throws PartIterationNotFoundException {
        PartIteration partI = em.find(PartIteration.class, pKey);
        if (partI == null) {
            throw new PartIterationNotFoundException(mLocale, pKey);
        } else {
            return partI;
        }
    }

    public PartIteration loadPartI(Locale pLocale, PartIterationKey pKey) throws PartIterationNotFoundException {
        mLocale = pLocale;
        return loadPartI(pKey);
    }
    
    public void updateIteration(PartIteration pPartI){
        em.merge(pPartI);
    }

    public void removeIteration(PartIteration pPartI){
        conversionDAO.removePartIterationConversion(pPartI);
        for(PartUsageLink partUsageLink:pPartI.getComponents()){
            if(!partLinkIsUsedInPreviousIteration(partUsageLink,pPartI)){
                em.remove(partUsageLink);
            }
        }
        em.remove(pPartI);
    }

    public boolean partLinkIsUsedInPreviousIteration(PartUsageLink partUsageLink, PartIteration partIte) {
        int iteration = partIte.getIteration();
        if(iteration == 1){
            return false;
        }
        PartIteration previousIteration = partIte.getPartRevision().getIteration(iteration-1);
        return previousIteration.getComponents().contains(partUsageLink);
    }

    public List<PartIteration> findUsedByAsComponent(PartMasterKey pPart) {
        return findUsedByAsComponent(em.getReference(PartMaster.class,pPart));
    }

    public List<PartIteration> findUsedByAsComponent(PartMaster pPart) {
        return em.createNamedQuery("PartIteration.findUsedByAsComponent", PartIteration.class)
                .setParameter("partMaster", pPart).getResultList();
    }

    public List<PartIteration> findUsedByAsSubstitute(PartMasterKey pPart) {
        return findUsedByAsSubstitute(em.getReference(PartMaster.class,pPart));
    }

    public List<PartIteration> findUsedByAsSubstitute(PartMaster pPart) {
        return em.createNamedQuery("PartIteration.findUsedByAsSubstitute", PartIteration.class)
                .setParameter("partMaster", pPart).getResultList();
    }


    public List<PartIteration> findAllPartIterationFromLOV(ListOfValuesKey lovKey) {
        return em.createNamedQuery("PartIteration.findWhereLOV", PartIteration.class)
                .setParameter("lovName", lovKey.getName())
                .setParameter("workspace_id", lovKey.getWorkspaceId())
                .getResultList();
    }
}
