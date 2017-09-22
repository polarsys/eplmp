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

import org.polarsys.eplmp.core.configuration.ProductConfiguration;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.ProductConfigurationNotFoundException;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductConfigurationDAO {

    private final EntityManager em;
    private final Locale mLocale;
    private static final Logger LOGGER = Logger.getLogger(ProductConfigurationDAO.class.getName());

    public ProductConfigurationDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }

    public ProductConfigurationDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public void createProductConfiguration(ProductConfiguration productConfiguration) throws CreationException {
        try {
            em.persist(productConfiguration);
            em.flush();
        } catch (PersistenceException pPEx) {
            LOGGER.log(Level.FINEST,null,pPEx);
            throw new CreationException(mLocale);
        }
    }

    public ProductConfiguration getProductConfiguration(int productConfigurationId) throws ProductConfigurationNotFoundException {
        ProductConfiguration productConfiguration = em.find(ProductConfiguration.class, productConfigurationId);
        if(productConfiguration != null){
            return productConfiguration;
        }else{
            throw new ProductConfigurationNotFoundException(mLocale,productConfigurationId);
        }
    }

    public List<ProductConfiguration> getAllProductConfigurations(String workspaceId) {
        return em.createNamedQuery("ProductConfiguration.findByWorkspace", ProductConfiguration.class)
                .setParameter("workspaceId", workspaceId)
                .getResultList();
    }

    public List<ProductConfiguration> getAllProductConfigurationsByConfigurationItem(ConfigurationItemKey ciKey) {
        return em.createNamedQuery("ProductConfiguration.findByConfigurationItem", ProductConfiguration.class)
                .setParameter("workspaceId", ciKey.getWorkspace())
                .setParameter("configurationItemId", ciKey.getId())
                .getResultList();
    }

    public void deleteProductConfiguration(ProductConfiguration productConfiguration) {
        em.remove(productConfiguration);
        em.flush();
    }
}
