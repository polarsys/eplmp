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


import org.polarsys.eplmp.core.util.DateUtils;

import javax.ws.rs.ext.ParamConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Charles Fallourd
 */
public class DateAdapter extends XmlAdapter<String, Date> implements ParamConverter<Date> {


    private static final Logger LOGGER = Logger.getLogger(DateAdapter.class.getName());

    public String marshal(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtils.format(date);
    }

    public Date unmarshal(String dateString) {
        Date d = null;
        try {
            d = DateUtils.parse(dateString);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error unmarshalling date", e);
        }
        return d;
    }

    @Override
    public Date fromString(String s) {
        return unmarshal(s);
    }

    @Override
    public String toString(Date date) {
        return marshal(date);
    }
}
