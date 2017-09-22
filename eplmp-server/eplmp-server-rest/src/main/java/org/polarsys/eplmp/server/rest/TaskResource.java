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

import org.polarsys.eplmp.core.change.ModificationNotification;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.core.services.ITaskManagerLocal;
import org.polarsys.eplmp.core.workflow.ActivityKey;
import org.polarsys.eplmp.core.workflow.TaskKey;
import org.polarsys.eplmp.core.workflow.TaskWrapper;
import org.polarsys.eplmp.server.rest.dto.*;
import io.swagger.annotations.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan Guimard
 */

@RequestScoped
@Api(hidden = true, value = "tasks", description = "Operations about tasks")
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class TaskResource {

    @Inject
    private IDocumentManagerLocal documentService;
    @Inject
    private IProductManagerLocal productService;

    @Inject
    private ITaskManagerLocal taskManager;

    private Mapper mapper;

    public TaskResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }


    @GET
    @ApiOperation(value = "Get assigned tasks for given user",
            response = TaskDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of TaskDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{assignedUserLogin}/assigned")
    @Produces(MediaType.APPLICATION_JSON)
    public TaskDTO[] getAssignedTasksForGivenUser(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Assigned user login") @PathParam("assignedUserLogin") String assignedUserLogin)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException,
            WorkspaceNotEnabledException {

        TaskWrapper[] runningTasksForGivenUser = taskManager.getAssignedTasksForGivenUser(workspaceId, assignedUserLogin);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (TaskWrapper taskWrapper : runningTasksForGivenUser) {
            TaskDTO taskDTO = mapper.map(taskWrapper.getTask(), TaskDTO.class);
            taskDTO.setHolderType(taskWrapper.getHolderType());
            taskDTO.setWorkspaceId(workspaceId);
            taskDTO.setHolderReference(taskWrapper.getHolderReference());
            taskDTO.setHolderVersion(taskWrapper.getHolderVersion());
            taskDTOs.add(taskDTO);
        }
        return taskDTOs.toArray(new TaskDTO[taskDTOs.size()]);
    }

    @GET
    @ApiOperation(value = "Get task",
            response = TaskDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of TaskDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaskDTO getTask(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Task id") @PathParam("taskId") String taskId)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException,
            TaskNotFoundException, AccessRightException, WorkspaceNotEnabledException {

        String[] split = taskId.split("-");

        int workflowId = Integer.parseInt(split[0]);
        int step = Integer.parseInt(split[1]);
        int task = Integer.parseInt(split[2]);

        TaskKey taskKey = new TaskKey(new ActivityKey(workflowId, step), task);
        TaskWrapper taskWrapper = taskManager.getTask(workspaceId, taskKey);

        TaskDTO taskDTO = mapper.map(taskWrapper.getTask(), TaskDTO.class);
        taskDTO.setHolderType(taskWrapper.getHolderType());
        taskDTO.setWorkspaceId(workspaceId);
        taskDTO.setHolderReference(taskWrapper.getHolderReference());
        taskDTO.setHolderVersion(taskWrapper.getHolderVersion());

        return taskDTO;
    }

    @GET
    @ApiOperation(value = "Get documents where user has assigned tasks",
            response = DocumentRevisionDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of DocumentRevisionDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{assignedUserLogin}/documents")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentRevisionDTO[] getDocumentsWhereGivenUserHasAssignedTasks(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Assigned user login") @PathParam("assignedUserLogin") String assignedUserLogin,
            @ApiParam(required = false, value = "Status filter") @QueryParam("filter") String filter)
            throws EntityNotFoundException, UserNotActiveException {

        DocumentRevision[] docRs;

        if ("in_progress".equals(filter)) {
            docRs = documentService.getDocumentRevisionsWithOpenedTasksForGivenUser(workspaceId, assignedUserLogin);
        } else {
            docRs = documentService.getDocumentRevisionsWithAssignedTasksForGivenUser(workspaceId, assignedUserLogin);
        }

        List<DocumentRevisionDTO> docRsDTOs = new ArrayList<>();

        for (DocumentRevision docR : docRs) {

            DocumentRevisionDTO docDTO = mapper.map(docR, DocumentRevisionDTO.class);
            docDTO.setPath(docR.getLocation().getCompletePath());
            docDTO = Tools.createLightDocumentRevisionDTO(docDTO);
            docDTO.setIterationSubscription(documentService.isUserIterationChangeEventSubscribedForGivenDocument(workspaceId, docR));
            docDTO.setStateSubscription(documentService.isUserStateChangeEventSubscribedForGivenDocument(workspaceId, docR));
            docRsDTOs.add(docDTO);

        }

        return docRsDTOs.toArray(new DocumentRevisionDTO[docRsDTOs.size()]);
    }

    @GET
    @ApiOperation(value = "Get parts where user has assigned tasks",
            response = PartRevisionDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of PartRevisionDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{assignedUserLogin}/parts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartsWhereGivenUserHasAssignedTasks(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Assigned user login") @PathParam("assignedUserLogin") String assignedUserLogin,
            @ApiParam(required = false, value = "Task status filter") @QueryParam("filter") String filter)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException {

        PartRevision[] withTaskPartRevisions;

        if ("in_progress".equals(filter)) {
            withTaskPartRevisions = productService.getPartRevisionsWithOpenedTasksForGivenUser(workspaceId, assignedUserLogin);
        } else {
            withTaskPartRevisions = productService.getPartRevisionsWithAssignedTasksForGivenUser(workspaceId, assignedUserLogin);
        }

        List<PartRevisionDTO> partRevisionDTOs = new ArrayList<>();

        for (PartRevision partRevision : withTaskPartRevisions) {
            PartRevisionDTO partRevisionDTO = Tools.mapPartRevisionToPartDTO(partRevision);

            PartIterationKey iterationKey = new PartIterationKey(partRevision.getKey(), partRevision.getLastIterationNumber());
            List<ModificationNotification> notifications = productService.getModificationNotifications(iterationKey);
            List<ModificationNotificationDTO> notificationDTOs = Tools.mapModificationNotificationsToModificationNotificationDTO(notifications);
            partRevisionDTO.setNotifications(notificationDTOs);

            partRevisionDTOs.add(partRevisionDTO);
        }

        return Response.ok(new GenericEntity<List<PartRevisionDTO>>((List<PartRevisionDTO>) partRevisionDTOs) {
        }).build();
    }


    @PUT
    @ApiOperation(value = "Approve or reject task on document",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful task process"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{taskId}/process")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processTask(@ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
                                @ApiParam(required = true, value = "Task id") @PathParam("taskId") String taskId,
                                @ApiParam(required = true, value = "Task process data") TaskProcessDTO taskProcessDTO)
            throws EntityNotFoundException, NotAllowedException, UserNotActiveException, AccessRightException {

        String[] split = taskId.split("-");
        int workflowId = Integer.parseInt(split[0]);
        int step = Integer.parseInt(split[1]);
        int index = Integer.parseInt(split[2]);

        taskManager.processTask(workspaceId, new TaskKey(new ActivityKey(workflowId, step), index), taskProcessDTO.getAction().name(), taskProcessDTO.getComment(), taskProcessDTO.getSignature());
        return Response.noContent().build();
    }

}
