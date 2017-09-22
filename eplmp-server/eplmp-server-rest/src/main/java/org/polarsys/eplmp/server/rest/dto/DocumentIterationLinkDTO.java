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

@XmlRootElement
@ApiModel(value="DocumentIterationLinkDTO", description="This class is the representation of a {@link org.polarsys.eplmp.core.document.DocumentIterationLink} entity")
public class DocumentIterationLinkDTO implements Serializable {

    @ApiModelProperty(value = "Workspace id")
    private String workspaceId;

    @ApiModelProperty(value = "Target document master id")
    private String documentMasterId;

    @ApiModelProperty(value = "Target document version")
    private String version;

    @ApiModelProperty(value = "Target document iteration")
    private int iteration;

    @ApiModelProperty(value = "Target document title")
    private String title;

    @ApiModelProperty(value = "Comment")
    private String commentLink;

    public DocumentIterationLinkDTO() {
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getDocumentMasterId() {
        return documentMasterId;
    }

    public void setDocumentMasterId(String documentMasterId) {
        this.documentMasterId = documentMasterId;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String getCommentLink() {
        return commentLink;
    }

    public void setCommentLink(String commentLink) {
        this.commentLink = commentLink;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
