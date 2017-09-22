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

package org.polarsys.eplmp.core.exceptions;

import org.polarsys.eplmp.i18n.PropertiesLoader;

import java.util.Locale;
import java.util.Properties;

/**
 * Parent class for all application (non system) exceptions.
 *
 * @author Florent Garin
 */
public abstract class ApplicationException extends Exception {

    private static final String BUNDLE_BASE_NAME = "/com/docdoku/core/i18n/LocalStrings";
    private Properties properties;

    public ApplicationException(String pMessage) {
        super(pMessage);
        loadFile(Locale.getDefault());
    }

    public ApplicationException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        loadFile(Locale.getDefault());
    }

    public ApplicationException(Locale pLocale) {
        super();
        loadFile(pLocale);
    }

    public ApplicationException(Locale pLocale, Throwable pCause) {
        super(pCause);
        loadFile(pLocale);
    }

    public void setLocale(Locale pLocale) {
        loadFile(pLocale);
    }

    protected String getBundleDefaultMessage() {
        return getBundleMessage(getClass().getSimpleName());
    }

    protected String getBundleMessage(String pKey) {
        return properties.getProperty(pKey);
    }

    @Override
    public String getMessage() {
        String detailMessage = super.getMessage();
        return detailMessage == null ? getLocalizedMessage() : detailMessage;
    }

    @Override
    public abstract String getLocalizedMessage();

    @Override
    public String toString() {
        return getMessage();
    }

    private void loadFile(Locale locale) {
        properties = PropertiesLoader.loadLocalizedProperties(locale, BUNDLE_BASE_NAME, getClass());
    }
}
