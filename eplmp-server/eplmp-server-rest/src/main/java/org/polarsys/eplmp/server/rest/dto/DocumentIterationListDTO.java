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
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value="DocumentIterationListDTO", description="This class wraps a list of {@link org.polarsys.eplmp.core.document.DocumentIteration} entities")
public class DocumentIterationListDTO implements Serializable {

    @ApiModelProperty(value = "List of document iterations")
    private List<DocumentIterationDTO> documents;

    public DocumentIterationListDTO() {
    }

    public List<DocumentIterationDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentIterationDTO> documents) {
        this.documents = documents;
    }
}
