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


/**
 *
 * @author Florent Garin
 */
public class FileAlreadyExistsException extends EntityAlreadyExistsException {
    private final String mFullName;

    public FileAlreadyExistsException(BinaryResource pFile) {
        this(pFile.getFullName());
    }

    public FileAlreadyExistsException(String pFullName) {
        this(pFullName, null);
    }
    
    public FileAlreadyExistsException(BinaryResource pFile, Throwable pCause) {
        this(pFile.getFullName(),pCause);
    }
    
    public FileAlreadyExistsException(String pFullName, Throwable pCause) {
        super(pCause);
        mFullName=pFullName;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mFullName);     
    }
}
