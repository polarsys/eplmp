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
package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author lebeaujulien on 03/03/15.
 */
@XmlRootElement
@ApiModel(value="ListOfValuesDTO", description="This class is a representation of a {@link org.polarsys.eplmp.core.meta.ListOfValues} entity")
public class ListOfValuesDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "LOV name")
    private String name;

    @ApiModelProperty(value = "LOV values")
    private List<NameValuePairDTO> values;

    @ApiModelProperty(value = "LOV id")
    private String id;

    private boolean isDeletable = true;

    public ListOfValuesDTO() {
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NameValuePairDTO> getValues() {
        return values;
    }

    public void setValues(List<NameValuePairDTO> values) {
        this.values = values;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean isDeletable) {
        this.isDeletable = isDeletable;
    }
}
