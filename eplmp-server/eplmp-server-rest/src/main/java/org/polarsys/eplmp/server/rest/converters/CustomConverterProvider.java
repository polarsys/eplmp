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

package org.polarsys.eplmp.server.rest.converters;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Florent Garin
 */
@Provider
public class CustomConverterProvider implements ParamConverterProvider {

    private final DateAdapter dateAdapter = new DateAdapter();

    @Override
    // Safe cast, ignore warning
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] annotations) {
        if (clazz.getName().equals(Date.class.getName())) {
            return (ParamConverter<T>) dateAdapter;
        }
        return null;
    }
}
