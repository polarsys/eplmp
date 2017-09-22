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

package org.polarsys.eplmp.server.ws.chat;


import org.polarsys.eplmp.server.ws.WebSocketMessage;
import org.polarsys.eplmp.server.ws.WebSocketModule;
import org.polarsys.eplmp.server.ws.WebSocketSessionsManager;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.Session;

/**
 * Implementation of chat module
 *
 * @author Morgan Guimard
 */

@ChatWebSocketModule
public class ChatWebSocketModuleImpl implements WebSocketModule {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private static final String CHAT_MESSAGE_ACK = "CHAT_MESSAGE_ACK";
    public static final String CHAT_MESSAGE_UNREACHABLE = "UNREACHABLE";

    @Inject
    private WebSocketSessionsManager webSocketSessionsManager;

    @Override
    public boolean canDecode(WebSocketMessage webSocketMessage) {
        return CHAT_MESSAGE.equals(webSocketMessage.getType());
    }

    @Override
    public void process(Session session, WebSocketMessage webSocketMessage) {

        String sender = webSocketSessionsManager.getHolder(session);
        String remoteUser = webSocketMessage.getString("remoteUser");

        if (!webSocketSessionsManager.isAllowedToReachUser(sender, remoteUser)) {
            // Maybe send 403 ?
            return;
        }

        String context = webSocketMessage.getString("context");
        String message = webSocketMessage.getString("message");

        if (!webSocketSessionsManager.hasSessions(remoteUser)) {
            // Tell the sender the remote is offline
            WebSocketMessage messageForSender = createMessage(CHAT_MESSAGE, remoteUser, "", "", context, CHAT_MESSAGE_UNREACHABLE);
            webSocketSessionsManager.send(session, messageForSender);
        } else {
            WebSocketMessage messageForSender = createMessage(CHAT_MESSAGE_ACK, remoteUser, sender, message, context, "");
            WebSocketMessage messageForRemoteUser = createMessage(CHAT_MESSAGE, sender, sender, message, context, "");
            // Broadcast to both sender/remoteUser the message on each opened sessions
            webSocketSessionsManager.broadcast(sender, messageForSender);
            webSocketSessionsManager.broadcast(remoteUser, messageForRemoteUser);
        }
    }

    private WebSocketMessage createMessage(String type, String remoteUser, String sender, String message, String context, String error) {

        JsonObjectBuilder b = Json.createObjectBuilder()
                .add("type", type)
                .add("remoteUser", remoteUser)
                .add("sender", sender)
                .add("message", message);

        if (null != context) {
            b.add("context", context);
        }

        if (error != null) {
            b.add("error", error);
        }

        return new WebSocketMessage(b.build());
    }

}
