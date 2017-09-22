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

import org.polarsys.eplmp.core.change.ModificationNotification;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.product.PartRevisionKey;

import javax.persistence.EntityManager;
import java.util.List;

public class ModificationNotificationDAO {

    private EntityManager em;

    public ModificationNotificationDAO(EntityManager pEM) {
        em = pEM;
    }

    public void removeModificationNotifications(PartIterationKey pPartIPK){
        em.createNamedQuery("ModificationNotification.removeAllOnPartIteration")
                .setParameter("workspaceId", pPartIPK.getWorkspaceId())
                .setParameter("partNumber", pPartIPK.getPartMasterNumber())
                .setParameter("version", pPartIPK.getPartRevisionVersion())
                .setParameter("iteration", pPartIPK.getIteration()).executeUpdate();
    }

    public void removeModificationNotifications(PartRevisionKey pPartRPK){
        em.createNamedQuery("ModificationNotification.removeAllOnPartRevision")
                .setParameter("workspaceId", pPartRPK.getWorkspaceId())
                .setParameter("partNumber", pPartRPK.getPartMasterNumber())
                .setParameter("version", pPartRPK.getVersion()).executeUpdate();
    }

    public void createModificationNotification(ModificationNotification pNotification) {
        em.persist(pNotification);
    }

    public ModificationNotification getModificationNotification(int pId) {
        return em.find(ModificationNotification.class, pId);
    }

    public List<ModificationNotification> getModificationNotifications(PartIterationKey pPartIPK) {
        return em.createNamedQuery("ModificationNotification.findByImpactedPartIteration", ModificationNotification.class)
                .setParameter("workspaceId", pPartIPK.getWorkspaceId())
                .setParameter("partNumber", pPartIPK.getPartMasterNumber())
                .setParameter("version", pPartIPK.getPartRevisionVersion())
                .setParameter("iteration", pPartIPK.getIteration()).getResultList();
    }

    public boolean hasModificationNotifications(PartIterationKey pPartIPK){
        return !getModificationNotifications(pPartIPK).isEmpty();
    }

}
