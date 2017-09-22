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

package org.polarsys.eplmp.server.auth;

import javax.inject.Inject;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet context listener, register custom auth provider to application
 *
 * @author Morgan Guimard
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(CustomServletContextListener.class.getName());

    @Inject
    private AuthConfig authConfig;

    public CustomServletContextListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        LOGGER.log(Level.INFO, "Registering authentication provider");

        AuthConfigFactory.getFactory()
                .registerConfigProvider(new CustomAuthConfigProvider(authConfig), "HttpServlet",
                        getAppContextID(sce.getServletContext()), "Custom authentication modules registration on HttpServlet layer");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.log(Level.INFO, "Context destroyed");
    }

    public static String getAppContextID(ServletContext context) {
        return context.getVirtualServerName() + " " + context.getContextPath();
    }
}
