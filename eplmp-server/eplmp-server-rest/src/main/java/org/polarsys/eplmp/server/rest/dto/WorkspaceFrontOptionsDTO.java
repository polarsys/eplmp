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

@XmlRootElement
@ApiModel(value = "WorkspaceFrontOptionsDTO",
        description = "This class is the representation of a {@link org.polarsys.eplmp.core.admin.WorkspaceFrontOptions} entity"
)
public class WorkspaceFrontOptionsDTO implements Serializable {

    @ApiModelProperty(value = "Part table columns")
    private List<String> partTableColumns;

    @ApiModelProperty(value = "Document table columns")
    private List<String> documentTableColumns;

    public WorkspaceFrontOptionsDTO() {
    }

    public List<String> getPartTableColumns() {
        return partTableColumns;
    }

    public void setPartTableColumns(List<String> partTableColumns) {
        this.partTableColumns = partTableColumns;
    }

    public List<String> getDocumentTableColumns() {
        return documentTableColumns;
    }

    public void setDocumentTableColumns(List<String> documentTableColumns) {
        this.documentTableColumns = documentTableColumns;
    }
}
