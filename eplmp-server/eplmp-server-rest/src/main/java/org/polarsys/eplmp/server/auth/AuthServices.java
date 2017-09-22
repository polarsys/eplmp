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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.server.rest.Tools;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * This class is an helper class for authentication modules.
 * <p>
 * Allows to access AccountManagerBean from a class not managed by the container
 * Provides web.xml config values
 *
 * @author Morgan Guimard
 */
public class AuthServices {

    private static final Logger LOGGER = Logger.getLogger(AuthServices.class.getName());
    private static final String ACCOUNT_MANAGER = "java:app/eplmp-server-ejb/AccountManagerBean!org.polarsys.eplmp.core.services.IAccountManagerLocal";
    private static final String JAVA_COMP_ENV = "java:comp/env";

    private static IAccountManagerLocal accountManager;
    private static String[] publicPaths;

    static {

        try {
            InitialContext context = new InitialContext();
            accountManager = (IAccountManagerLocal) context.lookup(ACCOUNT_MANAGER);
            Context env = (Context) context.lookup(JAVA_COMP_ENV);
            final String publicPathsValue = (String) env.lookup("public-paths");

            if (publicPathsValue != null) {
                publicPaths = publicPathsValue.split(",");

                for (int i = 0; i < publicPaths.length; i++) {
                    boolean endLess = false;
                    if (publicPaths[i].endsWith("/**")) {
                        publicPaths[i] = publicPaths[i].substring(0, publicPaths[i].length() - 2);
                        endLess = true;
                    }
                    publicPaths[i] = publicPaths[i].replace("*", "[^/]+?");
                    if (endLess) {
                        publicPaths[i] += ".*";
                    }
                }

            }
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize AuthServices", e);
        }
    }


    public static Account authenticateAccount(String login, String password) {
        return accountManager.authenticateAccount(login, password);
    }

    public static UserGroupMapping getUserGroupMapping(String login) {
        return accountManager.getUserGroupMapping(login);
    }

    public static boolean isPublicRequestURI(String contextPath, String requestURI) {
        if (requestURI != null && publicPaths != null) {
            contextPath = Tools.stripTrailingSlash(contextPath);
            for (String excludedPath : publicPaths) {
                if (Pattern.matches(contextPath + excludedPath, requestURI)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-accept-encoding, password");
        response.setHeader("Access-Control-Expose-Headers", "jwt, x-archive-content-length, shared-entity-token, entity-token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

}
