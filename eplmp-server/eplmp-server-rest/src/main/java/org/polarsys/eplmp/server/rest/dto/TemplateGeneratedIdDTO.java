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

@XmlRootElement
@ApiModel(value="TemplateGeneratedIdDTO", description="This class provides information on next {@link org.polarsys.eplmp.core.product.PartMasterTemplate} or {@link org.polarsys.eplmp.core.document.DocumentMasterTemplate} generated id")
public class TemplateGeneratedIdDTO implements Serializable {

    @ApiModelProperty(value = "Generated id for template")
    private String id;

    public TemplateGeneratedIdDTO() {
    }

    public TemplateGeneratedIdDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
