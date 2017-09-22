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

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Query represents, without any surprise, a query which searches for parts.
 * Queries can freely be constructed of compound selections,
 * expressions, predicates, orderings.
 * Moreover they can be saved, to be lately re-executed.
 * They are private to the user who created them.
 *
 * @author Morgan Guimard
 */
@Table(name = "QUERY")
@Entity
@NamedQueries({
        @NamedQuery(name = "Query.findByWorkspace", query = "SELECT q FROM Query q WHERE q.author.workspaceId = :workspaceId"),
        @NamedQuery(name = "Query.findByWorkspaceAndName", query = "SELECT q FROM Query q WHERE q.author.workspaceId = :workspaceId AND q.name = :name")
})
public class Query implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QUERYRULE_ID", nullable = true)
    private QueryRule queryRule;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PATHDATA_QUERYRULE_ID", nullable = true)
    private QueryRule pathDataQueryRule;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "QUERY_SELECTS",
            joinColumns = {
                    @JoinColumn(name = "QUERY_ID", referencedColumnName = "ID")
            }
    )
    private List<String> selects = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "QUERY_ORDER_BY",
            joinColumns = {
                    @JoinColumn(name = "QUERY_ID", referencedColumnName = "ID")
            }
    )
    private List<String> orderByList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "QUERY_GROUPED_BY",
            joinColumns = {
                    @JoinColumn(name = "QUERY_ID", referencedColumnName = "ID")
            }
    )
    private List<String> groupedByList = new ArrayList<>();


    @OneToMany(mappedBy = "parentQuery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QueryContext> contexts = new ArrayList<>();

    public Query() {
    }

    public Query(User author, String name, Date creationDate, QueryRule queryRule, QueryRule pathDataQueryRule, List<String> selects, List<String> orderByList, List<String> groupedByList, List<QueryContext> contexts) {
        this.author = author;
        this.name = name;
        this.creationDate = creationDate;
        this.queryRule = queryRule;
        this.pathDataQueryRule = pathDataQueryRule;
        this.selects = selects;
        this.orderByList = orderByList;
        this.groupedByList = groupedByList;
        this.contexts = contexts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setQueryRule(QueryRule queryRule) {
        this.queryRule = queryRule;
    }

    public QueryRule getQueryRule() {
        return queryRule;
    }

    public QueryRule getPathDataQueryRule() {
        return pathDataQueryRule;
    }

    public void setPathDataQueryRule(QueryRule pathDataQueryRule) {
        this.pathDataQueryRule = pathDataQueryRule;
    }

    public void setRules(QueryRule queryRule) {
        this.queryRule = queryRule;
    }

    public List<String> getSelects() {
        return selects;
    }

    public void setSelects(List<String> selects) {
        this.selects = selects;
    }

    public List<String> getOrderByList() {
        return orderByList;
    }

    public void setOrderByList(List<String> orderByList) {
        this.orderByList = orderByList;
    }

    public List<String> getGroupedByList() {
        return groupedByList;
    }

    public void setGroupedByList(List<String> groupedByList) {
        this.groupedByList = groupedByList;
    }

    public List<QueryContext> getContexts() {
        return contexts;
    }

    public void setContexts(List<QueryContext> contexts) {
        this.contexts = contexts;
    }

    public boolean hasContext() {
        return !contexts.isEmpty();
    }
}
