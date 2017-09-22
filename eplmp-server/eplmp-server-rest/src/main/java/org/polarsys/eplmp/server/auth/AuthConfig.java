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

import org.jose4j.keys.HmacKey;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Get auth config from resources
 *
 * @author Morgan Guimard
 */
@ApplicationScoped
public class AuthConfig {

    private Properties properties;
    private Key defaultKey;

    private static final Logger LOGGER = Logger.getLogger(AuthConfig.class.getName());


    @PostConstruct
    private void init() {
        try {
            InitialContext ctx = new InitialContext();
            properties = (Properties) ctx.lookup("auth.config");
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            defaultKey = keyGen.generateKey();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize auth configuration", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Cannot generate random JWT default key", e);
        }
    }

    public Boolean isJwtEnabled() {
        return Boolean.parseBoolean(properties.getProperty("jwt.enabled"));
    }

    public Boolean isBasicHeaderEnabled() {
        return Boolean.parseBoolean(properties.getProperty("basic.header.enabled"));
    }

    public Boolean isSessionEnabled() {
        return Boolean.parseBoolean(properties.getProperty("session.enabled"));
    }

    public Key getJWTKey() {
        try {
            String secret = properties.getProperty("jwt.key");
            if (null != secret && !secret.isEmpty()) {
                return new HmacKey(secret.getBytes("UTF-8"));
            }
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Cannot create JWT key", e);
        }
        return defaultKey;
    }
}
