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

import org.polarsys.eplmp.core.configuration.PathDataMaster;

import java.text.MessageFormat;
import java.util.Locale;

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


    public PathDataAlreadyExistsException(Locale pLocale, PathDataMaster pPathDataMaster) {
        this(pLocale, pPathDataMaster, null);
    }

    public PathDataAlreadyExistsException(Locale pLocale, PathDataMaster pPathDataMaster, Throwable pCause) {
        super(pLocale, pCause);
        pathDataMaster = pPathDataMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, pathDataMaster);
    }
}
