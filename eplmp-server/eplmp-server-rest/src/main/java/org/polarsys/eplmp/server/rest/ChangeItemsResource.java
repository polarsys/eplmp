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
package org.polarsys.eplmp.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.polarsys.eplmp.core.security.UserGroupMapping;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

@RequestScoped
@Api(hidden = true, value = "changeItems", description = "Operations about change items",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class ChangeItemsResource {

    @Inject
    private ChangeIssuesResource issues;
    @Inject
    private ChangeRequestsResource requests;
    @Inject
    private ChangeOrdersResource orders;
    @Inject
    private MilestonesResource milestones;

    public ChangeItemsResource() {
    }

    @Path("/issues")
    @ApiOperation(value = "SubResource : ChangeIssuesResource")
    public ChangeIssuesResource issues() {
        return issues;
    }

    @Path("/requests")
    @ApiOperation(value = "SubResource : ChangeRequestsResource")
    public ChangeRequestsResource requests() {
        return requests;
    }

    @Path("/orders")
    @ApiOperation(value = "SubResource : ChangeOrdersResource")
    public ChangeOrdersResource orders() {
        return orders;
    }

    @Path("/milestones")
    @ApiOperation(value = "SubResource : MilestonesResource")
    public MilestonesResource milestones() {
        return milestones;
    }

}
