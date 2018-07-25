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
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceMaster;
import org.polarsys.eplmp.core.configuration.ProductInstanceMasterKey;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.ProductInstanceAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.ProductInstanceMasterNotFoundException;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Locale;

@Stateless(name = "ProductInstanceMasterDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProductInstanceMasterDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public ProductInstanceMasterDAO() {
        mLocale = Locale.getDefault();
    }

    public List<ProductInstanceMaster> findProductInstanceMasters(String workspaceId) {
        return em.createQuery("SELECT pim FROM ProductInstanceMaster pim WHERE pim.instanceOf.workspace.id = :workspaceId", ProductInstanceMaster.class)
                .setParameter("workspaceId",workspaceId)
                .getResultList();
    }

    public List<ProductInstanceMaster> findProductInstanceMasters(String ciId, String workspaceId) {
        return em.createNamedQuery("ProductInstanceMaster.findByConfigurationItemId", ProductInstanceMaster.class)
                .setParameter("ciId", ciId)
                .setParameter("workspaceId",workspaceId)
                .getResultList();
    }

    public List<ProductInstanceMaster> findProductInstanceMasters(PartRevision partRevision) {
        return em.createNamedQuery("ProductInstanceMaster.findByPart", ProductInstanceMaster.class)
                .setParameter("partRevision", partRevision)
                .getResultList();
    }

    public void createProductInstanceMaster(ProductInstanceMaster pProductInstanceMaster) throws ProductInstanceAlreadyExistsException, CreationException {
        try{
            em.persist(pProductInstanceMaster);
            em.flush();
        }catch (EntityExistsException e){
            throw new ProductInstanceAlreadyExistsException(mLocale, pProductInstanceMaster);
        }catch (PersistenceException e){
            throw new CreationException(mLocale);
        }

    }

    public void createProductInstanceMaster(Locale pLocale, ProductInstanceMaster pProductInstanceMaster) throws ProductInstanceAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createProductInstanceMaster(pProductInstanceMaster);
    }

    public ProductInstanceMaster loadProductInstanceMaster(ProductInstanceMasterKey pId) throws ProductInstanceMasterNotFoundException {
        ProductInstanceMaster productInstanceMaster = em.find(ProductInstanceMaster.class, pId);
        if (productInstanceMaster == null) {
            throw new ProductInstanceMasterNotFoundException(mLocale, pId);
        } else {
            return productInstanceMaster;
        }
    }

    public ProductInstanceMaster loadProductInstanceMaster(Locale pLocale, ProductInstanceMasterKey pId) throws ProductInstanceMasterNotFoundException {
        mLocale = pLocale;
        return loadProductInstanceMaster(pId);
    }

    public boolean existsProductInstanceMaster(ProductInstanceMasterKey pId) {
        return em.find(ProductInstanceMaster.class, pId) != null;
    }

    public void deleteProductInstanceMaster(ProductInstanceMaster productInstanceMaster) {
        for(ProductInstanceIteration productInstanceIteration : productInstanceMaster.getProductInstanceIterations()){
            for(BaselinedPart baselinedPart : productInstanceIteration.getBaselinedParts().values()){
                em.remove(baselinedPart);
            }
            
            em.refresh(productInstanceIteration.getPartCollection());
            em.remove(productInstanceIteration.getPartCollection());
            em.remove(productInstanceIteration);
        }

        em.remove(productInstanceMaster);
        em.flush();
    }
}
