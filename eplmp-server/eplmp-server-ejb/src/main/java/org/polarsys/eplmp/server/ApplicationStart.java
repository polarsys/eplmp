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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.services.IOAuthManagerLocal;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class ApplicationStart {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStart.class.getName());

    @Inject
    private IOAuthManagerLocal oAuthManager;

    @PostConstruct
    private void start() {
        LOGGER.log(Level.INFO, "ApplicationStart");
        oAuthManager.loadProvidersFromProperties();
    }

    @PreDestroy
    private void stop() {
        LOGGER.log(Level.INFO, "ApplicationStop");
    }
}
