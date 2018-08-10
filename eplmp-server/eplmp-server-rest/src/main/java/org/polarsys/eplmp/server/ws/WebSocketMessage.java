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

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.Serializable;

/**
 * This class is a container for a WebSocket message
 *
 * @author Morgan Guimard
 */
public class WebSocketMessage implements Serializable {

    protected JsonObject object;

    private static final String DISCRIMINATOR_FIELD = "type";

    public WebSocketMessage(JsonObject object) {
        this.object = object;
    }

    public JsonObject getObject() {
        return object;
    }

    public void setObject(JsonObject object) {
        this.object = object;
    }

    public String getType(){
        return object.getString(DISCRIMINATOR_FIELD);
    }

    public String getString(String key){

        if(!object.containsKey(key)){
            return null;
        }

        JsonValue jsonValue = object.get(key);

        if(jsonValue.getValueType().equals(JsonValue.ValueType.STRING)){
            return object.getString(key);
        }

        return null;
    }

    public JsonObject getJsonObject(String key){
        if(!object.containsKey(key)){
            return null;
        }

        JsonValue jsonValue = object.get(key);

        if(jsonValue.getValueType().equals(JsonValue.ValueType.OBJECT)){
            return object.getJsonObject(key);
        }

        return null;
    }

}
