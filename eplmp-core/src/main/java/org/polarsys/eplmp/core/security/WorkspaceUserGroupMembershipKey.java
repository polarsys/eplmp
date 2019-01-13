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

import java.io.Serializable;

/**
 * Identity class of {@link WorkspaceUserGroupMembership} objects.
 *
 * @author Florent Garin
 */
public class WorkspaceUserGroupMembershipKey implements Serializable {

    private String memberWorkspaceId;
    private String memberId;
    private String workspaceId;

    public WorkspaceUserGroupMembershipKey() {
    }

    public WorkspaceUserGroupMembershipKey(String pWorkspaceId, String pMemberWorkspaceId, String pMemberId) {
        workspaceId = pWorkspaceId;
        memberWorkspaceId = pMemberWorkspaceId;
        memberId = pMemberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberWorkspaceId() {
        return memberWorkspaceId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setMemberWorkspaceId(String memberWorkspaceId) {
        this.memberWorkspaceId = memberWorkspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    

    @Override
    public String toString() {
        return workspaceId + "/" + memberWorkspaceId + "-" + memberId;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof WorkspaceUserGroupMembershipKey)) {
            return false;
        }
        WorkspaceUserGroupMembershipKey key = (WorkspaceUserGroupMembershipKey) pObj;
        return key.workspaceId.equals(workspaceId) && key.memberWorkspaceId.equals(memberWorkspaceId) && key.memberId.equals(memberId);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + memberWorkspaceId.hashCode();
        hash = 31 * hash + memberId.hashCode();

        return hash;
    }
}
