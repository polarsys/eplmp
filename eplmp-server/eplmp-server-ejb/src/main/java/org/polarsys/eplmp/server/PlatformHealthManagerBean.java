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
import org.polarsys.eplmp.server.resourcegetters.OfficeConfig;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
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

    @Inject
    private OfficeConfig officeConfig;

    @Inject
    private ConfigManager configManager;

    @Override
    public void runHealthCheck() throws PlatformHealthException {

        boolean check = true;

        // Database check
        try {
            Long one = (Long) em.createNativeQuery("select 1 from dual").getSingleResult();
            if (one != 1) {
                LOGGER.log(Level.SEVERE, "Database doesn't seem to be reachable");
                check = false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database doesn't seem to be reachable", e);
            check = false;
        }

        // Indexer check
        boolean ping = indexerManager.ping();
        if (!ping) {
            LOGGER.log(Level.SEVERE, "Indexer doesn't seem to be reachable");
            check = false;
        }

        // LibreOffice check
        String officeHome = officeConfig.getOfficeHome();

        if (officeHome == null || officeHome.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Office is not configured");
            check = false;
        } else {
            Path path = Paths.get(officeHome);

            if (!path.toFile().exists()) {
                LOGGER.log(Level.SEVERE, "Office is not available for given path: " + officeHome);
                check = false;
            }

            if (!path.toFile().canExecute()) {
                LOGGER.log(Level.SEVERE, "Office is not executable");
                check = false;
            }
        }

        // Check for mandatory config
        String vaultPath = configManager.getVaultPath();

        if (vaultPath == null || vaultPath.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Vaultpath is not set, you won't be able to upload/download files");
            check = false;
        } else {
            Path path = Paths.get(vaultPath);

            if (!path.toFile().exists()) {
                LOGGER.log(Level.SEVERE, "Vaultpath is not available for given path: " + vaultPath);
                check = false;
            }

            if (!path.toFile().canWrite()) {
                LOGGER.log(Level.SEVERE, "Vaultpath is not writeable, please set appropriate rights on " + vaultPath);
                check = false;
            }

        }

        // Check for optional config
        String codebase = configManager.getCodebase();
        if (codebase == null || codebase.isEmpty()) {
            LOGGER.log(Level.WARNING, "Codebase is not set, if you are using docdoku-web-front with this server you should configure it");
        }

        if (!check) {
            LOGGER.log(Level.SEVERE, "Health check didn't pass");
            throw new PlatformHealthException(Locale.getDefault());
        }
    }
}