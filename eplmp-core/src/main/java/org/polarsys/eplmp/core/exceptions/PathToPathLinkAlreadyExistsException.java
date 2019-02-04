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

import org.polarsys.eplmp.core.product.PathToPathLink;

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class PathToPathLinkAlreadyExistsException extends EntityAlreadyExistsException {

    private final PathToPathLink mPathToPathLink;

    public PathToPathLinkAlreadyExistsException(String pMessage) {
        super(pMessage);
        mPathToPathLink =null;
    }

    public PathToPathLinkAlreadyExistsException(PathToPathLink pPathToPathLink) {
        this(pPathToPathLink, null);
    }

    public PathToPathLinkAlreadyExistsException(PathToPathLink pPathToPathLink, Throwable pCause) {
        super( pCause);
        mPathToPathLink = pPathToPathLink;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mPathToPathLink);
    }
}
