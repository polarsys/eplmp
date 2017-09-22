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
import java.util.Locale;

/**
 *
 * @author Morgan Guimard
 */
public class PathDataMasterNotFoundException extends EntityNotFoundException {
    private final Integer mPathDataMasterId;
    private final String mPathDataMasterPath;

    public PathDataMasterNotFoundException(String pMessage) {
        super(pMessage);
        mPathDataMasterId=null;
        mPathDataMasterPath=null;
    }
    public PathDataMasterNotFoundException(Locale pLocale, String pPathDataMasterPath) {
        this(pLocale, pPathDataMasterPath, null);
    }

    public PathDataMasterNotFoundException(Locale pLocale, String pPathDataMasterPath, Throwable pCause) {
        super(pLocale, pCause);
        mPathDataMasterPath=pPathDataMasterPath;
        mPathDataMasterId=null;
    }

    public PathDataMasterNotFoundException(Locale pLocale, Integer pPathDataMasterId) {
        this(pLocale, pPathDataMasterId, null);
    }

    public PathDataMasterNotFoundException(Locale pLocale, Integer pPathDataMasterId, Throwable pCause) {
        super(pLocale, pCause);
        mPathDataMasterId=pPathDataMasterId;
        mPathDataMasterPath=null;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        if(mPathDataMasterPath!=null)
            return MessageFormat.format(message,mPathDataMasterPath);
        else
            return MessageFormat.format(message,mPathDataMasterId);
    }
}
