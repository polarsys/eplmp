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

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class WorkflowNotFoundException extends EntityNotFoundException {
    private final int mID;

    public WorkflowNotFoundException(String pMessage) {
        super(pMessage);
        mID=-1;
    }

    public WorkflowNotFoundException(int pID) {
        this(pID, null);
    }

    public WorkflowNotFoundException(int pID, Throwable pCause) {
        super( pCause);
        mID=pID;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mID);     
    }
}
