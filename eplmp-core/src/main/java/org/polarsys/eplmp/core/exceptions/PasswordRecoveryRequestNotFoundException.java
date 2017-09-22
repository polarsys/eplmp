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
 * @author Florent Garin
 */
public class PasswordRecoveryRequestNotFoundException extends EntityNotFoundException {
    private final String mRecoveryUUID;


    public PasswordRecoveryRequestNotFoundException(String pMessage) {
        super(pMessage);
        mRecoveryUUID = null;
    }

    public PasswordRecoveryRequestNotFoundException(Locale pLocale, String recoveryUUID) {
        this(pLocale, recoveryUUID, null);
    }

    public PasswordRecoveryRequestNotFoundException(Locale pLocale, String recoveryUUID, Throwable pCause) {
        super(pLocale, pCause);
        mRecoveryUUID = recoveryUUID;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mRecoveryUUID);
    }
}
