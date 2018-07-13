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
import java.util.Locale;

/**
 * This class represents a user in the context of a specific workspace.
 *
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since V1.0
 */
@Table(name="USERDATA")
@javax.persistence.IdClass(org.polarsys.eplmp.core.common.UserKey.class)
@javax.persistence.Entity
public class User implements Serializable, Cloneable {

    @Column(name = "WORKSPACE_ID", nullable = false, insertable = false, updatable = false)
    private String workspaceId = "";

    @Column(name = "LOGIN", nullable = false, insertable = false, updatable = false)
    private String login = "";

    @Id
    @JoinColumn(name = "LOGIN")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Account account;

    @Id
    @JoinColumn(name = "WORKSPACE_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;

    public User() {
    }

    public User(Workspace pWorkspace, Account pAccount) {
        setWorkspace(pWorkspace);
        setAccount(pAccount);
    }


    public String getLogin() {
        return login;
    }

    public String getName() {
        return account==null?null:account.getName();
    }

    public String getEmail() {
        return account==null?null:account.getEmail();
    }

    public String getLanguage() {
        return account==null?null:account.getLanguage();
    }

    public String getTimeZone() {
        return account==null?null:account.getTimeZone();
    }

    public boolean isAdministrator() {
        return login.equals(workspace.getAdmin().getLogin());
    }

    public UserKey getKey() {
        return new UserKey(workspaceId, login);
    }

    public void setAccount(Account pAccount) {
        account = pAccount;
        login = pAccount.getLogin();
    }

    public Account getAccount() {
        return account;
    }


    public void setWorkspace(Workspace pWorkspace) {
        workspace = pWorkspace;
        workspaceId = workspace.getId();
    }

    public Workspace getWorkspace() {
        return workspace;
    }


    public String getWorkspaceId() {
        return workspaceId;
    }

    public Locale getLocale() {
        return new Locale(this.getLanguage());
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof User)) {
            return false;
        }
        User user = (User) pObj;
        return user.login.equals(login) && user.workspaceId.equals(workspaceId);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + workspaceId.hashCode();
        hash = 31 * hash + login.hashCode();
        return hash;
    }

    @Override
    public User clone() {
        User clone = null;
        try {
            clone = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }
}
