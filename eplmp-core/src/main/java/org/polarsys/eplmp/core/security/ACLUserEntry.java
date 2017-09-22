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

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Class that belongs to the ACL class and makes the mapping between a user
 * and an associated permission.
 *
 * @author Florent Garin
 * @version 1.1, 17/07/09
 * @since   V1.1
 */
@Table(name="ACLUSERENTRY")
@Entity
@IdClass(org.polarsys.eplmp.core.security.ACLUserEntryKey.class)
public class ACLUserEntry implements Serializable, Cloneable {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="ACL_ID", referencedColumnName="ID")
    protected ACL acl;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "PRINCIPAL_LOGIN", referencedColumnName = "LOGIN"),
        @JoinColumn(name = "PRINCIPAL_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User principal;

    private ACLPermission permission;

    public ACLUserEntry() {
    }

    public ACLUserEntry(ACL acl, User principal, ACLPermission permission) {
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

    public void setPrincipal(User pPrincipal) {
        this.principal = pPrincipal;
    }

    public ACLPermission getPermission() {
        return permission;
    }

    public User getPrincipal() {
        return principal;
    }

    public String getPrincipalLogin() {
        return principal.getLogin();
    }

    @Override
    public ACLUserEntry clone() {
        ACLUserEntry clone = null;
        try {
            clone = (ACLUserEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
    
}
