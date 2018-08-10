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

import org.polarsys.eplmp.core.document.DocumentMaster;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class DocumentMasterAlreadyExistsException extends EntityAlreadyExistsException {
    private final DocumentMaster mDocM;
    
    
    public DocumentMasterAlreadyExistsException(String pMessage) {
        super(pMessage);
        mDocM = null;
    }

    public DocumentMasterAlreadyExistsException(DocumentMaster pDocM) {
        this(pDocM, null);
    }

    public DocumentMasterAlreadyExistsException(DocumentMaster pDocM, Throwable pCause) {
        super( pCause);
        mDocM=pDocM;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mDocM);     
    }
}
