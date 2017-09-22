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

package org.polarsys.eplmp.server.rest.collections;

import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.query.Query;
import org.polarsys.eplmp.core.query.QueryResultRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan Guimard
 */

public class QueryResult {

    private Query query;
    private List<QueryResultRow> rows = new ArrayList<>();

    private ExportType exportType = ExportType.JSON;

    public QueryResult() {
    }

    public QueryResult(Query query, List<QueryResultRow> rows) {
        this.query = query;
        this.rows = rows;
    }

    public QueryResult(List<PartRevision> partRevisions, Query query) {
        this.query = query;
        for (PartRevision partRevision : partRevisions) {
            rows.add(new QueryResultRow(partRevision));
        }
    }

    public ExportType getExportType() {
        return exportType;
    }

    public void setExportType(ExportType exportType) {
        this.exportType = exportType;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<QueryResultRow> getRows() {
        return rows;
    }

    public void setRows(List<QueryResultRow> rows) {
        this.rows = rows;
    }

    public void mergeRows(List<QueryResultRow> rows) {
        List<QueryResultRow> mergedRows = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            for (QueryResultRow row : rows) {
                for (QueryResultRow filteredRow : this.rows) {
                    if (filteredRow.getPartRevision().equals(row.getPartRevision())) {
                        mergedRows.add(row);
                        break;
                    }
                }
            }
        }
        this.rows = mergedRows;
    }

    public enum ExportType {
        JSON, CSV, XLS
    }


}
