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
public class PathToPathLinkNotFoundException extends EntityNotFoundException {

    private final int mPathToPathLinkId;

    public PathToPathLinkNotFoundException(String pMessage) {
        super(pMessage);
        mPathToPathLinkId =0;
    }

    public PathToPathLinkNotFoundException(Locale pLocale, int pPathToPathLinkId) {
        this(pLocale, pPathToPathLinkId, null);
    }

    public PathToPathLinkNotFoundException(Locale pLocale, int pPathToPathLinkId, Throwable pCause) {
        super(pLocale, pCause);
        mPathToPathLinkId =pPathToPathLinkId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mPathToPathLinkId);
    }
}
