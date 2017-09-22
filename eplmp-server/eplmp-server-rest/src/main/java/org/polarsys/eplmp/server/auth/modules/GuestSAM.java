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

package org.polarsys.eplmp.server.auth.modules;

import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.server.auth.AuthServices;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class set the caller group to guest if the requested resource is public
 *
 * @author Morgan Guimard
 */
public class GuestSAM extends CustomSAM {

    private static final Logger LOGGER = Logger.getLogger(GuestSAM.class.getName());

    public GuestSAM() {
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {

        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        LOGGER.log(Level.FINE, "Validating request @" + request.getMethod() + " " + request.getRequestURI());

        CallerPrincipalCallback callerPrincipalCallback = new CallerPrincipalCallback(clientSubject, "");
        GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(clientSubject, new String[]{UserGroupMapping.GUEST_ROLE_ID});
        Callback[] callbacks = {callerPrincipalCallback, groupPrincipalCallback};

        try {
            callbackHandler.handle(callbacks);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new AuthException(e.getMessage());
        }

        return AuthStatus.SUCCESS;

    }

    @Override
    public boolean canHandle(MessageInfo messageInfo) {
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        return AuthServices.isPublicRequestURI(request.getContextPath(), request.getRequestURI());
    }

}
