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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author morgan on 09/04/15.
 */

@XmlRootElement
@ApiModel(value="QueryRuleDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.query.QueryRule} entity")
public class QueryRuleDTO implements Serializable {

    @ApiModelProperty(value = "Rule condition")
    private String condition;

    @ApiModelProperty(value = "Rule id")
    private String id;

    @ApiModelProperty(value = "Rule field")
    private String field;

    @ApiModelProperty(value = "Rule type")
    private String type;

    @ApiModelProperty(value = "Rule operator")
    private String operator;

    @ApiModelProperty(value = "Rule values")
    private List<String> values;

    @ApiModelProperty(value = "Rule sub rules")
    private List<QueryRuleDTO> rules;

    public QueryRuleDTO() {
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<QueryRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<QueryRuleDTO> rules) {
        this.rules = rules;
    }

    public List<QueryRuleDTO> getSubQueryRules() {
        return getRules();
    }

    public void setSubQueryRules(List<QueryRuleDTO> rules) {
        setRules(rules);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
