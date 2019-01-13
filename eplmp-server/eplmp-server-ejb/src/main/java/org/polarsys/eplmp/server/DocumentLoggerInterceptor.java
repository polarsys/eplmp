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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@LogDocument
@Interceptor
public class DocumentLoggerInterceptor implements Serializable {

    @Inject
    private IDocumentManagerLocal documentManager;

    @Inject
    private IContextManagerLocal contextManager;

    private static final Logger LOGGER = Logger.getLogger(DocumentLoggerInterceptor.class.getName());
    private static final String DOWNLOAD_EVENT = "DOWNLOAD";

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {

        Object result = ctx.proceed();

        Object[] parameters = ctx.getParameters();
        boolean isRoleAllowed = contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID);

        if (isRoleAllowed && parameters != null && parameters.length > 0 && parameters[0] instanceof String) {
            // Not reliable condition, should be reviewed
            String fullName = (String) parameters[0];
            try {
                documentManager.logDocument(fullName, DOWNLOAD_EVENT);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }
}
