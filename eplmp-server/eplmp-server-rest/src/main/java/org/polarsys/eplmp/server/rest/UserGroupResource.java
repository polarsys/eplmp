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

import io.swagger.annotations.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.UserGroupKey;
import org.polarsys.eplmp.core.exceptions.AccessRightException;
import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.exceptions.UserNotActiveException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotEnabledException;
import org.polarsys.eplmp.core.notification.TagUserGroupSubscription;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.INotificationManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.rest.dto.TagSubscriptionDTO;
import org.polarsys.eplmp.server.rest.dto.UserDTO;
import org.polarsys.eplmp.server.rest.dto.UserGroupDTO;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Api(hidden = true, value = "groups", description = "Operations about user groups",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class UserGroupResource {


    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private INotificationManagerLocal notificationManager;

    private Mapper mapper;

    private static final Logger LOGGER = Logger.getLogger(UserGroupResource.class.getName());

    public UserGroupResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }


    @GET
    @ApiOperation(value = "Get user groups in given workspace",
            response = UserGroupDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of UserGroupDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public UserGroupDTO[] getGroups(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws AccessRightException, EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        UserGroup[] userGroups = userManager.getUserGroups(workspaceId);
        UserGroupDTO[] userGroupDTOs = new UserGroupDTO[userGroups.length];
        for (int i = 0; i < userGroups.length; i++) {
            userGroupDTOs[i] = mapper.map(userGroups[i], UserGroupDTO.class);
        }
        return userGroupDTOs;
    }

    @GET
    @ApiOperation(value = "Get tag subscriptions of given user group",
            response = TagSubscriptionDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of TagSubscriptionDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{groupId}/tag-subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    public TagSubscriptionDTO[] getTagSubscriptionsForGroup(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Group id") @PathParam("groupId") String groupId)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {

        List<TagUserGroupSubscription> subs = notificationManager.getTagUserGroupSubscriptionsByGroup(workspaceId, groupId);

        TagSubscriptionDTO[] subDTOs = new TagSubscriptionDTO[subs.size()];
        for (int i = 0; i < subs.size(); i++) {
            subDTOs[i] = mapper.map(subs.get(i), TagSubscriptionDTO.class);
        }
        return subDTOs;
    }

    @GET
    @ApiOperation(value = "Get users of given user group",
            response = UserDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of UserDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{groupId}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO[] getUsersInGroup(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Group id") @PathParam("groupId") String groupId)
            throws AccessRightException, EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        UserGroup userGroup = userManager.getUserGroup(new UserGroupKey(workspaceId, groupId));
        Set<User> users = userGroup.getUsers();
        User[] usersArray = users.toArray(new User[users.size()]);
        UserDTO[] userDTOs = new UserDTO[users.size()];
        for (int i = 0; i < usersArray.length; i++) {
            userDTOs[i] = mapper.map(usersArray[i], UserDTO.class);
        }
        return userDTOs;
    }


    @PUT
    @ApiOperation(value = "Update or create tag subscription of given user group",
            response = TagSubscriptionDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful retrieval of TagSubscriptionDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{groupId}/tag-subscriptions/{tagName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserGroupSubscription(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Group id") @PathParam("groupId") String groupId,
            @ApiParam(required = true, value = "Tag name") @PathParam("tagName") String tagName,
            @ApiParam(required = true, value = "Tag subscription to update or create") TagSubscriptionDTO subDTO)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException, WorkspaceNotEnabledException {

        notificationManager.createOrUpdateTagUserGroupSubscription(workspaceId,
                groupId,
                tagName,
                subDTO.isOnIterationChange(),
                subDTO.isOnStateChange());
        subDTO.setTag(tagName);

        return Tools.prepareCreatedResponse(tagName, subDTO);
    }

    @DELETE
    @ApiOperation(value = "Delete tag subscription of given user group",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful deletion of TagSubscriptionDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{groupId}/tag-subscriptions/{tagName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserGroupSubscription(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Group id id") @PathParam("groupId") String groupId,
            @ApiParam(required = true, value = "Tag name") @PathParam("tagName") String tagName)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {

        notificationManager.removeTagUserGroupSubscription(workspaceId, groupId, tagName);
        return Response.noContent().build();
    }

}

