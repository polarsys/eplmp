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

import org.polarsys.eplmp.core.product.PartMaster;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class PartMasterAlreadyExistsException extends EntityAlreadyExistsException {
    private final PartMaster mPartMaster;
    
    
    public PartMasterAlreadyExistsException(String pMessage) {
        super(pMessage);
        mPartMaster = null;
    }
    
    
    public PartMasterAlreadyExistsException(PartMaster pPartMaster) {
        this(pPartMaster, null);
    }

    public PartMasterAlreadyExistsException(PartMaster pPartMaster, Throwable pCause) {
        super( pCause);
        mPartMaster=pPartMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMaster);     
    }
}
