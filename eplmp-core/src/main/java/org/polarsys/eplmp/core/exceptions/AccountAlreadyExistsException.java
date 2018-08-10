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


/**
 *
 * @author Florent Garin
 */
public class AccountAlreadyExistsException extends EntityAlreadyExistsException {
    private final String mLogin;

    public AccountAlreadyExistsException(String pLogin) {
        this(pLogin, null);
    }

    public AccountAlreadyExistsException(String pLogin, Throwable pCause) {
        super(pCause);
        mLogin = pLogin;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mLogin);
    }
}
