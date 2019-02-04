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

import org.polarsys.eplmp.core.meta.Folder;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class FolderAlreadyExistsException extends EntityAlreadyExistsException {
    private final Folder mFolder;

    public FolderAlreadyExistsException(String pMessage) {
        super(pMessage);
        mFolder=null;
    }
    
    public FolderAlreadyExistsException(Folder pFolder) {
        this(pFolder, null);
    }

    public FolderAlreadyExistsException(Folder pFolder, Throwable pCause) {
        super( pCause);
        mFolder=pFolder;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mFolder);     
    }
}
