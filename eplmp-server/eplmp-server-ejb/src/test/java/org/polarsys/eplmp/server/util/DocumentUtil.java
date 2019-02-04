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

package org.polarsys.eplmp.server.util;

/**
 * @author Asmae CHADID on 09/03/15.
 */

public class DocumentUtil {

    public static final String WORKSPACE_ID = "TestWorkspace";
    public static final String DOCUMENT_ID = "TestDocument";
    public static final String DOCUMENT_TEMPLATE_ID = "temp_1";
    public static final String FILE1_NAME = "uploadedFile";
    public static final String FILE2_NAME = "file_à-tèsté.txt";
    public static final String FILE3_NAME = "file_à-t*st?! .txt";
    public static final String FILE4_NAME = "uploadedFile";
    public static final long DOCUMENT_SIZE = 22;
    public static final String VERSION = "A";
    public static final int ITERATION = 1;
    public static final String FULL_NAME = WORKSPACE_ID + "/documents/" + DOCUMENT_ID + "/" + VERSION + "/" + ITERATION + "/" + FILE1_NAME;
    public static final String FULL_NAME4 = WORKSPACE_ID + "/documents/" + DOCUMENT_ID + "/" + VERSION + "/" + ITERATION + "/" + FILE4_NAME;
    public static final String FOLDER = "newFolder";
    public static final String USER_2_LOGIN = "user2";
    public static final String USER_2_NAME = "user2";
    public static final String USER2_MAIL = "user2@docdoku.com";
    public static final String LANGUAGE = "en";
    public static final String USER_1_LOGIN = "user1";
    public static final String USER_1_NAME = "user1";
    public static final String USER1_MAIL = "user1@docdoku.com";

    public static final String WORKSPACE_DESCRIPTION = "pDescription";

}
