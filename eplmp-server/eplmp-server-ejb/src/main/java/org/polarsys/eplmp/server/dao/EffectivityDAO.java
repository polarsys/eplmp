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
import org.polarsys.eplmp.core.exceptions.EffectivityAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.EffectivityNotFoundException;
import org.polarsys.eplmp.core.product.Effectivity;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Stateless(name = "EffectivityDAO")
public class EffectivityDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public EffectivityDAO() {
        mLocale = Locale.getDefault();
    }

    public Effectivity loadEffectivity(int pId) throws EffectivityNotFoundException {
        Effectivity effectivity = em.find(Effectivity.class, pId);
        if (effectivity == null) {
            throw new EffectivityNotFoundException(mLocale, String.valueOf(pId));
        } else {
            return effectivity;
        }
    }

    public Effectivity loadEffectivity(Locale pLocale, int pId) throws EffectivityNotFoundException {
        mLocale = pLocale;
        return loadEffectivity(pId);
    }

    public void updateEffectivity(Effectivity effectivity) {
        em.merge(effectivity);
        em.flush();
    }

    public void removeEffectivity(Effectivity pEffectivity) {
        em.remove(pEffectivity);
        em.flush();
    }

    public void createEffectivity(Effectivity pEffectivity) throws EffectivityAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pEffectivity);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new EffectivityAlreadyExistsException(mLocale, pEffectivity);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createEffectivity(Locale pLocale, Effectivity pEffectivity) throws EffectivityAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createEffectivity(pEffectivity);
    }

    public PartRevision getPartRevisionHolder(int pId) {

        TypedQuery<PartRevision> query =
                em.createNamedQuery("Effectivity.findPartRevisionHolder", PartRevision.class);
        query.setParameter("effectivityId", pId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void removeEffectivityConstraints(String workspaceId) {
        TypedQuery<Effectivity> query = em.createNamedQuery("Effectivity.getEffectivitiesInWorkspace", Effectivity.class);
        query.setParameter("workspaceId", workspaceId);
        List<Effectivity> effectivities = query.getResultList();
        effectivities.forEach(effectivity -> effectivity.setConfigurationItem(null));
    }
}
