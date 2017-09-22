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
 * @author Florent Garin
 */
public class UserNotFoundException extends EntityNotFoundException {
    private final String mLogin;
    
    public UserNotFoundException(String pMessage) {
        super(pMessage);
        mLogin=null;
    }
    
    public UserNotFoundException(Locale pLocale, String pLogin) {
        this(pLocale, pLogin, null);
    }

    public UserNotFoundException(Locale pLocale, String pLogin, Throwable pCause) {
        super(pLocale, pCause);
        mLogin=pLogin;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mLogin);     
    }
}
