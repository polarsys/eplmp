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

import org.polarsys.eplmp.core.query.Query;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author Morgan Guimard
 */
public class QueryAlreadyExistsException extends EntityAlreadyExistsException {
    private final Query mQuery;

    public QueryAlreadyExistsException(Locale pLocale, Query query) {
        this(pLocale, query, null);
    }

    public QueryAlreadyExistsException(Locale pLocale, Query pQuery, Throwable pCause) {
        super(pLocale, pCause);
        mQuery=pQuery;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mQuery.getName());
    }
}
