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

import javax.persistence.*;
import java.io.Serializable;

/**
 * A QueryContext is the scope on which a {@link Query} is executed. So it is
 * a constituent part of a query.
 * It determines if the query will fetch parts from the whole catalog,
 * a specific product or more precisely from a deliverable instance.
 *
 * @author Morgan Guimard
 */
@Table(name="QUERYCONTEXT")
@Entity
public class QueryContext implements Serializable{

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private int id;

    private String workspaceId;

    private String serialNumber;

    private String configurationItemId;

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="QUERY_ID", referencedColumnName="ID"),

    })
    private Query parentQuery;

    public QueryContext() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(String configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Query getParentQuery() {
        return parentQuery;
    }

    public void setParentQuery(Query parentQuery) {
        this.parentQuery = parentQuery;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
}
