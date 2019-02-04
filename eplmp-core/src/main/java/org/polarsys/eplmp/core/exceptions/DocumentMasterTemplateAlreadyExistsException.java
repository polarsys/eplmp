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

import org.polarsys.eplmp.core.document.DocumentMasterTemplate;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class DocumentMasterTemplateAlreadyExistsException extends EntityAlreadyExistsException {
    private final DocumentMasterTemplate mDocMTemplate;
    
    
    public DocumentMasterTemplateAlreadyExistsException(String pMessage) {
        super(pMessage);
        mDocMTemplate=null;
    }

    public DocumentMasterTemplateAlreadyExistsException(DocumentMasterTemplate pDocMTemplate) {
        this(pDocMTemplate, null);
    }

    public DocumentMasterTemplateAlreadyExistsException(DocumentMasterTemplate pDocMTemplate, Throwable pCause) {
        super( pCause);
        mDocMTemplate=pDocMTemplate;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mDocMTemplate);     
    }
}
