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


import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


/**
 * This class is used for encoding web socket payloads
 * <p>
 * Encode only json WebSocketMessage
 *
 * @author Morgan Guimard
 */
public class WebSocketMessageEncoder implements Encoder.Text<WebSocketMessage> {

    @Override
    public String encode(WebSocketMessage WebSocketMessage) throws EncodeException {
        return WebSocketMessage.getObject().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }
}
