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

package org.polarsys.eplmp.core.meta;

import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A tag is just a label pinned on an entity.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="TAG")
@javax.persistence.IdClass(TagKey.class)
@javax.persistence.Entity
public class Tag implements Serializable {
    
    @Column(name = "WORKSPACE_ID", nullable = false, insertable = false, updatable = false)
    private String workspaceId="";

    @Column(length=100)
    @Id
    private String label="";

    @Id
    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    private Workspace workspace;

    public Tag() {
    }
    
    public Tag(Workspace pWorkspace, String pLabel) {
        setWorkspace(pWorkspace);
        label=pLabel;
    }
    
    public void setWorkspace(Workspace pWorkspace){
        workspace=pWorkspace;
        workspaceId=workspace.getId();
    }

    public String getLabel() {
        return label;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Workspace getWorkspace() {
        return workspace;
    }
   
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + label.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) pObj;
        
        return tag.workspaceId.equals(workspaceId)
                && tag.label.equals(label);
    }
    
    @Override
    public String toString() {
        return label;
    }
}
