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

import org.polarsys.eplmp.core.configuration.BaselinedPart;
import org.polarsys.eplmp.core.configuration.ProductBaseline;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceIterationKey;
import org.polarsys.eplmp.core.exceptions.ProductInstanceIterationNotFoundException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "ProductInstanceIterationDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProductInstanceIterationDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    private static final Logger LOGGER = Logger.getLogger(ProductInstanceIterationDAO.class.getName());

    public ProductInstanceIterationDAO() {
        mLocale = Locale.getDefault();
    }

    public void createProductInstanceIteration(ProductInstanceIteration productInstanceIteration){
        try {
            em.persist(productInstanceIteration);
            em.flush();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Fail to create product instance iteration",e);
        }
    }


    public ProductInstanceIteration loadProductInstanceIteration(ProductInstanceIterationKey pId) throws ProductInstanceIterationNotFoundException {
        ProductInstanceIteration productInstanceIteration = em.find(ProductInstanceIteration.class, pId);
        if (productInstanceIteration == null) {
            throw new ProductInstanceIterationNotFoundException(mLocale, pId);
        } else {
            return productInstanceIteration;
        }
    }

    public ProductInstanceIteration loadProductInstanceIteration(Locale pLocale, ProductInstanceIterationKey pId) throws ProductInstanceIterationNotFoundException {
        mLocale = pLocale;
        return loadProductInstanceIteration(pId);
    }

    public List<BaselinedPart> findBaselinedPartWithReferenceLike(int collectionId, String q, int maxResults) {
        return em.createNamedQuery("BaselinedPart.findByReference",BaselinedPart.class)
                .setParameter("id", "%" + q + "%")
                .setParameter("partCollection",collectionId)
                .setMaxResults(maxResults)
                .getResultList();

    }

    public boolean isBaselinedUsed(ProductBaseline productBaseline) {
        return !em.createNamedQuery("ProductInstanceIteration.findByProductBaseline",ProductInstanceIteration.class)
                .setParameter("productBaseline",productBaseline)
                .getResultList().isEmpty();
    }
}
