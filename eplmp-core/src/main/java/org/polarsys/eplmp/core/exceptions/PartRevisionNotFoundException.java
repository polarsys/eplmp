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


import org.polarsys.eplmp.core.common.Version;
import org.polarsys.eplmp.core.product.PartRevisionKey;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class PartRevisionNotFoundException extends EntityNotFoundException {
    private final String mPartMNumber;
    private final String mPartRStringVersion;

    public PartRevisionNotFoundException(String pMessage) {
        super(pMessage);
        mPartMNumber=null;
        mPartRStringVersion=null;
    }

    public PartRevisionNotFoundException(Locale pLocale, PartRevisionKey pPartRPK) {
        this(pLocale, pPartRPK, null);
    }

    public PartRevisionNotFoundException(Locale pLocale, PartRevisionKey pPartRPK, Throwable pCause) {
        this(pLocale, pPartRPK.getPartMaster().getNumber(), pPartRPK.getVersion(), pCause);
    }

    public PartRevisionNotFoundException(Locale pLocale, String pPartMNumber, Version pPartRVersion) {
        this(pLocale, pPartMNumber, pPartRVersion.toString(), null);
    }

    public PartRevisionNotFoundException(Locale pLocale, String pPartMNumber, String pPartRStringVersion) {
        this(pLocale, pPartMNumber, pPartRStringVersion, null);
    }

    public PartRevisionNotFoundException(Locale pLocale, String pPartMNumber, String pPartRStringVersion, Throwable pCause) {
        super(pLocale, pCause);
        mPartMNumber=pPartMNumber;
        mPartRStringVersion=pPartRStringVersion;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMNumber,mPartRStringVersion);     
    }
}
