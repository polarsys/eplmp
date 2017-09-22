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
import java.util.Locale;

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

    public DocumentIterationNotFoundException(Locale pLocale, DocumentIterationKey pKey) {
        this(pLocale, pKey, null);
    }

    public DocumentIterationNotFoundException(Locale pLocale, DocumentIterationKey pKey, Throwable pCause) {
        this(pLocale, pKey.getDocumentMasterId(), pKey.getDocumentRevisionVersion(), pKey.getIteration(),  pCause);
    }

    public DocumentIterationNotFoundException(Locale pLocale,
                                              String pDocMId,
                                              String pDocRStringVersion,
                                              int pDocIStringIteration,
                                              Throwable pCause) {
        super(pLocale, pCause);
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
