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

package org.polarsys.eplmp.core.common;


import javax.persistence.*;
import java.io.Serializable;

/**
 * The context in which documents, workflow models, parts, products, templates and all
 * the other objects reside.  
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since V1.0
 */
@Table(name="WORKSPACE")
@javax.persistence.Entity
@NamedQueries({
        @NamedQuery(name="Workspace.findWorkspacesWhereUserIsActive", query="SELECT w FROM Workspace w WHERE EXISTS (SELECT u.workspace FROM WorkspaceUserMembership u WHERE u.workspace = w AND u.member.login = :userLogin) OR EXISTS (SELECT g FROM WorkspaceUserGroupMembership g WHERE g.workspace = w AND EXISTS (SELECT gr FROM UserGroup gr, User us WHERE us.workspace = gr.workspace AND g.workspace = gr.workspace AND us.login = :userLogin AND us member of gr.users))"),
        @NamedQuery(name="Workspace.findAllWorkspaces", query="SELECT w FROM Workspace w")        
})
public class Workspace implements Serializable, Cloneable {

    @Column(length=100)
    @javax.persistence.Id
    private String id="";
    
    @javax.persistence.ManyToOne(optional=false, fetch=FetchType.EAGER)
    private Account admin;

    @Lob
    private String description;    
    
    private boolean folderLocked;

    private boolean enabled;

    public Workspace(String pId, Account pAdmin, String pDescription, boolean pFolderLocked) {
        id = pId;
        admin = pAdmin;
        description = pDescription;
        folderLocked=pFolderLocked;
    }
    public Workspace(String pId) {
        id = pId;
    }
    public Workspace() {
    }
    
    public Account getAdmin() {
        return admin;
    }
    public void setAdmin(Account pAdmin) {
        admin = pAdmin;
    }

    public String getId() {
        return id;
    }
    public void setId(String pId){
        id=pId;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public boolean isFolderLocked() {
        return folderLocked;
    }
    public void setFolderLocked(boolean folderLocked) {
        this.folderLocked = folderLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Workspace)){
            return false;
        }
        Workspace workspace = (Workspace) pObj;
        return workspace.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    

    @Override
    public Workspace clone() {
        try {
            return (Workspace) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
