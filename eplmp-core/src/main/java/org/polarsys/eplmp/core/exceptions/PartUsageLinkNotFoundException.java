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
 * @author Florent Garin
 */
public class PartUsageLinkNotFoundException extends EntityNotFoundException {
    private final int mPartUsageLink;

    public PartUsageLinkNotFoundException(String pMessage) {
        super(pMessage);
        mPartUsageLink = -1;
    }

    public PartUsageLinkNotFoundException(int pPartUsageLink) {
        this(pPartUsageLink, null);
    }

    public PartUsageLinkNotFoundException(int pPartUsageLink, Throwable pCause) {
        super( pCause);
        mPartUsageLink=pPartUsageLink;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mPartUsageLink);     
    }
}
