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


/**
 *
 * @author Morgan Guimard
 */
public class GCMAccountNotFoundException extends EntityNotFoundException {
    private final String mLogin;

    public GCMAccountNotFoundException(String pLogin) {
        this(pLogin, null);
    }

    public GCMAccountNotFoundException(String pLogin, Throwable pCause) {
        super( pCause);
        mLogin=pLogin;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mLogin);     
    }
}
