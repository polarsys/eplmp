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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IContextManagerLocal;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 * @author morgan on 07/09/15.
 */

@DeclareRoles({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IContextManagerLocal.class)
@Stateless(name = "ContextManagerBean")
public class ContextManagerBean implements IContextManagerLocal{

    @Resource
    private SessionContext ctx;

    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public boolean isCallerInRole(String role) {
        return ctx.isCallerInRole(role);
    }

    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public String getCallerPrincipalLogin() {
        return ctx.getCallerPrincipal().toString();
    }

    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public String getCallerPrincipalName() {
        return ctx.getCallerPrincipal().getName();
    }

}
