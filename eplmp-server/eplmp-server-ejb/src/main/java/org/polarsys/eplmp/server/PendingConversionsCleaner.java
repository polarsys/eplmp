/*******************************************************************************
 * Copyright (c) 2017-2019 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.server.dao.ConversionDAO;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;


@Singleton
@Startup
public class PendingConversionsCleaner {

    private final static Integer RETENTION_TIME_MS = 60 * 60 * 1000;
    private final static String TIMER_HOURS = "*";
    private final static String TIMER_MINUTES = "*/5";
    private Logger LOGGER = Logger.getLogger(PendingConversionsCleaner.class.getName());

    @Inject
    private ConversionDAO conversionDAO;

    @PostConstruct
    private void start() {
        LOGGER.info("PendingConversionsCleaner registered");
    }

    @Schedule(hour = TIMER_HOURS, minute = TIMER_MINUTES, persistent = false)
    public void run() {
        LOGGER.info("Cleaning pending conversions");
        Integer conversionsSetAsFailed = conversionDAO.setPendingConversionsAsFailedIfOver(RETENTION_TIME_MS);
        LOGGER.info(conversionsSetAsFailed + " conversion(s) set as failed");
    }

}
