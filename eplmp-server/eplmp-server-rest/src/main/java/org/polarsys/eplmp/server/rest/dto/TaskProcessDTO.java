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

import java.io.Serializable;


@ApiModel(value="TaskProcessDTO", description="Use this class to process a task")
public class TaskProcessDTO implements Serializable {

    @ApiModelProperty(value = "Task process action")
    private TaskAction action;

    @ApiModelProperty(value = "Task process comment")
    private String comment;

    @ApiModelProperty(value = "Task process signature")
    private String signature;

    public TaskProcessDTO() {
    }

    public TaskProcessDTO(TaskAction action, String comment, String signature) {
        this.action = action;
        this.comment = comment;
        this.signature = signature;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public TaskAction getAction() {
        return action;
    }

    public void setAction(TaskAction action) {
        this.action = action;
    }

}
