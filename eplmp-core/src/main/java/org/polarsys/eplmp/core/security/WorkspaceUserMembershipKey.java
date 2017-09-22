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
package org.polarsys.eplmp.core.security;

import java.io.Serializable;

/**
 * Identity class of {@link WorkspaceUserMembership} objects.
 *
 * @author Florent Garin
 */
public class WorkspaceUserMembershipKey implements Serializable {

    private String memberWorkspaceId;
    private String memberLogin;
    private String workspaceId;

    public WorkspaceUserMembershipKey() {
    }

    public WorkspaceUserMembershipKey(String pWorkspaceId, String pMemberWorkspaceId, String pMemberLogin) {
        workspaceId = pWorkspaceId;
        memberWorkspaceId = pMemberWorkspaceId;
        memberLogin = pMemberLogin;
    }

    public String getMemberLogin() {
        return memberLogin;
    }

    public String getMemberWorkspaceId() {
        return memberWorkspaceId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setMemberLogin(String memberLogin) {
        this.memberLogin = memberLogin;
    }

    public void setMemberWorkspaceId(String memberWorkspaceId) {
        this.memberWorkspaceId = memberWorkspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    

    @Override
    public String toString() {
        return workspaceId + "/" + memberWorkspaceId + "-" + memberLogin;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof WorkspaceUserMembershipKey)) {
            return false;
        }
        WorkspaceUserMembershipKey key = (WorkspaceUserMembershipKey) pObj;
        return key.workspaceId.equals(workspaceId) && key.memberWorkspaceId.equals(memberWorkspaceId) && key.memberLogin.equals(memberLogin);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + memberWorkspaceId.hashCode();
        hash = 31 * hash + memberLogin.hashCode();

        return hash;
    }
}
