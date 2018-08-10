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

import org.polarsys.eplmp.core.workflow.Role;

import java.text.MessageFormat;


/**
 * @author Taylor Labejof
 */
public class RoleAlreadyExistsException extends EntityAlreadyExistsException {
    private final Role mRole;

    public RoleAlreadyExistsException(Role pRole) {
        this(pRole, null);
    }

    public RoleAlreadyExistsException(Role pRole, Throwable pCause) {
        super( pCause);
        mRole=pRole;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mRole.getName());
    }
}
