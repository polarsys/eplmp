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

package org.polarsys.eplmp.server.helpers;

import org.polarsys.eplmp.i18n.PropertiesLoader;

import java.util.Locale;
import java.util.Properties;

public class LangHelper {

    private static final String BUNDLE_BASE_NAME = "/org/polarsys/eplmp/server/i18n/LocalStrings";

    private final Properties properties;

    public LangHelper(Locale locale) {
        properties = PropertiesLoader.loadLocalizedProperties(locale, BUNDLE_BASE_NAME, getClass());
    }

    public String getLocalizedMessage(String key) {
        return properties.getProperty(key);
    }

}
