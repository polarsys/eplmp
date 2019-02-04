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

package org.polarsys.eplmp.core.query;

import java.util.Date;

/**
 * Wraps data needed to perform a basic query on part revisions.
 * Contrary to what the query builder through the {@link Query} class
 * is capable of, the structure of a basic query is fix.
 *
 * This class is not persisted and should be considered as value object.
 *
 * @author Morgan Guimard
 * @version 2.0, 03/01/2014
 * @since V2.0
 */
public class PartSearchQuery extends SearchQuery {
    private String partNumber;
    private String name;
    private Boolean standardPart;

    public PartSearchQuery() {

    }

    public PartSearchQuery(String workspaceId, String queryString, String partNumber, String name, String version,
                           String author, String type, Date creationDateFrom, Date creationDateTo, Date modificationDateFrom,
                           Date modificationDateTo, SearchQuery.AbstractAttributeQuery[] attributes, String[] tags, Boolean standardPart, String content, boolean fetchHeadOnly) {
        super(workspaceId, queryString, version, author, type, creationDateFrom, creationDateTo, modificationDateFrom,
                modificationDateTo, attributes, tags, content, fetchHeadOnly);
        this.partNumber = partNumber;
        this.name = name;
        this.standardPart = standardPart;
    }

    //Getter
    public String getPartNumber() {
        return partNumber;
    }

    public String getName() {
        return name;
    }

    public Boolean isStandardPart() {
        return standardPart;
    }

    //Setter
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStandardPart(Boolean standardPart) {
        this.standardPart = standardPart;
    }
}
