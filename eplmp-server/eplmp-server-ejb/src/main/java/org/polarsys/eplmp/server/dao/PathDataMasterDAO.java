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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathDataMasterDAO {

    private EntityManager em;
    private Locale mLocale;

    private static final Logger LOGGER = Logger.getLogger(PathDataMasterDAO.class.getName());

    public PathDataMasterDAO(EntityManager pEM) {
        em = pEM;
    }

    public PathDataMasterDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public void createPathData(PathDataMaster pathDataMaster){
        try {
            em.persist(pathDataMaster);
            em.flush();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Fail to create path data",e);
        }
    }


    public PathDataMaster findByPathAndProductInstanceIteration(String pathAsString, ProductInstanceIteration productInstanceIteration) throws PathDataMasterNotFoundException {
        try {
            return em.createNamedQuery("PathDataMaster.findByPathAndProductInstanceIteration", PathDataMaster.class)
                    .setParameter("path", pathAsString)
                    .setParameter("productInstanceIteration", productInstanceIteration)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new PathDataMasterNotFoundException(mLocale,pathAsString);
        }
    }

    public PathDataMaster findByPathIdAndProductInstanceIteration(int pathId, ProductInstanceIteration productInstanceIteration) throws PathDataMasterNotFoundException {
        try {
            return em.createNamedQuery("PathDataMaster.findByPathIdAndProductInstanceIteration", PathDataMaster.class)
                    .setParameter("pathId", pathId)
                    .setParameter("productInstanceIteration", productInstanceIteration)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new PathDataMasterNotFoundException(mLocale,pathId);
        }
    }

    public ProductInstanceMaster findByPathData(PathDataMaster pathDataMaster){
        try {
            return em.createNamedQuery("ProductInstanceMaster.findByPathData", ProductInstanceMaster.class)
                    .setParameter("pathDataMasterList", pathDataMaster)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public void removePathData(PathDataMaster pathDataMaster) {
        em.remove(pathDataMaster);
        em.flush();
    }
}
