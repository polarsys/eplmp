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

package org.polarsys.eplmp.core.query;

import java.util.Date;

/**
 * Wraps data needed to perform a basic query on document revisions.
 * This class is not persisted and should be considered as value object.
 *
 * @author Florent Garin
 * @version 2.0, 03/01/2014
 * @since V2.0
 */
public class DocumentSearchQuery extends SearchQuery {
    private String docMId;
    private String title;
    private String folder;

    public DocumentSearchQuery() {

    }

    public DocumentSearchQuery(String workspaceId, String queryString, String docMId, String title, String version, String author, String type, Date creationDateFrom, Date creationDateTo, Date modificationDateFrom, Date modificationDateTo, SearchQuery.AbstractAttributeQuery[] attributes, String[] tags, String content, String folder, boolean fetchHeadOnly) {
        super(workspaceId, queryString, version, author, type, creationDateFrom, creationDateTo, modificationDateFrom, modificationDateTo, attributes, tags, content, fetchHeadOnly);
        this.docMId = docMId;
        this.title = title;
        this.content = content;
        this.folder = folder;
    }

    public String getDocMId() {
        return docMId;
    }

    public void setDocMId(String docMId) {
        this.docMId = docMId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
