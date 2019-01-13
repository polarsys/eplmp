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
 * @author Taylor LABEJOF
 */
public class PreconditionFailedException extends RestApiException {
    private final String resource;


    public PreconditionFailedException(String resource) {
        super();
        this.resource = resource;
    }

    @Override
    public String getMessage() {
        return "Http preconditions have failed for " + resource;
    }
}
