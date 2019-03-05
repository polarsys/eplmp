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
package org.polarsys.eplmp.server.indexer.util;

public class IndexerMapping {

    public static final String INDEX_PREFIX = "eplmp";
    public static final String INDEX_SEPARATOR = "-";
    public static final String INDEX_DOCUMENTS = "documents";
    public static final String INDEX_PARTS = "parts";
    public static final String TYPE = "_doc";

    // patterns: ["eplmp-*"]
    public static final String EPLMP_COMMON = INDEX_PREFIX + INDEX_SEPARATOR + "common";
    public static final String COMMON_TEMPLATE = "/org/polarsys/eplmp/server/indexer/common-template.json";

    // patterns: ["eplmp-*-documents"]
    public static final String EPLMP_DOCUMENTS = INDEX_PREFIX + INDEX_SEPARATOR + INDEX_DOCUMENTS;
    public static final String DOCUMENT_TEMPLATE = "/org/polarsys/eplmp/server/indexer/document-template.json";

    // patterns: ["eplmp-*-parts"]
    public static final String EPLMP_PARTS = INDEX_PREFIX + INDEX_SEPARATOR + INDEX_PARTS;
    public static final String PART_TEMPLATE = "/org/polarsys/eplmp/server/indexer/part-template.json";

    public static final String WORKSPACE_ID_KEY = "workspaceId";
    public static final String ITERATION_KEY = "iteration";
    public static final String VERSION_KEY = "version";
    public static final String AUTHOR_LOGIN_KEY = "authorLogin";
    public static final String AUTHOR_NAME_KEY = "authorName";
    public static final String FILE_NAME_KEY = "fileName";
    public static final String CREATION_DATE_KEY = "creationDate";
    public static final String MODIFICATION_DATE_KEY = "modificationDate";
    public static final String TYPE_KEY = "type";
    public static final String DOCUMENT_ID_KEY = "docMId";
    public static final String PART_NUMBER_KEY = "partNumber";
    public static final String PART_NAME_KEY = "partName";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String REVISION_NOTE_KEY = "revisionNote";
    public static final String WORKFLOW_KEY = "workflow";
    public static final String FOLDER_KEY = "folder";
    public static final String TAGS_KEY = "tags";
    public static final String ATTRIBUTES_KEY = "attributes";
    public static final String ATTRIBUTE_NAME = "attr_name";
    public static final String ATTRIBUTE_VALUE = "attr_value";
    public static final String FILES_KEY = "files";
    public static final String CONTENT_KEY = "content";
    public static final String STANDARD_PART_KEY = "standardPart";

}
