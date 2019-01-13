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

import org.polarsys.eplmp.core.configuration.ProductInstanceMaster;

import java.text.MessageFormat;


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


    public ProductInstanceAlreadyExistsException(ProductInstanceMaster productInstanceMaster) {
        this(productInstanceMaster, null);
    }

    public ProductInstanceAlreadyExistsException(ProductInstanceMaster productInstanceMaster, Throwable pCause) {
        super( pCause);
        this.productInstanceMaster=productInstanceMaster;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,productInstanceMaster);
    }


}
