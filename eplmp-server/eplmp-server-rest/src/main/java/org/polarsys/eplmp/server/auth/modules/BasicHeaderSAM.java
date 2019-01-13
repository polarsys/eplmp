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

package org.polarsys.eplmp.server.auth.modules;

import org.polarsys.eplmp.core.common.Account;
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
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of basic authentication in requests headers
 *
 * @author Morgan Guimard
 */
public class BasicHeaderSAM extends CustomSAM {

    private static final Logger LOGGER = Logger.getLogger(BasicHeaderSAM.class.getName());

    public BasicHeaderSAM() {
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {

        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();

        LOGGER.log(Level.FINE, "Validating request @" + request.getMethod() + " " + request.getRequestURI());

        String authorization = request.getHeader("Authorization");
        String[] splitAuthorization = authorization.split(" ");

        byte[] decoded = DatatypeConverter.parseBase64Binary(splitAuthorization[1]);

        String credentials;

        try {
            credentials = new String(decoded, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return AuthStatus.FAILURE;
        }

        String[] splitCredentials = credentials.split(":");

        String login = splitCredentials[0];
        String password = splitCredentials[1];

        Account account = AuthServices.authenticateAccount(login, password);
        UserGroupMapping userGroupMapping = AuthServices.getUserGroupMapping(login);

        if (account != null) {
            CallerPrincipalCallback callerPrincipalCallback = new CallerPrincipalCallback(clientSubject, login);
            GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(clientSubject, new String[]{userGroupMapping.getGroupName()});
            Callback[] callbacks = new Callback[]{callerPrincipalCallback, groupPrincipalCallback};

            try {
                callbackHandler.handle(callbacks);
            } catch (IOException | UnsupportedCallbackException e) {
                LOGGER.log(Level.SEVERE, null, e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return AuthStatus.FAILURE;
            }

            return AuthStatus.SUCCESS;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return AuthStatus.FAILURE;

    }

    @Override
    public boolean canHandle(MessageInfo messageInfo) {
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        String authorization = request.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Basic ") && authorization.split(" ").length == 2;
    }
}
