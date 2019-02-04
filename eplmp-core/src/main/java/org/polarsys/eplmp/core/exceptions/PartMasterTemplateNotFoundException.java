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
public class PartMasterTemplateNotFoundException extends EntityNotFoundException {
    private final String mPartMTemplateId;

    public PartMasterTemplateNotFoundException(String pPartMTemplateID) {
        this(pPartMTemplateID, null);
    }

    public PartMasterTemplateNotFoundException(String pPartMTemplateId, Throwable pCause) {
        super( pCause);
        mPartMTemplateId=pPartMTemplateId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMTemplateId);
    }
}
