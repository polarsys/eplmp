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


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * This class is used for decoding web socket payloads
 * <p>
 * Decode only json objects
 * <p>
 * Type field is mandatory
 *
 * @author Morgan Guimard
 */
public class WebSocketMessageDecoder implements Decoder.Text<WebSocketMessage> {

    @Override
    public WebSocketMessage decode(String messageAsString) throws DecodeException {
        JsonObject jsonObj = readAsJson(messageAsString);
        return new WebSocketMessage(jsonObj);
    }

    @Override
    public boolean willDecode(String s) {
        JsonObject jsonObj = readAsJson(s);
        return jsonObj.containsKey("type");
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    private JsonObject readAsJson(String s) {
        JsonReader reader = Json.createReader(new StringReader(s));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        return jsonObject;
    }
}
