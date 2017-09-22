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

package org.polarsys.eplmp.server.converters;


import java.io.*;

/**
 * This ConverterUtils class expose util methods around files conversion
 */
public class ConverterUtils {

    private ConverterUtils() {
    }

    /**
     * Returns input stream content as String.
     * Use it to get info and error messages from process output
     *
     * @param is an InputStream
     * @return the representation as String
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        try (InputStreamReader isr = new InputStreamReader(is, "UTF-8"); BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

}
