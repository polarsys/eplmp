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

package org.polarsys.eplmp.server.rest.exceptions;

/**
 * @author Julien Maffre
 */
public class RequestedRangeNotSatisfiableException extends RestApiException {
    private final String resource;
    private final long contentRange;

    public RequestedRangeNotSatisfiableException(String resource, long contentRange) {
        this.resource = resource;
        this.contentRange = contentRange;
    }

    @Override
    public String getMessage() {
        return "Http requested range is not satisfiable for " + resource;
    }

    public long getContentRange() {
        return contentRange;
    }
}
