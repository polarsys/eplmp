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
public class PartMasterTemplateNotFoundException extends EntityNotFoundException {
    private final String mPartMTemplateId;

    public PartMasterTemplateNotFoundException(String pMessage) {
        super(pMessage);
        mPartMTemplateId=null;
    }

    public PartMasterTemplateNotFoundException(Locale pLocale, String pPartMTemplateID) {
        this(pLocale, pPartMTemplateID, null);
    }

    public PartMasterTemplateNotFoundException(Locale pLocale, String pPartMTemplateId, Throwable pCause) {
        super(pLocale, pCause);
        mPartMTemplateId=pPartMTemplateId;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMTemplateId);
    }
}
