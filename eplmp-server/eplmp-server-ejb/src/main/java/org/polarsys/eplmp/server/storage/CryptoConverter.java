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

package org.polarsys.eplmp.server.storage;


import org.polarsys.eplmp.server.config.SecurityConfig;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Inject
    private SecurityConfig securityConfig;

    private static final Logger LOGGER = Logger.getLogger(CryptoConverter.class.getName());
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Override
    public String convertToDatabaseColumn(String attrValue) {

        if (attrValue == null || attrValue.isEmpty()) {
            return attrValue;
        }

        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, securityConfig.getKey());
            //retrieves the initialization vector which needs to be stored along the ciphered data
            byte[] iv = c.getIV();
            //the '.' is a safe delimiter for Base64 data
            String base64IV = Base64.getEncoder().encodeToString(iv);
            String base64EncryptedData = Base64.getEncoder().encodeToString(c.doFinal(attrValue.getBytes("UTF-8")));
            return base64IV + "." + base64EncryptedData;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Cannot encrypt, stores the value unchanged", ex);
            return attrValue;
        }
    }

    @Override
    public String convertToEntityAttribute(String storedData) {

        if (storedData == null || storedData.isEmpty()) {
            return storedData;
        }

        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            String[] strData = storedData.split("\\.");
            byte[] iv = Base64.getDecoder().decode(strData[0]);
            c.init(Cipher.DECRYPT_MODE, securityConfig.getKey(), new IvParameterSpec(iv));
            return new String(c.doFinal(Base64.getDecoder().decode(strData[1])), "UTF-8");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Cannot decrypt, returns the value as stored", ex);
            return storedData;
        }
    }
}
