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
import java.util.ArrayList;
import java.util.List;

/**
 * QueryRule is the "where" element of a {@link Query}.
 *
 * @author Morgan Guimard
 */
@Table(name="QUERYRULE")
@Entity
public class QueryRule implements Serializable {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private int qid;

    @Column(name="COND")
    private String condition;
    private String id;
    private String field;
    private String type;
    private String operator;

    @Column(name="VALUE")
    @OrderColumn(name="VALUE_ORDER")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "QUERYRULE_VALUES",
            joinColumns= {
                    @JoinColumn(name = "QUERYRULE_ID", referencedColumnName = "QID")
            }
    )
    private List<String> values=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "PARENT_QUERY_RULE")
    private QueryRule parentQueryRule;

    @OneToMany(mappedBy = "parentQueryRule", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @OrderBy("qid ASC")
    private List<QueryRule> subQueryRules;

    public QueryRule() {
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QueryRule getParentQueryRule() {
        return parentQueryRule;
    }

    public void setParentQueryRule(QueryRule parentQueryRule) {
        this.parentQueryRule = parentQueryRule;
    }

    public List<QueryRule> getSubQueryRules() {
        return subQueryRules;
    }

    public void setSubQueryRules(List<QueryRule> subQueryRules) {
        this.subQueryRules = subQueryRules;
    }

    public boolean hasSubRules() {
        return getSubQueryRules() != null && !getSubQueryRules().isEmpty();
    }
}
