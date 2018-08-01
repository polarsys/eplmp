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

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class ModificationNotificationDAO {

    public static final String WORKSPACE_ID = "workspaceId";
    public static final String PART_NUMBER = "partNumber";
    public static final String VERSION = "version";

    @PersistenceContext
    private EntityManager em;

    public void removeModificationNotifications(PartIterationKey pPartIPK){
        em.createNamedQuery("ModificationNotification.removeAllOnPartIteration")
                .setParameter(WORKSPACE_ID, pPartIPK.getWorkspaceId())
                .setParameter(PART_NUMBER, pPartIPK.getPartMasterNumber())
                .setParameter(VERSION, pPartIPK.getPartRevisionVersion())
                .setParameter("iteration", pPartIPK.getIteration()).executeUpdate();
    }

    public void removeModificationNotifications(PartRevisionKey pPartRPK){
        em.createNamedQuery("ModificationNotification.removeAllOnPartRevision")
                .setParameter(WORKSPACE_ID, pPartRPK.getWorkspaceId())
                .setParameter(PART_NUMBER, pPartRPK.getPartMasterNumber())
                .setParameter(VERSION, pPartRPK.getVersion()).executeUpdate();
    }

    public void createModificationNotification(ModificationNotification pNotification) {
        em.persist(pNotification);
    }

    public ModificationNotification getModificationNotification(int pId) {
        return em.find(ModificationNotification.class, pId);
    }

    public List<ModificationNotification> getModificationNotifications(PartIterationKey pPartIPK) {
        return em.createNamedQuery("ModificationNotification.findByImpactedPartIteration", ModificationNotification.class)
                .setParameter(WORKSPACE_ID, pPartIPK.getWorkspaceId())
                .setParameter(PART_NUMBER, pPartIPK.getPartMasterNumber())
                .setParameter(VERSION, pPartIPK.getPartRevisionVersion())
                .setParameter("iteration", pPartIPK.getIteration()).getResultList();
    }

    public boolean hasModificationNotifications(PartIterationKey pPartIPK){
        return !getModificationNotifications(pPartIPK).isEmpty();
    }

}
