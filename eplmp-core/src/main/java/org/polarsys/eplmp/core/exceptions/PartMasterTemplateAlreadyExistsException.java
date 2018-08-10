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

import org.polarsys.eplmp.core.product.PartMasterTemplate;

import java.text.MessageFormat;


/**
 *
 * @author Morgan Guimard
 */
public class PartMasterTemplateAlreadyExistsException extends EntityAlreadyExistsException {
    private final PartMasterTemplate mPartMTemplate;


    public PartMasterTemplateAlreadyExistsException(String pMessage) {
        super(pMessage);
        mPartMTemplate=null;
    }


    public PartMasterTemplateAlreadyExistsException(PartMasterTemplate pPartMTemplate) {
        this(pPartMTemplate, null);
    }

    public PartMasterTemplateAlreadyExistsException(PartMasterTemplate pPartMTemplate, Throwable pCause) {
        super( pCause);
        mPartMTemplate=pPartMTemplate;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMTemplate);
    }
}
