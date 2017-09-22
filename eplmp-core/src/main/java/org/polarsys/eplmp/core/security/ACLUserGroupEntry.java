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

import org.polarsys.eplmp.core.common.UserGroup;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Class that belongs to the ACL class and makes the mapping between a group
 * and an associated permission.
 *
 * @author Florent Garin
 * @version 1.1, 17/07/09
 * @since   V1.1
 */
@Table(name="ACLUSERGROUPENTRY")
@Entity
@IdClass(org.polarsys.eplmp.core.security.ACLUserGroupEntryKey.class)
public class ACLUserGroupEntry implements Serializable, Cloneable {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="ACL_ID", referencedColumnName="ID")
    protected ACL acl;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "PRINCIPAL_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "PRINCIPAL_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private UserGroup principal;

    private ACLPermission permission;

    public ACLUserGroupEntry(){

    }

    public ACLUserGroupEntry(ACL acl, UserGroup principal, ACLPermission permission) {
        setACL(acl);
        setPrincipal(principal);
        setPermission(permission);
    }

    public void setACL(ACL pACL) {
        this.acl = pACL;
    }

    @XmlTransient
    public ACL getAcl() {
        return acl;
    }

    public void setPermission(ACLPermission permission) {
        this.permission = permission;
    }

    public void setPrincipal(UserGroup pPrincipal) {
        this.principal = pPrincipal;
    }

    public ACLPermission getPermission() {
        return permission;
    }

    public UserGroup getPrincipal() {
        return principal;
    }

    public String getPrincipalId() {
        return principal.getId();
    }


    @Override
    public ACLUserGroupEntry clone() {
        ACLUserGroupEntry clone = null;
        try {
            clone = (ACLUserGroupEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
    
}
