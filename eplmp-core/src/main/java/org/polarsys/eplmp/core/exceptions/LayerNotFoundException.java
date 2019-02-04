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
public class LayerNotFoundException extends EntityNotFoundException {
    private final int mLayer;

    public LayerNotFoundException(String pMessage) {
        super(pMessage);
        mLayer=-1;
    }

    public LayerNotFoundException(int pLayer) {
        this(pLayer, null);
    }

    public LayerNotFoundException(int pLayer, Throwable pCause) {
        super( pCause);
        mLayer=pLayer;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mLayer);     
    }
}
