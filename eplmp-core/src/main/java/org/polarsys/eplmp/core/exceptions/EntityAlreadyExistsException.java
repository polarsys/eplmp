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

import java.util.Locale;

/**
 * Base class for implementing an Exception that represents a failed attempt
 * to create an entity because it already exists in the persistent store.
 *
 * @author Taylor Labejof
 */
public abstract class EntityAlreadyExistsException extends ApplicationException {

    public EntityAlreadyExistsException(String pMessage) {
        super(pMessage);
    }

    public EntityAlreadyExistsException(Locale pLocale) {
        super(pLocale);
    }

    public EntityAlreadyExistsException(Locale pLocale, Throwable pCause) {
        super(pLocale, pCause);
    }
}
