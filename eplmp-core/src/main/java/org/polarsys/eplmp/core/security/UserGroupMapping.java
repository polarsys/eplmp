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

import javax.persistence.Table;

/**
 * Useful class for managing security group as used by application servers.
 * This class has nothing to do with {@link UserGroup} and the context-aware security
 * model where a user may be granted full access on a given object and not be allowed
 * to see another one. This class just defines static groups which will lead to one of
 * the 3 global roles supported by the application: "users", "admin" and "guest".
 *
 * All regular users belong to, and only to, the "users" group.
 * "admin" is for the superuser account who can perform administration tasks.
 * "guest" role is for non logged users who access items that have been published.
 *
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="USERGROUPMAPPING")
@javax.persistence.Entity
public class UserGroupMapping implements java.io.Serializable {

    @javax.persistence.Id
    private String login="";
    private String groupName;
    
    public static final String REGULAR_USER_ROLE_ID="users";
    public static final String ADMIN_ROLE_ID ="admin";
    public static final String GUEST_ROLE_ID ="guest";

    public UserGroupMapping() {
    }
    
    public UserGroupMapping(String pLogin) {
        this(pLogin,REGULAR_USER_ROLE_ID);
    }
    
    public UserGroupMapping(String pLogin, String pRole) {
        login=pLogin;
        groupName=pRole;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
