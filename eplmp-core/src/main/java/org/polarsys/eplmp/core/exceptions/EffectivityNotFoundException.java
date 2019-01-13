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
 * @author Frédéric Maury
 */
public class EffectivityNotFoundException extends EntityNotFoundException {
    private String mId;

    public EffectivityNotFoundException(String pId) {
        this(pId, null);
    }

    public EffectivityNotFoundException(String pId, Throwable pCause) {
        super( pCause);
        mId = pId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mId);
    }
}
