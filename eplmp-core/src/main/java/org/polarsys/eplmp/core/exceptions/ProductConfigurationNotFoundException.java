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
public class ProductConfigurationNotFoundException extends EntityNotFoundException {
    private final int mProductConfigurationId;


    public ProductConfigurationNotFoundException(String pMessage) {
        super(pMessage);
        mProductConfigurationId = -1;
    }

    public ProductConfigurationNotFoundException(int pProductConfigurationId) {
        this(pProductConfigurationId, null);
    }

    public ProductConfigurationNotFoundException(int pProductConfigurationId, Throwable pCause) {
        super( pCause);
        mProductConfigurationId =pProductConfigurationId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mProductConfigurationId);
    }
}
