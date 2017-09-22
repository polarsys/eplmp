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
package org.polarsys.eplmp.server.rest.dto.change;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Morgan Guimard
 */

@XmlRootElement
@ApiModel(value="ChangeIssueListDTO", description="This class holds a list of {@link org.polarsys.eplmp.core.change.ChangeIssue} entities")
public class ChangeIssueListDTO implements Serializable {

    @ApiModelProperty(value = "The list of issues")
    private List<ChangeIssueDTO> issues;

    public ChangeIssueListDTO() {
    }

    public List<ChangeIssueDTO> getIssues() {
        return issues;
    }

    public void setIssues(List<ChangeIssueDTO> issues) {
        this.issues = issues;
    }
}
