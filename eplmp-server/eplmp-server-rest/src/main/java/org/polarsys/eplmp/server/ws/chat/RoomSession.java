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

import javax.websocket.Session;

public class RoomSession {

    private String login;
    private Session userSession;

    public RoomSession(String login, Session userSession) {
        this.login = login;
        this.userSession = userSession;
    }

    public String getLogin() {
        return login;
    }

    public Session getUserSession() {
        return userSession;
    }
}
