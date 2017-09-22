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

import org.polarsys.eplmp.core.meta.ListOfValues;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class ListOfValuesAlreadyExistsException extends EntityAlreadyExistsException {
    private final ListOfValues mLov;


    public ListOfValuesAlreadyExistsException(String pMessage) {
        super(pMessage);
        mLov=null;
    }

    public ListOfValuesAlreadyExistsException(Locale pLocale, ListOfValues pLov) {
        this(pLocale, pLov, null);
    }

    public ListOfValuesAlreadyExistsException(Locale pLocale, ListOfValues pLov, Throwable pCause) {
        super(pLocale, pCause);
        mLov=pLov;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mLov);
    }
}
