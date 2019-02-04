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

package org.polarsys.eplmp.server.ws.status;


import org.polarsys.eplmp.server.ws.WebSocketMessage;
import org.polarsys.eplmp.server.ws.WebSocketModule;
import org.polarsys.eplmp.server.ws.WebSocketSessionsManager;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.websocket.Session;

/**
 * Status module plugin implementation
 *
 * @author Morgan Guimard
 */

@StatusWebSocketModule
public class StatusWebSocketModuleImpl implements WebSocketModule {

    private static final String USER_STATUS = "USER_STATUS";
    private static final String USER_STATUS_OFFLINE = "USER_STATUS_OFFLINE";
    private static final String USER_STATUS_ONLINE = "USER_STATUS_ONLINE";
    @Inject
    private WebSocketSessionsManager webSocketSessionsManager;

    @Override
    public boolean canDecode(WebSocketMessage webSocketMessage) {
        return USER_STATUS.equals(webSocketMessage.getType());
    }

    @Override
    public void process(Session session, WebSocketMessage webSocketMessage) {

        String sender = webSocketSessionsManager.getHolder(session);
        String remoteUser = webSocketMessage.getString("remoteUser");

        if(! webSocketSessionsManager.isAllowedToReachUser(sender, remoteUser)){
            // should send 403 ?
            return;
        }

        boolean isRemoteUserOnline = webSocketSessionsManager.hasSessions(remoteUser);
        WebSocketMessage message = createMessage(USER_STATUS, remoteUser, isRemoteUserOnline);

        webSocketSessionsManager.send(session,message);

    }

    private WebSocketMessage createMessage(String type, String remoteUser, boolean onlineStatus){

        JsonObjectBuilder b = Json.createObjectBuilder()
                .add("type",type)
                .add("remoteUser", remoteUser)
                .add("status",onlineStatus ? USER_STATUS_ONLINE:USER_STATUS_OFFLINE);

        return new WebSocketMessage(b.build());
    }

}
