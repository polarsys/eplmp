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
 * @author Morgan Guimard
 */
public class WorkspaceNotEnabledException extends EntityNotFoundException {
    private final String mID;

    public WorkspaceNotEnabledException(String pMessage) {
        super(pMessage);
        mID=null;
    }

    public WorkspaceNotEnabledException(Locale pLocale, String pID) {
        this(pLocale, pID, null);
    }

    public WorkspaceNotEnabledException(Locale pLocale, String pID, Throwable pCause) {
        super(pLocale, pCause);
        mID=pID;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mID);     
    }
}
