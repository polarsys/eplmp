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

import org.polarsys.eplmp.core.common.User;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class UserAlreadyExistsException extends EntityAlreadyExistsException {
    private final User mUser;
    
    
    public UserAlreadyExistsException(String pMessage) {
        super(pMessage);
        mUser=null;
    }
    
    
    public UserAlreadyExistsException(Locale pLocale, User pUser) {
        this(pLocale, pUser, null);
    }

    public UserAlreadyExistsException(Locale pLocale, User pUser, Throwable pCause) {
        super(pLocale, pCause);
        mUser=pUser;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mUser);     
    }
}
