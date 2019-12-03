/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.converters.serialization;

import org.apache.kafka.common.serialization.Serializer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Map;

/**
 * @author
 */
public class JsonbSerializer<T> implements Serializer<T> {

    private Jsonb jsonb = JsonbBuilder.create();


    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, T t) {
        return jsonb.toJson(t).getBytes();
    }

}
