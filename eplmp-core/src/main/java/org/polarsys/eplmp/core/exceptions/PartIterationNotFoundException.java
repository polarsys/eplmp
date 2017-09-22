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
import org.polarsys.eplmp.core.product.PartIterationKey;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class PartIterationNotFoundException extends EntityNotFoundException {
    private final String mPartMNumber;
    private final String mPartRStringVersion;
    private final int mPartIIteration;

    public PartIterationNotFoundException(String pMessage) {
        super(pMessage);
        mPartMNumber=null;
        mPartRStringVersion=null;
        mPartIIteration=-1;
    }

    public PartIterationNotFoundException(Locale pLocale, PartIterationKey pPartIPK) {
        this(pLocale, pPartIPK, null);
    }

    public PartIterationNotFoundException(Locale pLocale, PartIterationKey pPartIPK, Throwable pCause) {
        this(pLocale, pPartIPK.getPartMasterNumber(), pPartIPK.getPartRevision().getVersion(), pPartIPK.getIteration(), pCause);
    }

    public PartIterationNotFoundException(Locale pLocale, String pPartMNumber, Version pPartRVersion, int pPartIIteration) {
        this(pLocale, pPartMNumber, pPartRVersion.toString(), pPartIIteration, null);
    }

    public PartIterationNotFoundException(Locale pLocale, String pPartMNumber, String pPartRStringVersion, int pPartIIteration) {
        this(pLocale, pPartMNumber, pPartRStringVersion, pPartIIteration, null);
    }

    public PartIterationNotFoundException(Locale pLocale, String pPartMNumber, String pPartRStringVersion, int pPartIIteration, Throwable pCause) {
        super(pLocale, pCause);
        mPartMNumber=pPartMNumber;
        mPartRStringVersion=pPartRStringVersion;
        mPartIIteration=pPartIIteration;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartMNumber,mPartRStringVersion, mPartIIteration);
    }
}
