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

package org.polarsys.eplmp.server.util;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Asmae CHADID on 07/01/15.
 */
public class ResourceUtil {
    public static final String WORKSPACE_ID = "TestWorkspace";
    public static final String DOCUMENT_ID = "TestDocument";
    public static final String VERSION = "A";
    public static final int ITERATION = 1;
    public static final String SOURCE_FILE_STORAGE = "org/polarsys/eplmp/server/rest/file/toUpload/";
    public static final String TARGET_FILE_STORAGE = System.getProperty("java.io.tmpdir") + "org/polarsys/eplmp/server/rest/file/uploaded/";
    public static final String FILENAME1 = "TestFile.txt";
    public static final String FILENAME2 = "TestFile_With_éàè.txt";
    public static final String FILENAME3 = "TestFile_3.txt";

    public static final String DOC_TEMPLATE_ID = "temp_01";
    public static final String FILE_TYPE = "application/pdf";
    public static final String SHARED_DOC_ENTITY_UUID = "documents/share/shareuuid01";
    public static final String SHARED_PART_ENTITY_UUID = "parts/share/123652-85963";
    public static final String RANGE = "bytes=0-366828";
    public static final long DOCUMENT_SIZE = 26;
    public static final String PART_TEMPLATE_ID = "part_templ_01";
    public static final String TEST_PART_FILENAME1 = "part_file_test1.txt";
    public static final String TEST_PART_FILENAME2 = "part_file_test2.txt";
    public static final String PART_NUMBER = "PART01";
    public static final java.lang.String TEMP_SUFFIX = ".tmp";

    public static final String FILENAME_TO_UPLOAD_PART_SPECIAL_CHARACTER = "TestFile_With_éàè.txt";
    public static final long PART_SIZE = 363666;
    public static final String TARGET_PART_STORAGE = System.getProperty("java.io.tmpdir") + "/org/polarsys/eplmp/server/rest/part/uploaded/";
    public static final String SOURCE_PART_STORAGE = "org/polarsys/eplmp/server/rest/part/toUpload/";
    public static final String FILENAME_TARGET_PART = "new_part_file.txt";


    public static String getFilePath(String resourceName) {
        URL resource = ResourceUtil.class.getClassLoader().getResource(resourceName);
        return resource != null ? resource.getFile() : null;
    }

    public static Date getFutureDate() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR, 2);
        return calendar.getTime();
    }
}
