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
public class PathDataMasterNotFoundException extends EntityNotFoundException {
    private final Integer mPathDataMasterId;
    private final String mPathDataMasterPath;

    public PathDataMasterNotFoundException(String pPathDataMasterPath) {
        this(pPathDataMasterPath, null);
    }

    public PathDataMasterNotFoundException(String pPathDataMasterPath, Throwable pCause) {
        super( pCause);
        mPathDataMasterPath=pPathDataMasterPath;
        mPathDataMasterId=null;
    }

    public PathDataMasterNotFoundException(Integer pPathDataMasterId) {
        this(pPathDataMasterId, null);
    }

    public PathDataMasterNotFoundException(Integer pPathDataMasterId, Throwable pCause) {
        super( pCause);
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
