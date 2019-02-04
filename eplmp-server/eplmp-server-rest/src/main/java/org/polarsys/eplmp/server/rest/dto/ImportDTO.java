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
import java.util.Date;
import java.util.List;

@XmlRootElement
@ApiModel(value="ImportDTO", description="This class is a representation of an {@link org.polarsys.eplmp.core.product.Import} entity")
public class ImportDTO implements Serializable {

    @ApiModelProperty(value = "Import id")
    private String id;

    @ApiModelProperty(value = "Imported file name")
    private String fileName;

    @ApiModelProperty(value = "Import end date")
    private Date endDate;

    @ApiModelProperty(value = "Import start date")
    private Date startDate;

    @ApiModelProperty(value = "Success flag")
    private boolean succeed;

    @ApiModelProperty(value = "Pending flag")
    private boolean pending;

    @ApiModelProperty(value = "Potential errors")
    private List<String> errors;

    @ApiModelProperty(value = "Potential warnings")
    private List<String> warnings;

    public ImportDTO() {
    }

    public ImportDTO(String id, String fileName, Date endDate, Date startDate, boolean succeed, boolean pending, List<String> errors, List<String> warnings) {
        this.id = id;
        this.fileName = fileName;
        this.endDate = endDate;
        this.startDate = startDate;
        this.succeed = succeed;
        this.pending = pending;
        this.errors = errors;
        this.warnings = warnings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
