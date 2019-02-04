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
package org.polarsys.eplmp.core.security;

import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class that holds information on how a specific user group belongs to a
 * workspace.
 * 
 * @author Florent Garin
 * @version 1.1, 08/07/09
 * @since   V1.1
 */
@Table(name="WORKSPACEUSERGROUPMEMBERSHIP")
@javax.persistence.IdClass(org.polarsys.eplmp.core.security.WorkspaceUserGroupMembershipKey.class)
@javax.persistence.Entity
public class WorkspaceUserGroupMembership implements Serializable {

    @javax.persistence.Column(name = "WORKSPACE_ID", length=100, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String workspaceId = "";
    @javax.persistence.ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;
    @javax.persistence.Column(name = "MEMBER_WORKSPACE_ID", length=100, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String memberWorkspaceId = "";
    @javax.persistence.Column(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String memberId = "";
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "MEMBER_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private UserGroup member;

    private boolean readOnly;

    public WorkspaceUserGroupMembership() {
    }

    public WorkspaceUserGroupMembership(Workspace pWorkspace, UserGroup pMember) {
        setWorkspace(pWorkspace);
        setMember(pMember);
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setWorkspace(Workspace pWorkspace) {
        workspace = pWorkspace;
        workspaceId = workspace.getId();
    }

    public void setMember(UserGroup pMember) {
        this.member = pMember;
        this.memberId=member.getId();
        this.memberWorkspaceId=member.getWorkspaceId();
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public UserGroup getMember() {
        return member;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberWorkspaceId() {
        return memberWorkspaceId;
    }
}
