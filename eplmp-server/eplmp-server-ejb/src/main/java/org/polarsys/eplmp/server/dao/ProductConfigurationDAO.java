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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "ProductConfigurationDAO")
public class ProductConfigurationDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;
    private static final Logger LOGGER = Logger.getLogger(ProductConfigurationDAO.class.getName());

    public ProductConfigurationDAO() {
        mLocale = Locale.getDefault();
    }

    public void createProductConfiguration(ProductConfiguration pProductConfiguration) throws CreationException {
        try {
            em.persist(pProductConfiguration);
            em.flush();
        } catch (PersistenceException pPEx) {
            LOGGER.log(Level.FINEST,null,pPEx);
            throw new CreationException(mLocale);
        }
    }

    public void createProductConfiguration(Locale pLocale, ProductConfiguration pProductConfiguration) throws CreationException {
        mLocale = pLocale;
        createProductConfiguration(pProductConfiguration);
    }

    public ProductConfiguration getProductConfiguration(int pProductConfigurationId) throws ProductConfigurationNotFoundException {
        ProductConfiguration productConfiguration = em.find(ProductConfiguration.class, pProductConfigurationId);
        if(productConfiguration != null){
            return productConfiguration;
        }else{
            throw new ProductConfigurationNotFoundException(mLocale,pProductConfigurationId);
        }
    }

    public ProductConfiguration getProductConfiguration(Locale pLocale, int pProductConfigurationId) throws ProductConfigurationNotFoundException {
        mLocale = pLocale;
        return getProductConfiguration(pProductConfigurationId);
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
