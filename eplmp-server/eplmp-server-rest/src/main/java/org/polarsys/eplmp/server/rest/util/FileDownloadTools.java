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

package org.polarsys.eplmp.server.rest.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Elisabel Généreux
 */
public class FileDownloadTools {

    private static final Logger LOGGER = Logger.getLogger(FileDownloadTools.class.getName());
    private static final String CHARSET = "UTF-8";

    /**
     * Get the output name of file
     *
     * @return Output name of file
     */
    public static String getFileName(String fullName, String outputFormat) {
        String fileName = fullName;

        try {
            fileName = URLEncoder.encode(fileName, CHARSET).replace("+", " ");
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.WARNING, null, e);
        }

        if (outputFormat != null) {
            fileName += "." + outputFormat;
        }

        return fileName;
    }

    /**
     * Get the Content disposition for this file
     *
     * @return Http Response content disposition
     */
    // Todo : may be use "accept" header instead of query param "type"
    public static String getContentDisposition(String downloadType, String fileName) {
        String dispositionType = ("viewer".equals(downloadType)) ? "inline" : "attachment";
        return dispositionType + "; filename=\"" + fileName + "\" ; filename*=\"" + fileName + "\"";
    }

}
