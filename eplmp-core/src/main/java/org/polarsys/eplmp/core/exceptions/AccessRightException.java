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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class AccessRightException extends ApplicationException {
    private final String mName;

    public AccessRightException(String pMessage) {
        super(pMessage);
        mName=null;
    }
    
    public AccessRightException(Locale pLocale, User pUser) {
        this(pLocale, pUser.toString());
    }
    
    public AccessRightException(Locale pLocale, Account pAccount) {
        this(pLocale, pAccount.toString());
    }

    public AccessRightException(Locale pLocale, String pName) {
        super(pLocale);
        mName=pName;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mName);     
    }
}
