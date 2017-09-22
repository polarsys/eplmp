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

import org.polarsys.eplmp.core.common.UserGroup;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class UserGroupAlreadyExistsException extends EntityAlreadyExistsException {
    private final UserGroup mUserGroup;
    
    
    public UserGroupAlreadyExistsException(String pMessage) {
        super(pMessage);
        mUserGroup=null;
    }

    public UserGroupAlreadyExistsException(Locale pLocale, UserGroup pUserGroup) {
        this(pLocale, pUserGroup, null);
    }

    public UserGroupAlreadyExistsException(Locale pLocale, UserGroup pUserGroup, Throwable pCause) {
        super(pLocale, pCause);
        mUserGroup=pUserGroup;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mUserGroup);
    }
}
