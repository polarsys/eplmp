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
public class BaselineNotFoundException extends EntityNotFoundException {
    private final int mBaseline;


    public BaselineNotFoundException(String pMessage) {
        super(pMessage);
        mBaseline = -1;
    }

    public BaselineNotFoundException(Locale pLocale, int pBaseline) {
        this(pLocale, pBaseline, null);
    }

    public BaselineNotFoundException(Locale pLocale, int pBaseline, Throwable pCause) {
        super(pLocale, pCause);
        mBaseline=pBaseline;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mBaseline);
    }
}
