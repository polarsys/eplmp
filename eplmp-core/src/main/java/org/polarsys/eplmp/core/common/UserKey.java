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

import java.io.Serializable;

/**
 * Identity class of {@link User} objects.
 *
 * @author Florent Garin
 */
public class UserKey implements Serializable {
    
    private String workspace;
    private String account;
    
    public UserKey() {
    }
    
    public UserKey(String pWorkspaceId, String pLogin) {
        workspace =pWorkspaceId;
        account =pLogin;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspace.hashCode();
        hash = 31 * hash + account.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof UserKey)) {
            return false;
        }
        UserKey key = (UserKey) pObj;
        return key.account.equals(account) && key.workspace.equals(workspace);
    }
    
    @Override
    public String toString() {
        return workspace + "-" + account;
    }
    
    public String getWorkspace() {
        return workspace;
    }
    
    public void setWorkspace(String pWorkspaceId) {
        workspace = pWorkspaceId;
    }
    
    public String getAccount() {
        return account;
    }
    
    public void setAccount(String pLogin) {
        account = pLogin;
    }
    
}
