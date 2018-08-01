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
 * @author Morgan Guimard
 */
public class ProvidedAccountNotFoundException extends EntityNotFoundException {

    private final String mId;

    public ProvidedAccountNotFoundException(String id) {
        this(id, null);
    }

    public ProvidedAccountNotFoundException(String id, Throwable pCause) {
        super( pCause);
        mId = id;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mId);
    }
}
