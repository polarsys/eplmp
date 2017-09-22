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

package org.polarsys.eplmp.core.admin;

import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that wraps setting options of a particular workspace.
 * These settings are related to front-end concerns.
 *
 * @author Morgan Guimard
 * @version 2.5, 04/09/17
 * @since V2.5
 */
@Table(name = "WORKSPACEFRONTOPTIONS")
@Entity
public class WorkspaceFrontOptions implements Serializable {

    @Id
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;

    @OrderColumn(name = "PARTCOLUMN_ORDER")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WORKSPACE_PARTTABLECOLUMN",
            joinColumns = {
                    @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
            }
    )
    @Column(name = "TABLECOLUMN")
    private List<String> partTableColumns;

    @OrderColumn(name = "DOCUMENTCOLUMN_ORDER")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WORKSPACE_DOCUMENTTABLECOLUMN",
            joinColumns = {
                    @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
            }
    )
    @Column(name = "TABLECOLUMN")
    private List<String> documentTableColumns;

    public WorkspaceFrontOptions() {
    }

    public WorkspaceFrontOptions(Workspace workspace, List<String> partTableColumns, List<String> documentTableColumns) {
        this.workspace = workspace;
        this.partTableColumns = partTableColumns;
        this.documentTableColumns = documentTableColumns;
    }

    public List<String> getDocumentTableColumns() {
        return documentTableColumns;
    }

    public void setDocumentTableColumns(List<String> documentTableColumns) {
        this.documentTableColumns = documentTableColumns;
    }

    public List<String> getPartTableColumns() {
        return partTableColumns;
    }

    public void setPartTableColumns(List<String> partTableColumns) {
        this.partTableColumns = partTableColumns;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
