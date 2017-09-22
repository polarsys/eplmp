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

import org.polarsys.eplmp.core.common.BinaryResource;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class FileAlreadyExistsException extends EntityAlreadyExistsException {
    private final String mFullName;
    
    
    public FileAlreadyExistsException(String pMessage) {
        super(pMessage);
        mFullName=null;
    }
    
    public FileAlreadyExistsException(Locale pLocale, BinaryResource pFile) {
        this(pLocale,pFile.getFullName());
    }

    public FileAlreadyExistsException(Locale pLocale, String pFullName) {
        this(pLocale,pFullName, null);
    }
    
    public FileAlreadyExistsException(Locale pLocale, BinaryResource pFile, Throwable pCause) {
        this(pLocale,pFile.getFullName(),pCause);
    }
    
    public FileAlreadyExistsException(Locale pLocale, String pFullName, Throwable pCause) {
        super(pLocale,pCause);
        mFullName=pFullName;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mFullName);     
    }
}
