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

import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.exceptions.BaselineNotFoundException;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.ProductInstanceMasterNotFoundException;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductBaselineDAO {

    private final EntityManager em;
    private final Locale mLocale;
    private static final Logger LOGGER = Logger.getLogger(ProductBaselineDAO.class.getName());

    public ProductBaselineDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }

    public ProductBaselineDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public List<ProductBaseline> findBaselines(String workspaceId) {
        return em.createQuery("SELECT b FROM ProductBaseline b WHERE b.configurationItem.workspace.id = :workspaceId", ProductBaseline.class)
                .setParameter("workspaceId",workspaceId)
                .getResultList();
    }

    public List<ProductBaseline> findBaselines(String ciId, String workspaceId){
        return em.createNamedQuery("ProductBaseline.findByConfigurationItemId", ProductBaseline.class)
                .setParameter("ciId", ciId)
                .setParameter("workspaceId",workspaceId)
                .getResultList();
    }

    public void createBaseline(ProductBaseline productBaseline) throws CreationException {
        try {
            em.persist(productBaseline);
            em.flush();
        } catch (PersistenceException pPEx) {
            LOGGER.log(Level.FINEST,null,pPEx);
            throw new CreationException(mLocale);
        }
    }

    public ProductBaseline loadBaseline(int pId) throws BaselineNotFoundException {
        ProductBaseline productBaseline = em.find(ProductBaseline.class, pId);
        if (productBaseline == null) {
            throw new BaselineNotFoundException(mLocale, pId);
        } else {
            return productBaseline;
        }
    }

    public void deleteBaseline(ProductBaseline productBaseline) {
        flushBaselinedParts(productBaseline);
        em.remove(productBaseline);
        em.flush();
    }

    public boolean existBaselinedPart(String workspaceId, String partNumber) {
        return em.createNamedQuery("BaselinedPart.existBaselinedPart", Long.class)
            .setParameter("partNumber", partNumber)
            .setParameter("workspaceId", workspaceId)
            .getSingleResult() > 0;
    }

    public void flushBaselinedParts(ProductBaseline productBaseline) {
        productBaseline.removeAllBaselinedParts();
        em.flush();
    }

    public List<ProductBaseline> findBaselineWherePartRevisionHasIterations(PartRevision partRevision) {
        return em.createNamedQuery("ProductBaseline.getBaselinesForPartRevision", ProductBaseline.class)
                .setParameter("partRevision", partRevision)
                .getResultList();
    }

    public List<PartRevision> findObsoletePartsInBaseline(String workspaceId, ProductBaseline productBaseline) {
        return em.createNamedQuery("ProductBaseline.findObsoletePartRevisions", PartRevision.class)
                .setParameter("productBaseline", productBaseline)
                .setParameter("workspaceId", workspaceId)
                .getResultList();
    }

    public ProductBaseline findBaselineById(int baselineId) {
        return em.find(ProductBaseline.class, baselineId);
    }

    public List<BaselinedPart> findBaselinedPartWithReferenceLike(int collectionId, String q, int maxResults) throws BaselineNotFoundException {
        return em.createNamedQuery("BaselinedPart.findByReference",BaselinedPart.class)
                                                  .setParameter("id", "%" + q + "%")
                                                  .setParameter("partCollection",collectionId)
                                                  .setMaxResults(maxResults)
                                                  .getResultList();
    }

    public ProductBaseline findLastBaselineWithSerialNumber(ConfigurationItemKey ciKey, String serialNumber) throws ProductInstanceMasterNotFoundException {
        ProductInstanceMasterKey productInstanceMasterKey = new ProductInstanceMasterKey(serialNumber, ciKey);
        ProductInstanceMaster productIM = new ProductInstanceMasterDAO(em).loadProductInstanceMaster(productInstanceMasterKey);
        ProductInstanceIteration productII = productIM.getLastIteration();

        if (productII != null) {
            return productII.getBasedOn();
        }

        return null;
    }

}
