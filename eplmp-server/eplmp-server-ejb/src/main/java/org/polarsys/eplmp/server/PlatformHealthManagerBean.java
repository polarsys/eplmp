/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.exceptions.PlatformHealthException;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IIndexerManagerLocal;
import org.polarsys.eplmp.core.services.IPlatformHealthManagerLocal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Local(IPlatformHealthManagerLocal.class)
@Stateless(name = "PlatformHealthManagerBean")
public class PlatformHealthManagerBean implements IPlatformHealthManagerLocal {

    private static final Logger LOGGER = Logger.getLogger(PlatformHealthManagerBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IIndexerManagerLocal indexerManager;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    @Override
    public void runHealthCheck() throws PlatformHealthException {
        // Database check
        try {
            Long one = (Long) em.createNativeQuery("select 1 from dual").getSingleResult();
            if (one != 1) {
                throw new PlatformHealthException("Cannot reach database.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database doesn't seem to be reachable", e);
            throw new PlatformHealthException(e);
        }

        // Indexer check
        boolean ping = indexerManager.ping();
        if (!ping) {
            LOGGER.log(Level.WARNING, "Indexer doesn't seem to be reachable");
            throw new PlatformHealthException("Cannot reach indexer");
        }
    }
}