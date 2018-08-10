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

import org.polarsys.eplmp.core.common.OAuthProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Get oauth config from resources
 * <p>
 * Insert oAuth provider config with ./asadmin commands, before the server starts
 *
 * @author Morgan Guimard
 */
@ApplicationScoped
public class OauthConfig {

    private static final Logger LOGGER = Logger.getLogger(OauthConfig.class.getName());

    private Properties properties;

    @PostConstruct
    private void init() {
        try {
            InitialContext ctx = new InitialContext();
            properties = (Properties) ctx.lookup("oauth.config");
        } catch (NamingException e) {
            LOGGER.log(Level.INFO, "Oauth providers configuration is empty");
        }
    }

    public List<OAuthProvider> getProviders() {

        if (properties == null || properties.isEmpty()) {
            LOGGER.log(Level.INFO, "No providers configured, skipping task.");
            return new ArrayList<>();
        }

        List<String> keys = properties.keySet().stream().map(Object::toString).collect(Collectors.toList());
        Map<Integer, OAuthProvider> providers = new HashMap<>();


        for (String k : keys) {

            String[] splitKey = k.split("/");

            if (splitKey.length < 2) {
                LOGGER.log(Level.SEVERE, "Cannot parse provider id: " + k);
                break;
            }

            Integer id;

            try {
                id = Integer.valueOf(splitKey[0]);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Cannot parse config key: " + k);
                break;
            }

            OAuthProvider oAuthProvider;

            if (!providers.containsKey(id)) {
                oAuthProvider = new OAuthProvider();
                oAuthProvider.setId(id);
                providers.put(id, oAuthProvider);
            } else {
                oAuthProvider = providers.get(id);
            }

            String property = k.substring(2);
            String value = properties.getProperty(k);

            switch (property) {
                case "name":
                    oAuthProvider.setName(value);
                    break;
                case "enabled":
                    oAuthProvider.setEnabled(Boolean.parseBoolean(value));
                    break;
                case "authority":
                    oAuthProvider.setAuthority(value);
                    break;
                case "issuer":
                    oAuthProvider.setIssuer(value);
                    break;
                case "clientID":
                    oAuthProvider.setClientID(value);
                    break;
                case "jwsAlgorithm":
                    oAuthProvider.setJwsAlgorithm(value);
                    break;
                case "jwkSetURL":
                    oAuthProvider.setJwkSetURL(value);
                    break;
                case "redirectUri":
                    oAuthProvider.setRedirectUri(value);
                    break;
                case "secret":
                    oAuthProvider.setSecret(value);
                    break;
                case "scope":
                    oAuthProvider.setScope(value);
                    break;
                case "responseType":
                    oAuthProvider.setResponseType(value);
                    break;
                case "authorizationEndpoint":
                    oAuthProvider.setAuthorizationEndpoint(value);
                    break;
                default:
                    break;
            }

        }

        List<OAuthProvider> discoversProviders = providers.values().stream().collect(Collectors.toList());
        LOGGER.log(Level.INFO, discoversProviders.size() + " providers discovered");
        return discoversProviders;
    }
}
