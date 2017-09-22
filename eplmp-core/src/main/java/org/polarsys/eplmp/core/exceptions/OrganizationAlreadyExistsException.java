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

import org.polarsys.eplmp.core.common.Organization;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class OrganizationAlreadyExistsException extends EntityAlreadyExistsException {
    private final Organization mOrganization;


    public OrganizationAlreadyExistsException(String pMessage) {
        super(pMessage);
        mOrganization=null;
    }

    public OrganizationAlreadyExistsException(Locale pLocale, Organization pOrganization) {
        this(pLocale, pOrganization, null);
    }

    public OrganizationAlreadyExistsException(Locale pLocale, Organization pOrganization, Throwable pCause) {
        super(pLocale, pCause);
        mOrganization=pOrganization;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mOrganization);
    }
}
