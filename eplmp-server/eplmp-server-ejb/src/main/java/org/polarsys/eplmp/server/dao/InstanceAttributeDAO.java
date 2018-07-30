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

import org.polarsys.eplmp.core.meta.InstanceAttribute;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class InstanceAttributeDAO {
    private static final Logger LOGGER = Logger.getLogger(InstanceAttributeDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    public void removeAttribute(InstanceAttribute pAttr){
        em.remove(pAttr);
    }

    public void createAttribute(InstanceAttribute pAttr){
        try{
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pAttr);
            em.flush();
        }catch(EntityExistsException pEEEx){
            //already created
            LOGGER.log(Level.FINER,null,pEEEx);
        }
    }

    public List<InstanceAttribute> getPartIterationsInstanceAttributesInWorkspace(String workspaceId){

        return em.createNamedQuery("PartIteration.findDistinctInstanceAttributes", InstanceAttribute.class)
                .setParameter("workspaceId", workspaceId)
                .getResultList();

    }

    public List<InstanceAttribute> getPathDataInstanceAttributesInWorkspace(String workspaceId){

        return em.createNamedQuery("PathDataIteration.findDistinctInstanceAttributes", InstanceAttribute.class)
                .setParameter("workspaceId", workspaceId)
                .getResultList();

    }
}
