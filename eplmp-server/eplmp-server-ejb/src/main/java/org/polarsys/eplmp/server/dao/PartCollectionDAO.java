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

import org.polarsys.eplmp.core.configuration.PartCollection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "PartCollectionDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PartCollectionDAO {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(PartCollectionDAO.class.getName());

    public void createPartCollection(PartCollection partCollection){
        try {
            em.persist(partCollection);
            em.flush();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,"Fail to create a collection of parts",e);
        }
    }
}
