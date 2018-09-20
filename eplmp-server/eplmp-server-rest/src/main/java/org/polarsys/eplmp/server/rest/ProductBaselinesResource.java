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
import org.polarsys.eplmp.core.configuration.BaselinedPart;
import org.polarsys.eplmp.core.configuration.ProductBaseline;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PathToPathLink;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IProductBaselineManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.LightPartLinkDTO;
import org.polarsys.eplmp.server.rest.dto.LightPartLinkListDTO;
import org.polarsys.eplmp.server.rest.dto.LightPathToPathLinkDTO;
import org.polarsys.eplmp.server.rest.dto.PathToPathLinkDTO;
import org.polarsys.eplmp.server.rest.dto.baseline.BaselinedPartDTO;
import org.polarsys.eplmp.server.rest.dto.baseline.ProductBaselineCreationDTO;
import org.polarsys.eplmp.server.rest.dto.baseline.ProductBaselineDTO;

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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Taylor LABEJOF
 */
@RequestScoped
@Api(hidden = true, value = "productBaseline", description = "Operations about product-baseline",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles(UserGroupMapping.REGULAR_USER_ROLE_ID)
@RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
public class ProductBaselinesResource {

    @Inject
    private IProductBaselineManagerLocal productBaselineService;

    @Inject
    private IProductManagerLocal productService;

    private Mapper mapper;

    public ProductBaselinesResource() {
    }

    @PostConstruct
    public void init() {
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @GET
    @ApiOperation(value = "Get product-baselines in given workspace",
            response = ProductBaselineDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of ProductBaselineDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductBaselines(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId)
            throws UserNotActiveException, EntityNotFoundException, AccessRightException, WorkspaceNotEnabledException {

        List<ProductBaseline> productBaselines = productBaselineService.getAllBaselines(workspaceId);
        return makeList(productBaselines, workspaceId);
    }


    @GET
    @Path("{ciId}/baselines")
    @ApiOperation(value = "Get product-baseline with given configuration item",
            response = ProductBaselineDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of ProductBaselineDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductBaselinesForProduct(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String ciId)
            throws UserNotActiveException, EntityNotFoundException, AccessRightException, WorkspaceNotEnabledException {

        ConfigurationItemKey configurationItemKey = new ConfigurationItemKey(workspaceId, ciId);
        List<ProductBaseline> productBaselines = productBaselineService.getBaselines(configurationItemKey);
        return makeList(productBaselines,workspaceId);
    }

    @POST
    @ApiOperation(value = "Create a new product-baseline",
            response = ProductBaselineDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of ProductBaselineDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ProductBaselineDTO createProductBaseline(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Product baseline to create") ProductBaselineCreationDTO productBaselineDTO,
            @ApiParam(required = false, value = "Dry run flag") @QueryParam("dryRun") boolean dryRun)
            throws UserNotActiveException, EntityNotFoundException, NotAllowedException, AccessRightException,
            PartRevisionNotReleasedException, EntityConstraintException, CreationException,
            EntityAlreadyExistsException, WorkspaceNotEnabledException {

        String ciId = productBaselineDTO.getConfigurationItemId();
        ConfigurationItemKey ciKey = new ConfigurationItemKey(workspaceId, ciId);
        String description = productBaselineDTO.getDescription();
        String name = productBaselineDTO.getName();
        ProductBaselineType type = productBaselineDTO.getType();

        List<BaselinedPartDTO> baselinedPartsDTO = productBaselineDTO.getBaselinedParts();
        List<PartIterationKey> partIterationKeys = baselinedPartsDTO.stream()
                .map(part -> new PartIterationKey(workspaceId, part.getNumber(), part.getVersion(), part.getIteration())).collect(Collectors.toList());

        ProductBaseline baseline = productBaselineService.createBaseline(ciKey, name, type, description, partIterationKeys,
                productBaselineDTO.getSubstituteLinks(), productBaselineDTO.getOptionalUsageLinks(),
                productBaselineDTO.getEffectiveDate(), productBaselineDTO.getEffectiveSerialNumber(), productBaselineDTO.getEffectiveLotId(), dryRun);

        ProductBaselineDTO dto = mapper.map(baseline, ProductBaselineDTO.class);

        dto.setConfigurationItemLatestRevision(baseline.getConfigurationItem().getDesignItem().getLastRevision().getVersion());

        if(!dryRun) {
            dto.setHasObsoletePartRevisions(!productBaselineService.getObsoletePartRevisionsInBaseline(workspaceId, baseline.getId()).isEmpty());
        }

        return dto;
    }


    @DELETE
    @ApiOperation(value = "Delete product-baseline",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful deletion of ProductBaselineDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{ciId}/baselines/{baselineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteProductBaseline(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String ciId,
            @ApiParam(required = true, value = "Baseline id") @PathParam("baselineId") int baselineId)
            throws EntityNotFoundException, AccessRightException, UserNotActiveException,
            EntityConstraintException, WorkspaceNotEnabledException {

        productBaselineService.deleteBaseline(workspaceId, baselineId);
        return Response.noContent().build();
    }

    @GET
    @ApiOperation(value = "Get product-baseline by id",
            response = ProductBaselineDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of ProductBaselineDTO"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{ciId}/baselines/{baselineId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductBaselineDTO getProductBaseline(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String ciId,
            @ApiParam(required = true, value = "Baseline id") @PathParam("baselineId") int baselineId)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {

        ProductBaseline productBaseline = productBaselineService.getBaseline(baselineId);
        ProductBaselineDTO productBaselineDTO = mapper.map(productBaseline, ProductBaselineDTO.class);
        productBaselineDTO.setPathToPathLinks(getPathToPathLinksForGivenBaseline(productBaseline));
        productBaselineDTO.setConfigurationItemId(productBaseline.getConfigurationItem().getId());
        productBaselineDTO.setConfigurationItemLatestRevision(productBaseline.getConfigurationItem().getDesignItem().getLastRevision().getVersion());

        ConfigurationItemKey ciKey = productBaseline.getConfigurationItem().getKey();

        List<LightPartLinkListDTO> substitutesParts = new ArrayList<>();
        List<LightPartLinkListDTO> optionalParts = new ArrayList<>();

        for (String path : productBaseline.getSubstituteLinks()) {
            LightPartLinkListDTO lightPartLinkListDTO = new LightPartLinkListDTO();
            for (PartLink partLink : productService.decodePath(ciKey, path)) {
                lightPartLinkListDTO.getPartLinks().add(new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId()));
            }
            substitutesParts.add(lightPartLinkListDTO);
        }
        for (String path : productBaseline.getOptionalUsageLinks()) {
            LightPartLinkListDTO lightPartLinkListDTO = new LightPartLinkListDTO();
            for (PartLink partLink : productService.decodePath(ciKey, path)) {
                lightPartLinkListDTO.getPartLinks().add(new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId()));
            }
            optionalParts.add(lightPartLinkListDTO);
        }

        productBaselineDTO.setSubstitutesParts(substitutesParts);
        productBaselineDTO.setOptionalsParts(optionalParts);

        return productBaselineDTO;
    }

    @GET
    @ApiOperation(value = "Get product-baseline's baselined parts",
            response = BaselinedPartDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of BaselinedPartDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{ciId}/baselines/{baselineId}/parts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductBaselineParts(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String ciId,
            @ApiParam(required = true, value = "Baseline id") @PathParam("baselineId") int baselineId,
            @ApiParam(required = true, value = "Query") @QueryParam("q") String q)
            throws EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        int maxResults = 8;
        List<BaselinedPart> baselinedPartList = productBaselineService.getBaselinedPartWithReference(baselineId, q, maxResults);

        List<BaselinedPartDTO> baselinedPartDTOList = new ArrayList<>();
        for (BaselinedPart baselinedPart : baselinedPartList) {
            baselinedPartDTOList.add(Tools.mapBaselinedPartToBaselinedPartDTO(baselinedPart));
        }
        return Response.ok(new GenericEntity<List<BaselinedPartDTO>>((List<BaselinedPartDTO>) baselinedPartDTOList) {
        }).build();
    }

    @GET
    @ApiOperation(value = "Get product-baseline's path-to-path links",
            response = LightPathToPathLinkDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of LightPathToPathLinkDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{ciId}/baselines/{baselineId}/path-to-path-links-types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPathToPathLinkTypesInBaseline(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String ciId,
            @ApiParam(required = true, value = "Baseline id") @PathParam("baselineId") int baselineId)
            throws EntityNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {

        List<String> pathToPathLinkTypes = productBaselineService.getPathToPathLinkTypes(workspaceId, ciId, baselineId);
        List<LightPathToPathLinkDTO> pathToPathLinkDTOs = new ArrayList<>();
        for (String type : pathToPathLinkTypes) {
            LightPathToPathLinkDTO pathToPathLinkDTO = new LightPathToPathLinkDTO();
            pathToPathLinkDTO.setType(type);
            pathToPathLinkDTOs.add(pathToPathLinkDTO);
        }
        return Response.ok(new GenericEntity<List<LightPathToPathLinkDTO>>((List<LightPathToPathLinkDTO>) pathToPathLinkDTOs) {
        }).build();
    }

    @GET
    @ApiOperation(value = "Get product-baseline's path-to-path links for given source and target",
            response = LightPathToPathLinkDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of LightPathToPathLinkDTOs. It can be an empty list."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("{ciId}/baselines/{baselineId}/path-to-path-links/source/{sourcePath}/target/{targetPath}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPathToPathLinkInProductBaseline(
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Baseline id") @PathParam("baselineId") int baselineId,
            @ApiParam(required = true, value = "Source path") @PathParam("sourcePath") String sourcePathAsString,
            @ApiParam(required = true, value = "Target path") @PathParam("targetPath") String targetPathAsString)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {

        List<PathToPathLink> pathToPathLinks = productBaselineService.getPathToPathLinkFromSourceAndTarget(workspaceId, configurationItemId, baselineId, sourcePathAsString, targetPathAsString);
        List<PathToPathLinkDTO> pathToPathLinkDTOs = new ArrayList<>();
        ConfigurationItemKey ciKey = new ConfigurationItemKey(workspaceId, configurationItemId);

        for (PathToPathLink pathToPathLink : pathToPathLinks) {
            PathToPathLinkDTO pathToPathLinkDTO = mapper.map(pathToPathLink, PathToPathLinkDTO.class);
            List<LightPartLinkDTO> sourceLightPartLinkDTOs = new ArrayList<>();

            List<PartLink> sourcePath = productService.decodePath(ciKey, pathToPathLink.getSourcePath());
            List<PartLink> targetPath = productService.decodePath(ciKey, pathToPathLink.getTargetPath());

            for (PartLink partLink : sourcePath) {
                LightPartLinkDTO lightPartLinkDTO = new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId());
                sourceLightPartLinkDTOs.add(lightPartLinkDTO);
            }

            List<LightPartLinkDTO> targetLightPartLinkDTOs = new ArrayList<>();
            for (PartLink partLink : targetPath) {
                LightPartLinkDTO lightPartLinkDTO = new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId());
                targetLightPartLinkDTOs.add(lightPartLinkDTO);
            }

            pathToPathLinkDTO.setSourceComponents(sourceLightPartLinkDTOs);
            pathToPathLinkDTO.setTargetComponents(targetLightPartLinkDTOs);
            pathToPathLinkDTOs.add(pathToPathLinkDTO);
        }

        return Response.ok(new GenericEntity<List<PathToPathLinkDTO>>((List<PathToPathLinkDTO>) pathToPathLinkDTOs) {
        }).build();

    }

    private List<PathToPathLinkDTO> getPathToPathLinksForGivenBaseline(ProductBaseline productBaseline) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, ConfigurationItemNotFoundException, PartUsageLinkNotFoundException, ProductInstanceMasterNotFoundException, BaselineNotFoundException, AccessRightException, WorkspaceNotEnabledException {

        List<PathToPathLink> pathToPathLinkTypes = productBaseline.getPathToPathLinks();
        List<PathToPathLinkDTO> pathToPathLinkDTOs = new ArrayList<>();

        for (PathToPathLink pathToPathLink : pathToPathLinkTypes) {
            PathToPathLinkDTO pathToPathLinkDTO = mapper.map(pathToPathLink, PathToPathLinkDTO.class);
            List<PartLink> sourcePath = productService.decodePath(productBaseline.getConfigurationItem().getKey(), pathToPathLink.getSourcePath());
            List<PartLink> targetPath = productService.decodePath(productBaseline.getConfigurationItem().getKey(), pathToPathLink.getTargetPath());

            List<LightPartLinkDTO> sourceLightPartLinkDTOs = new ArrayList<>();
            for (PartLink partLink : sourcePath) {
                LightPartLinkDTO lightPartLinkDTO = new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId());
                sourceLightPartLinkDTOs.add(lightPartLinkDTO);
            }

            List<LightPartLinkDTO> targetLightPartLinkDTOs = new ArrayList<>();
            for (PartLink partLink : targetPath) {
                LightPartLinkDTO lightPartLinkDTO = new LightPartLinkDTO(partLink.getComponent().getNumber(), partLink.getComponent().getName(),partLink.getReferenceDescription(),partLink.getFullId());
                targetLightPartLinkDTOs.add(lightPartLinkDTO);
            }

            pathToPathLinkDTO.setSourceComponents(sourceLightPartLinkDTOs);
            pathToPathLinkDTO.setTargetComponents(targetLightPartLinkDTOs);
            pathToPathLinkDTOs.add(pathToPathLinkDTO);

        }
        return pathToPathLinkDTOs;
    }

    private Response makeList(List<ProductBaseline> productBaselines, String workspaceId) throws UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, BaselineNotFoundException, WorkspaceNotEnabledException {
        List<ProductBaselineDTO> baselinesDTO = new ArrayList<>();
        for (ProductBaseline productBaseline : productBaselines) {
            ProductBaselineDTO productBaselineDTO = mapper.map(productBaseline, ProductBaselineDTO.class);
            productBaselineDTO.setConfigurationItemId(productBaseline.getConfigurationItem().getId());
            productBaselineDTO.setConfigurationItemLatestRevision(productBaseline.getConfigurationItem().getDesignItem().getLastRevision().getVersion());
            productBaselineDTO.setHasObsoletePartRevisions(!productBaselineService.getObsoletePartRevisionsInBaseline(workspaceId, productBaseline.getId()).isEmpty());
            baselinesDTO.add(productBaselineDTO);
        }
        return Response.ok(new GenericEntity<List<ProductBaselineDTO>>((List<ProductBaselineDTO>) baselinesDTO) {
        }).build();
    }

}
