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

import org.polarsys.eplmp.core.document.DocumentIterationKey;

import java.text.MessageFormat;

/**
 *
 * @author Morgan Guimard
 */
public class DocumentIterationNotFoundException extends EntityNotFoundException {
    private final String mDocMId;
    private final String mDocRStringVersion;
    private final Integer mDocIStringIteration;

    public DocumentIterationNotFoundException(String pMessage) {
        super(pMessage);
        mDocMId=null;
        mDocRStringVersion=null;
        mDocIStringIteration=null;
    }

    public DocumentIterationNotFoundException(DocumentIterationKey pKey) {
        this(pKey, null);
    }

    public DocumentIterationNotFoundException(DocumentIterationKey pKey, Throwable pCause) {
        this(pKey.getDocumentMasterId(), pKey.getDocumentRevisionVersion(), pKey.getIteration(),  pCause);
    }

    public DocumentIterationNotFoundException(String pDocMId,
                                              String pDocRStringVersion,
                                              int pDocIStringIteration,
                                              Throwable pCause) {
        super(pCause);
        mDocMId=pDocMId;
        mDocRStringVersion=pDocRStringVersion;
        mDocIStringIteration=pDocIStringIteration;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mDocMId,mDocRStringVersion,mDocIStringIteration);
    }
}
