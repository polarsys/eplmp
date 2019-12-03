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

package org.polarsys.eplmp.server.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Get config from resources
 *
 * @author Morgan Guimard
 */
@Singleton
public class SecurityConfig {

    @Resource(lookup="security.config")
    private Properties properties;

    private static final Logger LOGGER = Logger.getLogger(SecurityConfig.class.getName());

    private Key key;

    @PostConstruct
    void init(){
        try{
            KeyStore ks = KeyStore.getInstance(getKeyType());
            try (InputStream fis = new BufferedInputStream(new FileInputStream(getKeystoreLocation()))) {
                ks.load(fis, getKeystorePass().toCharArray());
            }
            key = ks.getKey(getKeyAlias(), getKeyPass().toCharArray());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Keystore loading failed", ex);
        }
    }

    private String getKeystoreLocation(){
        return properties.getProperty("keystoreLocation");
    }

    private String getKeystorePass(){
        return properties.getProperty("keystorePass");
    }

    private String getKeyAlias(){
        return properties.getProperty("keyAlias");
    }

    private String getKeyType(){
        return  Optional.ofNullable(properties.getProperty("keystoreType")).orElse("JCEKS");
    }

    private String getKeyPass(){
        return  Optional.ofNullable(properties.getProperty("keyPass")).orElse(getKeystorePass());
    }

    public Key getKey(){
        return key;
    }

}
