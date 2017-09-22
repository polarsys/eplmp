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

import org.polarsys.eplmp.core.workflow.RoleKey;

import java.text.MessageFormat;
import java.util.Locale;


/**
 * @author Morgan Guimard
 */
public class RoleNotFoundException extends EntityNotFoundException {
    private final RoleKey mRoleKey;

    public RoleNotFoundException(String pMessage) {
        super(pMessage);
        mRoleKey=null;
    }

    public RoleNotFoundException(Locale pLocale, RoleKey pRoleKey) {
        super(pLocale);
        mRoleKey=pRoleKey;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message, mRoleKey);
    }
}
