/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * DocDoku - initial API and implementation
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

    private static final String BUNDLE_BASE_NAME = "/org/polarsys/eplmp/core/i18n/LocalStrings";

    private Properties properties;

    public ApplicationException() {}

    public ApplicationException(String pMessage) {
        super(pMessage);
    }

    public ApplicationException(Throwable pCause) {
        super(pCause);
    }

    public ApplicationException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    protected String getBundleDefaultMessage() {
        return getBundleMessage(getClass().getSimpleName());
    }

    protected String getBundleMessage(String pKey) {
        if (null == properties) {
            setLocale(new Locale("en"));
        }
        return properties.getProperty(pKey);
    }

    public String getMessage(Locale locale){
        properties = PropertiesLoader.loadLocalizedProperties(locale, BUNDLE_BASE_NAME, getClass());
        return getMessage();
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

    private void setLocale(Locale locale) {
        properties = PropertiesLoader.loadLocalizedProperties(locale, BUNDLE_BASE_NAME, getClass());
    }
}
