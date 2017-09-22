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

import org.polarsys.eplmp.core.common.Version;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class DocumentRevisionNotFoundException extends EntityNotFoundException {
    private final String mDocMId;
    private final String mDocRStringVersion;

    public DocumentRevisionNotFoundException(String pMessage) {
        super(pMessage);
        mDocMId=null;
        mDocRStringVersion=null;
    }

    public DocumentRevisionNotFoundException(Locale pLocale, DocumentRevisionKey pDocRPK) {
        this(pLocale, pDocRPK, null);
    }

    public DocumentRevisionNotFoundException(Locale pLocale, DocumentRevisionKey pDocRPK, Throwable pCause) {
        this(pLocale, pDocRPK.getDocumentMaster().getId(), pDocRPK.getVersion(), pCause);
    }

    public DocumentRevisionNotFoundException(Locale pLocale, String pDocMID, Version pDocRVersion) {
        this(pLocale, pDocMID, pDocRVersion.toString(), null);
    }

    public DocumentRevisionNotFoundException(Locale pLocale, String pDocMId, String pDocRStringVersion) {
        this(pLocale, pDocMId, pDocRStringVersion, null);
    }

    public DocumentRevisionNotFoundException(Locale pLocale, String pDocMId, String pDocRStringVersion, Throwable pCause) {
        super(pLocale, pCause);
        mDocMId=pDocMId;
        mDocRStringVersion=pDocRStringVersion;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mDocMId,mDocRStringVersion);
    }
}
