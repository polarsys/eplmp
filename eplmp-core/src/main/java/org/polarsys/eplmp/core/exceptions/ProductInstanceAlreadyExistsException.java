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

import org.polarsys.eplmp.core.configuration.ProductInstanceMaster;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Taylor Labejof
 */
public class ProductInstanceAlreadyExistsException extends EntityAlreadyExistsException {
    private final ProductInstanceMaster productInstanceMaster;


    public ProductInstanceAlreadyExistsException(String pMessage) {
        super(pMessage);
        productInstanceMaster=null;
    }


    public ProductInstanceAlreadyExistsException(Locale pLocale, ProductInstanceMaster productInstanceMaster) {
        this(pLocale, productInstanceMaster, null);
    }

    public ProductInstanceAlreadyExistsException(Locale pLocale, ProductInstanceMaster productInstanceMaster, Throwable pCause) {
        super(pLocale, pCause);
        this.productInstanceMaster=productInstanceMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,productInstanceMaster);
    }


}
