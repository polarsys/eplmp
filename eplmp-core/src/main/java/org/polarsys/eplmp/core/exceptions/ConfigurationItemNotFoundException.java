/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
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
public class ConfigurationItemNotFoundException extends EntityNotFoundException {
    private final String mCIId;

    public ConfigurationItemNotFoundException(String pCIID) {
        this(pCIID, null);
    }

    public ConfigurationItemNotFoundException(String pCIId, Throwable pCause) {
        super(pCause);
        mCIId=pCIId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mCIId);     
    }
}
