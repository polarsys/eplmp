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

package org.polarsys.eplmp.i18n;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties files helper
 * * */
public class PropertiesLoader {

    public final static String[] SUPPORTED_LANGUAGES = {"fr", "en", "ru"};

    private PropertiesLoader() {
    }


    private final static Logger LOGGER = Logger.getLogger(PropertiesLoader.class.getName());

    /**
     * Load from given class resources an UTF8 encoded properties file with a lang suffix.
     * * */
    public static Properties loadLocalizedProperties(Locale locale, String propertiesFileBaseName, Class loader) {

        Properties properties = new Properties();

        String baseName;

        switch (locale.getLanguage()) {
            case "fr":
                baseName = propertiesFileBaseName + "_fr.properties";
                break;

            case "ru":
                baseName = propertiesFileBaseName + "_ru.properties";
                break;

            default:
                baseName = propertiesFileBaseName + "_en.properties";
                break;
        }

        try (InputStream is = loader.getResourceAsStream(baseName)) {
            properties.load(new InputStreamReader(is, "UTF-8"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

        return properties;
    }

}
