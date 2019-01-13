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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author morgan on 09/04/15.
 */

@XmlRootElement
@ApiModel(value="QueryDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.query.Query} entity")
public class QueryDTO implements Serializable {

    @ApiModelProperty(value = "Query id")
    private int id;

    @ApiModelProperty(value = "Query name")
    private String name;

    @ApiModelProperty(value = "Query creation date")
    private Date creationDate;

    @ApiModelProperty(value = "Part iteration query rule")
    @XmlElement(nillable = false)
    private QueryRuleDTO queryRule;

    @ApiModelProperty(value = "Path data query rule")
    @XmlElement(nillable = true)
    private QueryRuleDTO pathDataQueryRule;

    @ApiModelProperty(value = "List of select statements")
    private List<String> selects;

    @ApiModelProperty(value = "List of order by statements")
    private List<String> orderByList;

    @ApiModelProperty(value = "List of grouped by statements")
    private List<String> groupedByList;

    @ApiModelProperty(value = "Query context list")
    private List<QueryContextDTO> contexts;

    public QueryDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QueryRuleDTO getQueryRule() {
        return queryRule;
    }

    public void setQueryRule(QueryRuleDTO queryRule) {
        this.queryRule = queryRule;
    }

    public QueryRuleDTO getPathDataQueryRule() {
        return pathDataQueryRule;
    }

    public void setPathDataQueryRule(QueryRuleDTO pathDataQueryRule) {
        this.pathDataQueryRule = pathDataQueryRule;
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

    public List<QueryContextDTO> getContexts() {
        return contexts;
    }

    public void setContexts(List<QueryContextDTO> contexts) {
        this.contexts = contexts;
    }
}
