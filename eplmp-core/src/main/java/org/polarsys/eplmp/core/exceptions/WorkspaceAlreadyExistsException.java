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

import org.polarsys.eplmp.core.common.Workspace;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class WorkspaceAlreadyExistsException extends EntityAlreadyExistsException {
    private final Workspace mWorkspace;

    
    public WorkspaceAlreadyExistsException(String pMessage) {
        super(pMessage);
        mWorkspace=null;
    }
    
    public WorkspaceAlreadyExistsException(Locale pLocale, Workspace pWorkspace) {
        this(pLocale, pWorkspace, null);
    }

    public WorkspaceAlreadyExistsException(Locale pLocale, Workspace pWorkspace, Throwable pCause) {
        super(pLocale, pCause);
        mWorkspace=pWorkspace;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mWorkspace);     
    }
}
