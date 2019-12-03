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

import org.apache.kafka.common.serialization.Deserializer;
import org.polarsys.eplmp.server.converters.ConversionOrder;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author
 */
public class ConversionOrderDeserializer implements Deserializer<ConversionOrder> {

    private final static Logger LOGGER = Logger.getLogger(ConversionOrderDeserializer.class.getName());
    private Jsonb jsonb = JsonbBuilder.create();

    public ConversionOrderDeserializer() {
    }

    @Override
    public ConversionOrder deserialize(String s, byte[] bytes) {
        try {
            return jsonb.fromJson(new String(bytes), ConversionOrder.class);
        } catch (JsonbException e){
            LOGGER.warning("Cannot deserialize " + s);
            return null;
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }
}
