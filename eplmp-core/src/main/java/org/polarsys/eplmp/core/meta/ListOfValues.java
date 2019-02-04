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

package org.polarsys.eplmp.core.meta;

import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of values is basically a named collection of name-value pair.
 * The list is ordered and the value of the {@link NameValuePair} is optional.
 * Even if most of the time, we just want to define an enumeration type
 * and thus omit the value part of the pair, specifying a value could be
 * interesting for computation purpose.
 *
 * @author Florent Garin
 * @version 2.0, 27/02/15
 * @since V2.0
 */
@Table(name = "LOV")
@javax.persistence.IdClass(ListOfValuesKey.class)
@javax.persistence.Entity
public class ListOfValues implements Serializable {

    @Column(name = "WORKSPACE_ID", length = 100, nullable = false, insertable = false, updatable = false)
    @Id
    private String workspaceId = "";

    @Column(length = 100)
    @Id
    private String name = "";

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "NAMEVALUE_ORDER")
    @CollectionTable(
            name = "LOV_NAMEVALUE",
            joinColumns = {@JoinColumn(name = "LOV_NAME", referencedColumnName = "NAME"),
                    @JoinColumn(name = "LOV_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
            }
    )
    private List<NameValuePair> values=new ArrayList<>();

    public ListOfValues() {
    }

    public ListOfValues(Workspace pWorkspace, String pName) {
        setWorkspace(pWorkspace);
        name = pName;
    }

    public void setWorkspace(Workspace pWorkspace) {
        workspace = pWorkspace;
        workspaceId = workspace.getId();
    }

    public String getName() {
        return name;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public List<NameValuePair> getValues() {
        return values;
    }

    public void setValues(List<NameValuePair> values) {
        this.values = values;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + name.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof ListOfValues)) {
            return false;
        }
        ListOfValues lov = (ListOfValues) pObj;

        return lov.workspaceId.equals(workspaceId)
                && lov.name.equals(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
