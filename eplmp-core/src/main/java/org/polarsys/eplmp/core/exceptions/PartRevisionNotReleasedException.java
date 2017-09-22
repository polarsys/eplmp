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
public class PartRevisionNotReleasedException extends ApplicationException {
    private final String mCIId;


    public PartRevisionNotReleasedException(String pMessage) {
        super(pMessage);
        mCIId=null;
    }

    public PartRevisionNotReleasedException(Locale pLocale, String pCIID) {
        this(pLocale, pCIID, null);
    }

    public PartRevisionNotReleasedException(Locale pLocale, String pCIId, Throwable pCause) {
        super(pLocale, pCause);
        mCIId=pCIId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mCIId);     
    }
}
