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

package org.polarsys.eplmp.server.resourcegetters;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class OfficeConfig {

    private Properties properties;

    private static final Logger LOGGER = Logger.getLogger(OfficeConfig.class.getName());

    @PostConstruct
    private void init() {
        try {
            InitialContext ctx = new InitialContext();
            properties = (Properties) ctx.lookup("office.config");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize office configuration", e);
        }
    }

    public String getOfficeHome() {
        return properties.getProperty("office_home");
    }

    public Integer getOfficePort() {
        return Integer.parseInt(properties.getProperty("office_port"));
    }
}
