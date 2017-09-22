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
public class ListOfValuesNotFoundException extends EntityNotFoundException {
    private final String mName;

    public ListOfValuesNotFoundException(String pMessage) {
        super(pMessage);
        mName=null;
    }

    public ListOfValuesNotFoundException(Locale pLocale, String pName) {
        this(pLocale, pName, null);
    }

    public ListOfValuesNotFoundException(Locale pLocale, String pName, Throwable pCause) {
        super(pLocale, pCause);
        mName=pName;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mName);
    }
}
