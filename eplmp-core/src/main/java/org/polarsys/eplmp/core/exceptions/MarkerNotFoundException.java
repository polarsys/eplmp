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


public class MarkerNotFoundException extends EntityNotFoundException {
    private final int mMarker;

    public MarkerNotFoundException(int pMarker) {
        this(pMarker, null);
    }

    public MarkerNotFoundException(int pMarker, Throwable pCause) {
        super( pCause);
        mMarker = pMarker;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mMarker);
    }

}
