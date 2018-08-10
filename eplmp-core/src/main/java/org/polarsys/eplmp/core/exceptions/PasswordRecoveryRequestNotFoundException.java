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
 * @author Florent Garin
 */
public class PasswordRecoveryRequestNotFoundException extends EntityNotFoundException {
    private final String mRecoveryUUID;

    public PasswordRecoveryRequestNotFoundException(String recoveryUUID) {
        this(recoveryUUID, null);
    }

    public PasswordRecoveryRequestNotFoundException(String recoveryUUID, Throwable pCause) {
        super( pCause);
        mRecoveryUUID = recoveryUUID;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mRecoveryUUID);
    }
}
