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
public class BaselineNotFoundException extends EntityNotFoundException {
    private final int mBaseline;

    public BaselineNotFoundException(String pMessage) {
        super(pMessage);
        mBaseline = -1;
    }

    public BaselineNotFoundException(int pBaseline) {
        this(pBaseline, null);
    }

    public BaselineNotFoundException(int pBaseline, Throwable pCause) {
        super(pCause);
        mBaseline = pBaseline;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mBaseline);
    }
}
