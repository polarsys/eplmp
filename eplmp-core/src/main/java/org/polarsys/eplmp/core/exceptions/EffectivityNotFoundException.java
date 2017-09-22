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

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Frédéric Maury
 */
public class EffectivityNotFoundException extends EntityNotFoundException {
    private String mId;

    public EffectivityNotFoundException(String pMessage) {
        super(pMessage);
        mId = null;
    }

    public EffectivityNotFoundException(Locale pLocale, String pId) {
        this(pLocale, pId, null);
    }

    public EffectivityNotFoundException(Locale pLocale, String pId, Throwable pCause) {
        super(pLocale, pCause);
        mId = pId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mId);
    }
}
