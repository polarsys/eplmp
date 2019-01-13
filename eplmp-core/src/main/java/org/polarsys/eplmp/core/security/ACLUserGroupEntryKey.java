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

import org.polarsys.eplmp.core.common.UserGroupKey;

import java.io.Serializable;

/**
 * Identity class of {@link ACLUserGroupEntry} objects.
 *
 * @author Florent Garin
 */
public class ACLUserGroupEntryKey implements Serializable {

    private UserGroupKey principal;
    private int acl;

    public ACLUserGroupEntryKey() {
    }

    public ACLUserGroupEntryKey(int acl, UserGroupKey principal) {
        this.acl = acl;
        this.principal = principal;
    }

    public int getAcl() {
        return acl;
    }

    public UserGroupKey getPrincipal() {
        return principal;
    }

    public void setAcl(int aclId) {
        this.acl = aclId;
    }

    public void setPrincipal(UserGroupKey principal) {
        this.principal = principal;
    }

    @Override
    public String toString() {
        return acl + "/" + principal;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof ACLUserGroupEntryKey)) {
            return false;
        }
        ACLUserGroupEntryKey key = (ACLUserGroupEntryKey) pObj;
        return key.acl==acl && key.principal.equals(principal);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + principal.hashCode();
        hash = 31 * hash + acl;
        return hash;
    }
}
