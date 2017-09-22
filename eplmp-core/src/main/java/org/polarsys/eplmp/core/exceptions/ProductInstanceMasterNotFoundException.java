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

import org.polarsys.eplmp.core.configuration.ProductInstanceMasterKey;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class ProductInstanceMasterNotFoundException extends EntityNotFoundException {
    private final ProductInstanceMasterKey mProductInstanceMaster;

    public ProductInstanceMasterNotFoundException(String pMessage) {
        super(pMessage);
        mProductInstanceMaster=null;
    }

    public ProductInstanceMasterNotFoundException(Locale pLocale, ProductInstanceMasterKey pProductInstanceMaster) {
        this(pLocale, pProductInstanceMaster, null);
    }

    public ProductInstanceMasterNotFoundException(Locale pLocale, ProductInstanceMasterKey pProductInstanceMaster, Throwable pCause) {
        super(pLocale, pCause);
        mProductInstanceMaster=pProductInstanceMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mProductInstanceMaster);
    }
}
