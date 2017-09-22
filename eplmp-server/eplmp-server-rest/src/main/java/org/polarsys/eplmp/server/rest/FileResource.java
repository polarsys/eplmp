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

import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.server.rest.file.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

@RequestScoped
@Api(value = "files", description = "Operations about files")
@Path("files")
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.GUEST_ROLE_ID})
@RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.GUEST_ROLE_ID})
public class FileResource {

    @Inject
    private DocumentBinaryResource documentBinaryResource;

    @Inject
    private PartBinaryResource partBinaryResource;

    @Inject
    private DocumentTemplateBinaryResource documentTemplateBinaryResource;

    @Inject
    private PartTemplateBinaryResource partTemplateBinaryResource;

    @Inject
    private ProductInstanceBinaryResource productInstanceBinaryResource;

    public FileResource() {
    }

    @ApiOperation(value = "documents")
    @Path("/{workspaceId}/documents/{documentId}/{version}")
    public DocumentBinaryResource documentFile() {
        return documentBinaryResource;
    }

    @ApiOperation(value = "parts")
    @Path("/{workspaceId}/parts/{partNumber}/{version}")
    public PartBinaryResource partFile() {
        return partBinaryResource;
    }

    @ApiOperation(value = "document-templates")
    @Path("/{workspaceId}/document-templates/{templateId}/")
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public DocumentTemplateBinaryResource documentTemplateFile() {
        return documentTemplateBinaryResource;
    }

    @ApiOperation(value = "part-templates")
    @Path("/{workspaceId}/part-templates/{templateId}/")
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public PartTemplateBinaryResource partTemplateFile() {
        return partTemplateBinaryResource;
    }

    @ApiOperation(value = "product-instances")
    @Path("/{workspaceId}/product-instances/{serialNumber}/{ciId}/")
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public ProductInstanceBinaryResource productInstanceFile() {
        return productInstanceBinaryResource;
    }

}
