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
package org.polarsys.eplmp.server.rest;

import io.swagger.annotations.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.document.DocumentMasterTemplate;
import org.polarsys.eplmp.core.document.DocumentMasterTemplateKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.meta.InstanceAttributeTemplate;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;
import org.polarsys.eplmp.server.rest.dto.*;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yassine Belouad
 */

@RequestScoped
@Api(hidden = true, value = "documentTemplates", description = "Operations about document templates",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class DocumentTemplateResource {

    @Inject
    private IDocumentManagerLocal documentService;

    private Mapper mapper;

    public DocumentTemplateResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @ApiOperation(value = "Get document templates",
            response = DocumentMasterTemplateDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of DocumentMasterTemplateDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentMasterTemplateDTO[] getDocumentMasterTemplates(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        DocumentMasterTemplate[] documentMasterTemplates = documentService.getDocumentMasterTemplates(workspaceId);
        DocumentMasterTemplateDTO[] documentMasterTemplateDTOs = new DocumentMasterTemplateDTO[documentMasterTemplates.length];

        for (int i = 0; i < documentMasterTemplates.length; i++) {
            documentMasterTemplateDTOs[i] = mapper.map(documentMasterTemplates[i], DocumentMasterTemplateDTO.class);
        }

        return documentMasterTemplateDTOs;
    }

    @GET
    @ApiOperation(value = "Get document template by id",
            response = DocumentMasterTemplateDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of DocumentMasterTemplateDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentMasterTemplateDTO getDocumentMasterTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId)
            throws EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        DocumentMasterTemplate documentMasterTemplate = documentService.getDocumentMasterTemplate(new DocumentMasterTemplateKey(workspaceId, templateId));
        return mapper.map(documentMasterTemplate, DocumentMasterTemplateDTO.class);
    }

    @GET
    @ApiOperation(value = "Generate document template id",
            response = TemplateGeneratedIdDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of TemplateGeneratedIdDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}/generate_id")
    @Produces(MediaType.APPLICATION_JSON)
    public TemplateGeneratedIdDTO generateDocumentMasterId(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId)
            throws EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        String generateId = documentService.generateId(workspaceId, templateId);
        return new TemplateGeneratedIdDTO(generateId);
    }

    @POST
    @ApiOperation(value = "Create a new document template",
            response = DocumentMasterTemplateDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of created DocumentMasterTemplateDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentMasterTemplateDTO createDocumentMasterTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Document master template to create") DocumentTemplateCreationDTO templateCreationDTO)
            throws EntityNotFoundException, EntityAlreadyExistsException, AccessRightException, NotAllowedException, CreationException, WorkspaceNotEnabledException {

        String id = templateCreationDTO.getReference();
        String documentType = templateCreationDTO.getDocumentType();
        String workflowModelId = templateCreationDTO.getWorkflowModelId();
        String mask = templateCreationDTO.getMask();
        boolean idGenerated = templateCreationDTO.isIdGenerated();
        boolean attributesLocked = templateCreationDTO.isAttributesLocked();

        List<InstanceAttributeTemplateDTO> attrTemplateDTOs = templateCreationDTO.getAttributeTemplates();
        String[] lovNames = new String[attrTemplateDTOs.size()];
        for (int i = 0; i < attrTemplateDTOs.size(); i++)
            lovNames[i] = attrTemplateDTOs.get(i).getLovName();


        List<InstanceAttributeTemplate> attrTemplates = new ArrayList<>();
        for (InstanceAttributeTemplateDTO dto : attrTemplateDTOs) {
            attrTemplates.add(mapper.map(dto, InstanceAttributeTemplate.class));
        }

        DocumentMasterTemplate template = documentService.createDocumentMasterTemplate(workspaceId, id, documentType, workflowModelId, mask, attrTemplates, lovNames, idGenerated, attributesLocked);
        return mapper.map(template, DocumentMasterTemplateDTO.class);
    }

    @PUT
    @ApiOperation(value = "Update document template",
            response = DocumentMasterTemplateDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of updated DocumentMasterTemplateDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentMasterTemplateDTO updateDocumentMasterTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId,
            @ApiParam(required = true, value = "Document master template to update") DocumentMasterTemplateDTO documentMasterTemplateDTO)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException, NotAllowedException, WorkspaceNotEnabledException {

        String documentType = documentMasterTemplateDTO.getDocumentType();
        String workflowModelId = documentMasterTemplateDTO.getWorkflowModelId();
        String mask = documentMasterTemplateDTO.getMask();
        boolean idGenerated = documentMasterTemplateDTO.isIdGenerated();
        boolean attributesLocked = documentMasterTemplateDTO.isAttributesLocked();

        List<InstanceAttributeTemplateDTO> attrTemplateDTOs = documentMasterTemplateDTO.getAttributeTemplates();
        String[] lovNames = new String[attrTemplateDTOs.size()];
        for (int i = 0; i < attrTemplateDTOs.size(); i++)
            lovNames[i] = attrTemplateDTOs.get(i).getLovName();

        List<InstanceAttributeTemplate> attrTemplates = new ArrayList<>();
        for (InstanceAttributeTemplateDTO dto : attrTemplateDTOs) {
            attrTemplates.add(mapper.map(dto, InstanceAttributeTemplate.class));
        }

        DocumentMasterTemplate template = documentService.updateDocumentMasterTemplate(new DocumentMasterTemplateKey(workspaceId, templateId), documentType, workflowModelId, mask, attrTemplates, lovNames, idGenerated, attributesLocked);
        return mapper.map(template, DocumentMasterTemplateDTO.class);
    }

    @PUT
    @ApiOperation(value = "Update document template ACL",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful ACL update"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}/acl")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDocumentMasterTemplateACL(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId,
            @ApiParam(required = true, value = "ACL rules to set") ACLDTO acl)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException, NotAllowedException, WorkspaceNotEnabledException {

        if (acl.hasEntries()) {
            documentService.updateACLForDocumentMasterTemplate(workspaceId, templateId, acl.getUserEntriesMap(), acl.getUserGroupEntriesMap());
        } else {
            documentService.removeACLFromDocumentMasterTemplate(workspaceId, templateId);
        }

        return Response.noContent().build();
    }

    @DELETE
    @ApiOperation(value = "Delete document template",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful deletion of DocumentMasterTemplateDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}")
    public Response deleteDocumentMasterTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException, WorkspaceNotEnabledException {

        documentService.deleteDocumentMasterTemplate(new DocumentMasterTemplateKey(workspaceId, templateId));
        return Response.noContent().build();
    }

    @DELETE
    @ApiOperation(value = "Remove attached file from document template",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful deletion of file in DocumentMasterTemplateDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}/files/{fileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeAttachedFileFromDocumentTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId,
            @ApiParam(required = true, value = "File name") @PathParam("fileName") String fileName)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException, StorageException, WorkspaceNotEnabledException {

        String fileFullName = workspaceId + "/document-templates/" + templateId + "/" + fileName;

        documentService.removeFileFromTemplate(fileFullName);
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Rename attached file in document template",
            response = FileDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful rename file operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{templateId}/files/{fileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileDTO renameAttachedFileInDocumentTemplate(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Template id") @PathParam("templateId") String templateId,
            @ApiParam(required = true, value = "File name") @PathParam("fileName") String fileName,
            @ApiParam(required = true, value = "File to rename") FileDTO fileDTO)
            throws UserNotActiveException, EntityNotFoundException, CreationException, NotAllowedException,
            AccessRightException, EntityAlreadyExistsException, StorageException, WorkspaceNotEnabledException {
        String fileFullName = workspaceId + "/document-templates/" + templateId + "/" + fileName;
        BinaryResource binaryResource = documentService.renameFileInTemplate(fileFullName, fileDTO.getShortName());
        return new FileDTO(true, binaryResource.getFullName(), binaryResource.getName());
    }
}
