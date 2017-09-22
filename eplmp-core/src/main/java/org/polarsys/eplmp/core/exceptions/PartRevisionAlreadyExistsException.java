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

import org.polarsys.eplmp.core.product.PartRevision;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Morgan Guimard
 */
public class PartRevisionAlreadyExistsException extends EntityAlreadyExistsException {
    private final PartRevision mPartR;


    public PartRevisionAlreadyExistsException(String pMessage) {
        super(pMessage);
        mPartR = null;
    }


    public PartRevisionAlreadyExistsException(Locale pLocale, PartRevision pPartR) {
        this(pLocale, pPartR, null);
    }

    public PartRevisionAlreadyExistsException(Locale pLocale, PartRevision pPartR, Throwable pCause) {
        super(pLocale, pCause);
        mPartR=pPartR;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartR);
    }
}
