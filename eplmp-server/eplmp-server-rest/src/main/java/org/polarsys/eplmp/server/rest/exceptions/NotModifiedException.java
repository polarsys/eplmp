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
package org.polarsys.eplmp.server.rest.exceptions;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Taylor LABEJOF
 */
public class NotModifiedException extends RestApiException {
    private static final int CACHE_SECOND = 60 * 60 * 24;

    private final String eTag;

    public NotModifiedException(String eTag) {
        super();
        this.eTag = eTag;
    }

    public String getETag() {
        return eTag;
    }

    public Date getExpireDate() {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.SECOND, CACHE_SECOND);
        return expirationDate.getTime();
    }
}
