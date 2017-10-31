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

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Morgan Guimard
 */
public class ProvidedAccountNotFoundException extends EntityNotFoundException {

    private final String mId;

    public ProvidedAccountNotFoundException(String message) {
        super(message);
        mId = null;
    }

    public ProvidedAccountNotFoundException(Locale pLocale, String id) {
        this(pLocale, id, null);
    }

    public ProvidedAccountNotFoundException(Locale pLocale, String id, Throwable pCause) {
        super(pLocale, pCause);
        mId = id;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mId);
    }
}
