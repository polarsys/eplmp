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


import org.polarsys.eplmp.core.exceptions.ConfigurationItemAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.ConfigurationItemNotFoundException;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.LayerNotFoundException;
import org.polarsys.eplmp.core.product.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Locale;

public class ConfigurationItemDAO {

    private EntityManager em;
    private Locale mLocale;

    public ConfigurationItemDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public ConfigurationItemDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }

    public void updateConfigurationItem(ConfigurationItem pCI) {
        em.merge(pCI);
    }

    public ConfigurationItem removeConfigurationItem(ConfigurationItemKey pKey) throws ConfigurationItemNotFoundException, LayerNotFoundException {
        ConfigurationItem ci = loadConfigurationItem(pKey);

        removeLayersFromConfigurationItem(pKey);
        removeEffectivitiesFromConfigurationItem(pKey);

        em.remove(ci);
        return ci;
    }

    public void removeLayersFromConfigurationItem(ConfigurationItemKey pKey){
        TypedQuery<Layer> query = em.createNamedQuery("Layer.removeLayersFromConfigurationItem", Layer.class);
        query.setParameter("workspaceId", pKey.getWorkspace());
        query.setParameter("configurationItemId", pKey.getId());
        query.executeUpdate();
    }

    public void removeEffectivitiesFromConfigurationItem(ConfigurationItemKey pKey){
        TypedQuery<Effectivity> query = em.createNamedQuery("Effectivity.removeEffectivitiesFromConfigurationItem", Effectivity.class);
        query.setParameter("workspaceId", pKey.getWorkspace());
        query.setParameter("configurationItemId", pKey.getId());
        query.executeUpdate();
    }

    public List<ConfigurationItem> findAllConfigurationItems(String pWorkspaceId) {
        TypedQuery<ConfigurationItem> query = em.createNamedQuery("ConfigurationItem.getConfigurationItemsInWorkspace", ConfigurationItem.class);
        query.setParameter("workspaceId", pWorkspaceId);
        return query.getResultList();
    }

    public ConfigurationItem loadConfigurationItem(ConfigurationItemKey pKey)
            throws ConfigurationItemNotFoundException {
        ConfigurationItem ci = em.find(ConfigurationItem.class, pKey);
        if (ci == null) {
            throw new ConfigurationItemNotFoundException(mLocale, pKey.getId());
        } else {
            return ci;
        }
    }

    public void createConfigurationItem(ConfigurationItem pCI) throws ConfigurationItemAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pCI);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new ConfigurationItemAlreadyExistsException(mLocale, pCI);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public List<ConfigurationItem> findConfigurationItemsByDesignItem(PartMaster partMaster) {

        TypedQuery<ConfigurationItem> query = em.createNamedQuery("ConfigurationItem.findByDesignItem", ConfigurationItem.class);
        return query.setParameter("designItem", partMaster).getResultList();

    }

    public boolean isPartMasterLinkedToConfigurationItem(PartMaster partMaster){
        return !findConfigurationItemsByDesignItem(partMaster).isEmpty();
    }
}
