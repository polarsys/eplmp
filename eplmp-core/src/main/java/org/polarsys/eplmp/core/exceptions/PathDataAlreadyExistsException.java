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

import org.polarsys.eplmp.core.configuration.PathDataMaster;

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class PathDataAlreadyExistsException extends EntityAlreadyExistsException {

    private final PathDataMaster pathDataMaster;

    public PathDataAlreadyExistsException(String pMessage) {
        super(pMessage);
        pathDataMaster = null;
    }


    public PathDataAlreadyExistsException(PathDataMaster pPathDataMaster) {
        this(pPathDataMaster, null);
    }

    public PathDataAlreadyExistsException(PathDataMaster pPathDataMaster, Throwable pCause) {
        super( pCause);
        pathDataMaster = pPathDataMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, pathDataMaster);
    }
}
