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

import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "PathDataIterationDAO")
public class PathDataIterationDAO {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(PathDataIterationDAO.class.getName());

    public void createPathDataIteration(PathDataIteration pathDataIteration){
        try {
            em.persist(pathDataIteration);
            em.flush();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Fail to create path data",e);
        }
    }

    public List<PathDataIteration> getLastPathDataIterations(ProductInstanceIteration productInstanceIteration){
        return em.createNamedQuery("PathDataIteration.findLastIterationFromProductInstanceIteration",PathDataIteration.class)
                .setParameter("productInstanceIteration", productInstanceIteration)
                .getResultList();
    }
}
