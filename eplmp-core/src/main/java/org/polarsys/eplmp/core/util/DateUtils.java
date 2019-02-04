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

package org.polarsys.eplmp.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper class to deal with dates, use a single date pattern defined here
 */
public class DateUtils {

    private DateUtils() {
    }

    private static final String GLOBAL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIMEZONE = "UTC";

    public static Date parse(String s) throws ParseException {
        return parse(s, TIMEZONE);
    }

    public static Date parse(String s, String timeZone) throws ParseException {
        SimpleDateFormat sdf;

        if (s.length() == SHORT_DATE_FORMAT.length()) {
            sdf = getSimpleDateFormat(SHORT_DATE_FORMAT, timeZone);
        } else {
            sdf = getSimpleDateFormat(GLOBAL_DATE_FORMAT, timeZone);
        }

        return sdf.parse(s);
    }

    public static String format(Date d) {
        return getSimpleDateFormat(GLOBAL_DATE_FORMAT, TIMEZONE).format(d);
    }

    private static SimpleDateFormat getSimpleDateFormat(String format, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf;
    }
}
