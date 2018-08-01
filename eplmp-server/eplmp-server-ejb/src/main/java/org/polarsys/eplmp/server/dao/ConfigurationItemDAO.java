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
import org.polarsys.eplmp.core.product.*;

import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import java.util.List;


@RequestScoped
public class ConfigurationItemDAO {

    public static final String WORKSPACE_ID = "workspaceId";

    @PersistenceContext
    private EntityManager em;

    public ConfigurationItemDAO() {
    }

    public ConfigurationItem removeConfigurationItem(ConfigurationItemKey pKey) throws ConfigurationItemNotFoundException {
        ConfigurationItem ci = loadConfigurationItem(pKey);

        removeLayersFromConfigurationItem(pKey);
        removeEffectivitiesFromConfigurationItem(pKey);

        em.remove(ci);
        return ci;
    }

    public void removeLayersFromConfigurationItem(ConfigurationItemKey pKey){
        TypedQuery<Layer> query = em.createNamedQuery("Layer.removeLayersFromConfigurationItem", Layer.class);
        query.setParameter(WORKSPACE_ID, pKey.getWorkspace());
        query.setParameter("configurationItemId", pKey.getId());
        query.executeUpdate();
    }

    public void removeEffectivitiesFromConfigurationItem(ConfigurationItemKey pKey){
        TypedQuery<Effectivity> query = em.createNamedQuery("Effectivity.removeEffectivitiesFromConfigurationItem", Effectivity.class);
        query.setParameter(WORKSPACE_ID, pKey.getWorkspace());
        query.setParameter("configurationItemId", pKey.getId());
        query.executeUpdate();
    }

    public List<ConfigurationItem> findAllConfigurationItems(String pWorkspaceId) {
        TypedQuery<ConfigurationItem> query = em.createNamedQuery("ConfigurationItem.getConfigurationItemsInWorkspace", ConfigurationItem.class);
        query.setParameter(WORKSPACE_ID, pWorkspaceId);
        return query.getResultList();
    }

    public ConfigurationItem loadConfigurationItem(ConfigurationItemKey pKey)
            throws ConfigurationItemNotFoundException {
        ConfigurationItem ci = em.find(ConfigurationItem.class, pKey);
        if (ci == null) {
            throw new ConfigurationItemNotFoundException(pKey.getId());
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
            throw new ConfigurationItemAlreadyExistsException(pCI);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException("");
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
