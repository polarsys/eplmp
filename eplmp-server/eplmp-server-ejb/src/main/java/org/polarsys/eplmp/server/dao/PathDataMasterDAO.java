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

import org.polarsys.eplmp.core.configuration.PathDataMaster;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceMaster;
import org.polarsys.eplmp.core.exceptions.PathDataMasterNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class PathDataMasterDAO {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(PathDataMasterDAO.class.getName());

    public PathDataMasterDAO() {
    }

    public void createPathData(PathDataMaster pathDataMaster) {
        try {
            em.persist(pathDataMaster);
            em.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fail to create path data", e);
        }
    }


    public PathDataMaster findByPathAndProductInstanceIteration(String pPathAsString, ProductInstanceIteration pProductInstanceIteration) throws PathDataMasterNotFoundException {
        try {
            return em.createNamedQuery("PathDataMaster.findByPathAndProductInstanceIteration", PathDataMaster.class)
                    .setParameter("path", pPathAsString)
                    .setParameter("productInstanceIteration", pProductInstanceIteration)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new PathDataMasterNotFoundException(pPathAsString);
        }
    }

    public PathDataMaster findByPathIdAndProductInstanceIteration(int pPathId, ProductInstanceIteration pProductInstanceIteration) throws PathDataMasterNotFoundException {
        try {
            return em.createNamedQuery("PathDataMaster.findByPathIdAndProductInstanceIteration", PathDataMaster.class)
                    .setParameter("pathId", pPathId)
                    .setParameter("productInstanceIteration", pProductInstanceIteration)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new PathDataMasterNotFoundException(pPathId);
        }
    }

    public ProductInstanceMaster findByPathData(PathDataMaster pathDataMaster) {
        try {
            return em.createNamedQuery("ProductInstanceMaster.findByPathData", ProductInstanceMaster.class)
                    .setParameter("pathDataMasterList", pathDataMaster)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void removePathData(PathDataMaster pathDataMaster) {
        em.remove(pathDataMaster);
        em.flush();
    }
}
