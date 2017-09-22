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
 * @author Taylor Labejof
 */
public class MilestoneNotFoundException extends EntityNotFoundException {
    private final int mChange;
    private final String mTitle;

    public MilestoneNotFoundException(String pMessage) {
        super(pMessage);
        mTitle=null;
        mChange=-1;
    }

    public MilestoneNotFoundException(Locale pLocale, int pChange) {
        this(pLocale, pChange, null);
    }

    public MilestoneNotFoundException(Locale pLocale, int pChange, Throwable pCause) {
        super(pLocale, pCause);
        mChange =pChange;
        mTitle=null;
    }

    public MilestoneNotFoundException(Locale pLocale, String pTitle) {
        super(pLocale, null);
        mTitle = pTitle;
        mChange = -1;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        if(mTitle!=null){
            return MessageFormat.format(message, mTitle);
        }
        return MessageFormat.format(message, "N° "+mChange);
    }
}
