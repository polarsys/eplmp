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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties files helper
 *
 * @author Morgan Guimard
 * * */
public class PropertiesLoader {

    private static final String[] SUPPORTED_LANGUAGES = {"fr", "en", "ru"};

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class.getName());

    private PropertiesLoader() {
    }

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

    /**
     * Get the list of supported languages
     * * */
    public static List<String> getSupportedLanguages() {
        return Arrays.asList(SUPPORTED_LANGUAGES);
    }
}
