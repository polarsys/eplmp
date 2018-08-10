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

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class PathToPathLinkNotFoundException extends EntityNotFoundException {

    private final int mPathToPathLinkId;

    public PathToPathLinkNotFoundException(String pMessage) {
        super(pMessage);
        mPathToPathLinkId =0;
    }

    public PathToPathLinkNotFoundException(int pPathToPathLinkId) {
        this(pPathToPathLinkId, null);
    }

    public PathToPathLinkNotFoundException(int pPathToPathLinkId, Throwable pCause) {
        super( pCause);
        mPathToPathLinkId =pPathToPathLinkId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mPathToPathLinkId);
    }
}
