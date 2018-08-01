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

package org.polarsys.eplmp.server.rest.locale;


import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.i18n.PropertiesLoader;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
@RequestScoped
public class LocaleProvider {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final Locale DEFAULT = new Locale(DEFAULT_LANGUAGE);
    private static final Logger LOGGER = Logger.getLogger(LocaleProvider.class.getName());

    @Context
    private HttpServletRequest httpRequest;

    private Locale locale;

    public LocaleProvider() {
    }

    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            IContextManagerLocal contextManager = (IContextManagerLocal) ctx.lookup("java:app/eplmp-server-ejb/ContextManagerBean!org.polarsys.eplmp.core.services.IContextManagerLocal");
            if (contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID) || contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)) {
                IAccountManagerLocal accountManager = (IAccountManagerLocal) ctx.lookup("java:app/eplmp-server-ejb/AccountManagerBean!org.polarsys.eplmp.core.services.IAccountManagerLocal");
                try {
                    Account myAccount = accountManager.getMyAccount();
                    locale = myAccount.getLocale();
                } catch (AccountNotFoundException e) {
                    LOGGER.log(Level.FINE, null, e);
                    locale = defaults();
                }
            } else {
                locale = defaults();
            }
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize LocaleProvider", e);
        }
    }

    @Produces
    public Locale userLocale() {
        return locale;
    }

    private Locale defaults() {
        Locale locale = httpRequest.getLocale();
        String language = locale.getLanguage();
        return PropertiesLoader.getSupportedLanguages().contains(language) ? new Locale(language) : DEFAULT;
    }

}
