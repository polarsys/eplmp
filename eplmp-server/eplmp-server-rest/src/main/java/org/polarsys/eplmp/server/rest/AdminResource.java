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
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.OAuthProvider;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.server.rest.dto.AccountDTO;
import org.polarsys.eplmp.server.rest.dto.OAuthProviderDTO;
import org.polarsys.eplmp.server.rest.dto.PlatformOptionsDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceDTO;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Morgan Guimard
 */
@RequestScoped
@Api(value = "admin", description = "Admin resources")
@Path("admin")
@DeclareRoles(UserGroupMapping.ADMIN_ROLE_ID)
@RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
public class AdminResource implements Serializable {

    @Inject
    private IWorkspaceManagerLocal workspaceService;

    @Inject
    private IDocumentManagerLocal documentService;

    @Inject
    private IProductManagerLocal productService;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IWorkspaceManagerLocal workspaceManager;

    @Inject
    private IAccountManagerLocal accountManager;

    @Inject
    private IPlatformOptionsManagerLocal platformOptionsManager;

    @Inject
    private IIndexerManagerLocal indexManager;

    @Inject
    private IOAuthManagerLocal oAuthManager;

    private Mapper mapper;

    public AdminResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @Path("disk-usage-stats")
    @ApiOperation(value = "Get disk usage stats",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of disk usage statistics"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDiskSpaceUsageStats()
            throws AccountNotFoundException {

        JsonObjectBuilder diskUsage = Json.createObjectBuilder();

        Workspace[] allWorkspaces = userManager.getAdministratedWorkspaces();

        for (Workspace workspace : allWorkspaces) {
            long workspaceDiskUsage = workspaceService.getDiskUsageInWorkspace(workspace.getId());
            diskUsage.add(workspace.getId(), workspaceDiskUsage);
        }

        return diskUsage.build();

    }

    @GET
    @Path("users-stats")
    @ApiOperation(value = "Get users stats",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of user statistics"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getUsersStats()
            throws AccountNotFoundException, WorkspaceNotFoundException, AccessRightException, UserNotFoundException,
            UserNotActiveException, WorkspaceNotEnabledException {

        JsonObjectBuilder userStats = Json.createObjectBuilder();

        Workspace[] allWorkspaces = userManager.getAdministratedWorkspaces();

        for (Workspace workspace : allWorkspaces) {
            int userCount = userManager.getUsers(workspace.getId()).length;
            userStats.add(workspace.getId(), userCount);
        }

        return userStats.build();

    }

    @GET
    @Path("documents-stats")
    @ApiOperation(value = "Get documents stats",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of documents statistics"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDocumentsStats()
            throws AccountNotFoundException, WorkspaceNotFoundException, AccessRightException, UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        JsonObjectBuilder docStats = Json.createObjectBuilder();

        Workspace[] allWorkspaces = userManager.getAdministratedWorkspaces();

        for (Workspace workspace : allWorkspaces) {
            int documentsCount = documentService.getDocumentsInWorkspaceCount(workspace.getId());
            docStats.add(workspace.getId(), documentsCount);
        }

        return docStats.build();

    }

    @GET
    @Path("products-stats")
    @ApiOperation(value = "Get products stats",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of products statistics"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProductsStats()
            throws AccountNotFoundException, UserNotFoundException, UserNotActiveException,
            WorkspaceNotFoundException, WorkspaceNotEnabledException {

        JsonObjectBuilder productsStats = Json.createObjectBuilder();

        Workspace[] allWorkspaces = userManager.getAdministratedWorkspaces();

        for (Workspace workspace : allWorkspaces) {
            int productsCount = productService.getConfigurationItems(workspace.getId()).size();
            productsStats.add(workspace.getId(), productsCount);
        }

        return productsStats.build();

    }

    @GET
    @Path("parts-stats")
    @ApiOperation(value = "Get parts stats",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of parts statistics"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getPartsStats()
            throws AccountNotFoundException, AccessRightException, WorkspaceNotFoundException, UserNotFoundException,
            UserNotActiveException, WorkspaceNotEnabledException {

        JsonObjectBuilder partsStats = Json.createObjectBuilder();

        Workspace[] allWorkspaces = userManager.getAdministratedWorkspaces();

        for (Workspace workspace : allWorkspaces) {
            int productsCount = productService.getPartsInWorkspaceCount(workspace.getId());
            partsStats.add(workspace.getId(), productsCount);
        }

        return partsStats.build();
    }


    @PUT
    @ApiOperation(value = "Synchronize index for workspace",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted delete operation (asynchronous method)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("index/{workspaceId}")
    public Response indexWorkspaceData(
            @ApiParam(value = "Workspace id", required = true) @PathParam("workspaceId") String workspaceId) throws AccountNotFoundException {
        indexManager.indexWorkspaceData(workspaceId);
        return Response.accepted().build();

    }

    @PUT
    @ApiOperation(value = "Synchronize index for all workspaces",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted delete operation (asynchronous method)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("index-all")
    public Response indexAllWorkspaces()
            throws AccountNotFoundException {
        indexManager.indexAllWorkspacesData();
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    @Path("platform-options")
    @ApiOperation(value = "Get platform options",
            response = PlatformOptionsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of PlatformOptions"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public PlatformOptionsDTO getPlatformOptions() {
        return mapper.map(platformOptionsManager.getPlatformOptions(), PlatformOptionsDTO.class);
    }

    @PUT
    @Path("platform-options")
    @ApiOperation(value = "Set platform options",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful update of PlatformOptions"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setPlatformOptions(
            @ApiParam("Options to set") PlatformOptionsDTO platformOptionsDTO) {
        platformOptionsManager.setRegistrationStrategy(platformOptionsDTO.getRegistrationStrategy());
        platformOptionsManager.setWorkspaceCreationStrategy(platformOptionsDTO.getWorkspaceCreationStrategy());
        return Response.noContent().build();
    }

    @PUT
    @ApiOperation(value = "Enable or disable workspace",
            response = WorkspaceDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of updated Workspace"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("workspace/{workspaceId}/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkspaceDTO enableWorkspace(
            @ApiParam(value = "Workspace id", required = true) @PathParam("workspaceId") String workspaceId,
            @ApiParam(value = "Enabled", required = true) @QueryParam("enabled") boolean enabled)
            throws WorkspaceNotFoundException {
        Workspace workspace = workspaceManager.enableWorkspace(workspaceId, enabled);
        return mapper.map(workspace, WorkspaceDTO.class);
    }

    @PUT
    @ApiOperation(value = "Enable or disable account",
            response = AccountDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of updated Account"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("accounts/{login}/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO enableAccount(
            @ApiParam(value = "Workspace id", required = true) @PathParam("login") String login,
            @ApiParam(value = "Enabled", required = true) @QueryParam("enabled") boolean enabled)
            throws WorkspaceNotFoundException, AccountNotFoundException, NotAllowedException {
        Account account = accountManager.enableAccount(login, enabled);
        return mapper.map(account, AccountDTO.class);
    }


    @GET
    @Path("accounts")
    @ApiOperation(value = "Get accounts ",
            response = AccountDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Accounts"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountManager.getAccounts();
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for (Account account : accounts) {
            accountsDTO.add(mapper.map(account, AccountDTO.class));
        }
        return accountsDTO;
    }

    @PUT
    @Path("accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of updated Account"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Update account",
            response = AccountDTO.class)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountDTO updateAccount(
            @ApiParam(required = true, value = "Updated account") AccountDTO accountDTO)
            throws AccountNotFoundException, NotAllowedException {

        Account account = accountManager.updateAccount(
                accountDTO.getLogin(),
                accountDTO.getName(),
                accountDTO.getEmail(),
                accountDTO.getLanguage(),
                accountDTO.getNewPassword(),
                accountDTO.getTimeZone());

        return mapper.map(account, AccountDTO.class);
    }

    @GET
    @Path("providers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of auth providers"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Get detailed providers",
            response = OAuthProviderDTO.class,
            responseContainer = "List")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailedProviders() {
        List<OAuthProvider> providers = oAuthManager.getProviders();
        List<OAuthProviderDTO> dtos = new ArrayList<>();

        for (OAuthProvider provider : providers) {
            dtos.add(mapper.map(provider, OAuthProviderDTO.class));
        }

        return Response.ok(new GenericEntity<List<OAuthProviderDTO>>(dtos) {
        }).build();
    }

    @GET
    @Path("providers/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of auth provider"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Get detailed provider",
            response = OAuthProviderDTO.class)
    @Produces(MediaType.APPLICATION_JSON)
    public OAuthProviderDTO getDetailedProvider(@ApiParam(value = "Provider id", required = true) @PathParam("id") int providerId) throws OAuthProviderNotFoundException {
        OAuthProvider provider = oAuthManager.getProvider(providerId);
        return mapper.map(provider, OAuthProviderDTO.class);
    }

    @POST
    @Path("providers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of auth provider"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Create provider",
            response = OAuthProviderDTO.class)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProvider(@ApiParam(required = true, value = "Updated account") OAuthProviderDTO providerDTO)
            throws AccountNotFoundException {

        OAuthProvider provider = oAuthManager.createProvider(providerDTO.getName(), providerDTO.isEnabled(), providerDTO.getAuthority(),
                providerDTO.getIssuer(), providerDTO.getClientID(), providerDTO.getJwsAlgorithm(),
                providerDTO.getJwkSetURL(), providerDTO.getRedirectUri(), providerDTO.getSecret(), providerDTO.getScope(), providerDTO.getResponseType(),
                providerDTO.getAuthorizationEndpoint());
        return Response.ok().entity(mapper.map(provider, OAuthProviderDTO.class)).build();
    }

    @PUT
    @Path("providers/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful update of auth provider"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Update provider",
            response = OAuthProviderDTO.class)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProvider(
            @ApiParam(value = "OAuthProvider id", required = true) @PathParam("id") int id,
            @ApiParam(required = true, value = "Updated provider") OAuthProviderDTO providerDTO)
            throws AccountNotFoundException, OAuthProviderNotFoundException {

        OAuthProvider provider = oAuthManager.updateProvider(id, providerDTO.getName(), providerDTO.isEnabled(), providerDTO.getAuthority(),
                providerDTO.getIssuer(), providerDTO.getClientID(), providerDTO.getJwsAlgorithm(),
                providerDTO.getJwkSetURL(), providerDTO.getRedirectUri(), providerDTO.getSecret(), providerDTO.getScope(), providerDTO.getResponseType(),
                providerDTO.getAuthorizationEndpoint());
        return Response.ok().entity(mapper.map(provider, OAuthProviderDTO.class)).build();
    }

    @DELETE
    @Path("providers/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful removal of auth provider"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation(value = "Remove provider",
            response = Response.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeProvider(
            @ApiParam(value = "OAuthProvider id", required = true) @PathParam("id") int id)
            throws AccountNotFoundException, OAuthProviderNotFoundException {
        oAuthManager.deleteProvider(id);
        return Response.noContent().build();
    }

}
