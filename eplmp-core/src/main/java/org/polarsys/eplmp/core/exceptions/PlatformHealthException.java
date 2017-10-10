/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.core.exceptions;

import java.util.Locale;

public class PlatformHealthException extends ApplicationException {

    public PlatformHealthException(String pMessage) {
        super(pMessage);
    }

    public PlatformHealthException(Locale pLocale) {
        this(pLocale, null);
    }

    public PlatformHealthException(Locale pLocale, Throwable pCause) {
        super(pLocale, pCause);
    }

    public PlatformHealthException(Throwable pCause) {
        super(Locale.getDefault(), pCause);
    }

    @Override
    public String getLocalizedMessage() {
        return getBundleDefaultMessage();
    }
}