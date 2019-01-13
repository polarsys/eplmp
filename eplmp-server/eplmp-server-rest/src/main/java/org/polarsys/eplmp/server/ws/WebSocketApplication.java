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

package org.polarsys.eplmp.server.ws;

import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.auth.AuthConfig;
import org.polarsys.eplmp.server.auth.jwt.JWTokenFactory;
import org.polarsys.eplmp.server.auth.jwt.JWTokenUserGroupMapping;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class expose a web socket end point on path {contextPath}/ws
 * <p>
 * It maintains the list of current users sockets, receive, process and broadcast messages.
 * Authentication is made by passing a jwt token on first message.
 * <p>
 */
@ServerEndpoint(
        value = "/ws",
        decoders = {
                WebSocketMessageDecoder.class
        },
        encoders = {
                WebSocketMessageEncoder.class
        }
)
public class WebSocketApplication {

    private static final Logger LOGGER = Logger.getLogger(WebSocketApplication.class.getName());

    private static final String AUTH = "AUTH";

    @Inject
    @Any
    private Instance<WebSocketModule> webSocketModules;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private WebSocketSessionsManager webSocketSessionsManager;

    @Inject
    private AuthConfig authConfig;

    public WebSocketApplication() {
    }

    @OnError
    public void error(Session session, Throwable error) {
        LOGGER.log(Level.SEVERE, "WebSocket error", error);
        unTrackSession(session);
    }

    @OnClose
    public void close(Session session, CloseReason reason) {
        LOGGER.log(Level.FINE, "WebSocket closed with message '" +
                reason.getReasonPhrase() + "' and code " + reason.getCloseCode());
        unTrackSession(session);
    }

    @OnOpen
    public void open(Session session) {
        session.getUserProperties().put(AUTH, null);
    }

    @OnMessage
    public void message(Session session, WebSocketMessage message) {
        if (null == session.getUserProperties().get(AUTH)) {
            authenticateOrClose(session, message);
            return;
        }

        // Modules are responsible for :
        // Exit if caller is not allowed to reach callee (business)
        // Prevent caller to join himself

        WebSocketModule selectedModule = selectModule(message);

        if (null != selectedModule) {
            selectedModule.process(session, message);
        } else {
            LOGGER.log(Level.WARNING, "No modules for type " + message.getType());
        }

    }

    private void authenticateOrClose(Session session, WebSocketMessage message) {

        String type = message.getType();

        if (AUTH.equals(type)) {
            String jwt = message.getString("jwt");

            JWTokenUserGroupMapping jwTokenUserGroupMapping = JWTokenFactory.validateAuthToken(authConfig.getJWTKey(), jwt);

            if (null != jwTokenUserGroupMapping) {
                UserGroupMapping userGroupMapping = jwTokenUserGroupMapping.getUserGroupMapping();
                String login = userGroupMapping.getLogin();
                if (login != null) {
                    session.getUserProperties().put(AUTH, jwt);
                    webSocketSessionsManager.addSession(login, session);
                    return;
                }
            }
        }

        // Authentication failed, close socket
        closeSession(session);
        unTrackSession(session);

    }

    private WebSocketModule selectModule(WebSocketMessage webSocketMessage) {
        for (WebSocketModule webSocketModule : webSocketModules) {
            if (webSocketModule.canDecode(webSocketMessage)) {
                return webSocketModule;
            }
        }
        return null;
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    private void unTrackSession(Session session) {
        if (null != session.getUserProperties().get(AUTH)) {
            webSocketSessionsManager.removeSession(session);
        }
    }

}
