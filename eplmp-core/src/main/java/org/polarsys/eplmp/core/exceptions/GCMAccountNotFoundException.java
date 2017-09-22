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
 *
 * @author Morgan Guimard
 */
public class GCMAccountNotFoundException extends EntityNotFoundException {
    private final String mLogin;


    public GCMAccountNotFoundException(Locale pLocale, String pLogin) {
        this(pLocale, pLogin, null);
    }

    public GCMAccountNotFoundException(Locale pLocale, String pLogin, Throwable pCause) {
        super(pLocale, pCause);
        mLogin=pLogin;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mLogin);     
    }
}
