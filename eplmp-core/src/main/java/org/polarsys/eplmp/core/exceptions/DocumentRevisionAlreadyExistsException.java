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

import org.polarsys.eplmp.core.document.DocumentRevision;

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class DocumentRevisionAlreadyExistsException extends EntityAlreadyExistsException {
    private final DocumentRevision mDocR;


    public DocumentRevisionAlreadyExistsException(String pMessage) {
        super(pMessage);
        mDocR=null;
    }

    public DocumentRevisionAlreadyExistsException(DocumentRevision pDocR) {
        this(pDocR, null);
    }

    public DocumentRevisionAlreadyExistsException(DocumentRevision pDocR, Throwable pCause) {
        super( pCause);
        mDocR=pDocR;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mDocR);
    }
}
