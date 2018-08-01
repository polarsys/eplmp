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

import org.polarsys.eplmp.core.product.Effectivity;

import java.text.MessageFormat;


public class EffectivityAlreadyExistsException extends EntityAlreadyExistsException {
    private Effectivity mEffectivity;

    public EffectivityAlreadyExistsException(String pMessage) {
        super(pMessage);
        mEffectivity=null;
    }

    public EffectivityAlreadyExistsException(Effectivity pEffectivity) {
        this(pEffectivity, null);
    }

    public EffectivityAlreadyExistsException(Effectivity pEffectivity, Throwable pCause) {
        super( pCause);
        mEffectivity=pEffectivity;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mEffectivity);
    }
}
