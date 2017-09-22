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

package org.polarsys.eplmp.server.rest.util;

/**
 * This class holds the context for a document baseline export
 * See {link org.polarsys.eplmp.server.rest.writers.DocumentBaselineFileExportMessageBodyWriter} for response implementation
 *
 * @author Elisabel Généreux on 21/09/16.
 */
public class DocumentBaselineFileExport {

    /**
     * The workspace concerned
     */
    private String workspaceId;
    /**
     * The baseline identifier
     */
    private Integer baselineId;

    public DocumentBaselineFileExport() {
    }

    public DocumentBaselineFileExport(String workspaceId, Integer baselineId) {
        this.workspaceId = workspaceId;
        this.baselineId = baselineId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Integer getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(Integer baselineId) {
        this.baselineId = baselineId;
    }
}
