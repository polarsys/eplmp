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
public class PartMasterNotFoundException extends EntityNotFoundException {
    private final String mPartM;

    public PartMasterNotFoundException(String pMessage) {
        super(pMessage);
        mPartM=null;
    }
    
    public PartMasterNotFoundException(Locale pLocale, String pPartM) {
        this(pLocale, pPartM, null);
    }

    public PartMasterNotFoundException(Locale pLocale, String pPartM, Throwable pCause) {
        super(pLocale, pCause);
        mPartM=pPartM;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartM);     
    }
}
