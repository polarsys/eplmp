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
 * @author Florent Garin
 */
public class PartMasterNotFoundException extends EntityNotFoundException {
    private final String mPartM;
    
    public PartMasterNotFoundException(String pPartM) {
        this(pPartM, null);
    }

    public PartMasterNotFoundException(String pPartM, Throwable pCause) {
        super( pCause);
        mPartM=pPartM;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartM);     
    }
}
