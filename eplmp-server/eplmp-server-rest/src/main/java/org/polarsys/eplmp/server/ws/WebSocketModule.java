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

package org.polarsys.eplmp.server.ws;

import javax.websocket.Session;

/**
 * This interface is an extension point for creating web socket modules
 *
 * @author Morgan Guimard
 */
public interface WebSocketModule {
    boolean canDecode(WebSocketMessage webSocketMessage);
    void process(Session session, WebSocketMessage webSocketMessage);
}
