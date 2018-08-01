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

package org.polarsys.eplmp.server.rest.file;

import io.swagger.annotations.*;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.configuration.PathDataMaster;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceIterationKey;
import org.polarsys.eplmp.core.configuration.ProductInstanceMasterKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IProductInstanceManagerLocal;
import org.polarsys.eplmp.core.services.IPublicEntityManagerLocal;
import org.polarsys.eplmp.server.helpers.Streams;
import org.polarsys.eplmp.server.rest.exceptions.PreconditionFailedException;
import org.polarsys.eplmp.server.rest.exceptions.RequestedRangeNotSatisfiableException;
import org.polarsys.eplmp.server.rest.file.util.BinaryResourceDownloadMeta;
import org.polarsys.eplmp.server.rest.file.util.BinaryResourceDownloadResponseBuilder;
import org.polarsys.eplmp.server.rest.file.util.BinaryResourceUpload;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;

/**
 * @author Asmae CHADID on 30/03/15.
 **/

@RequestScoped
@Api(hidden = true, value = "productInstanceBinary", description = "Operations about product instances files",
        authorizations = {@Authorization(value = "authorization")})
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID})
@RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
public class ProductInstanceBinaryResource {

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    @Inject
    private IContextManagerLocal contextManager;

    @Inject
    private IProductInstanceManagerLocal productInstanceManagerLocal;

    @Inject
    private IPublicEntityManagerLocal publicEntityManager;

    @POST
    @ApiOperation(value = "Upload product instance files",
            response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "upload", paramType = "formData", dataType = "file", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Upload success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("iterations/{iteration}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Response uploadFilesToProductInstanceIteration(
            @Context HttpServletRequest request,
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Serial number") @PathParam("serialNumber") String serialNumber,
            @ApiParam(required = true, value = "Product instance iteration") @PathParam("iteration") int iteration)
            throws EntityNotFoundException, UserNotActiveException, NotAllowedException,
            AccessRightException, EntityAlreadyExistsException, CreationException, WorkspaceNotEnabledException {


        try {
            String fileName = null;
            ProductInstanceIterationKey iterationKey = new ProductInstanceIterationKey(serialNumber, workspaceId, configurationItemId, iteration);
            Collection<Part> formParts = request.getParts();

            for (Part formPart : formParts) {
                fileName = uploadAFile(workspaceId, formPart, iterationKey);
            }

            if (formParts.size() == 1) {
                return BinaryResourceUpload.tryToRespondCreated(request.getRequestURI() + URLEncoder.encode(fileName, "UTF-8"));
            }
            return Response.noContent().build();

        } catch (IOException | ServletException | StorageException e) {
            return BinaryResourceUpload.uploadError(e);
        }

    }


    @GET
    @ApiOperation(value = "Download product instance file",
            response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("iterations/{iteration}/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileFromProductInstance(
            @Context Request request,
            @ApiParam(required = false, value = "Range") @HeaderParam("Range") String range,
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Serial number") @PathParam("serialNumber") String serialNumber,
            @ApiParam(required = true, value = "Product instance iteration") @PathParam("iteration") int iteration,
            @ApiParam(required = true, value = "File name") @PathParam("fileName") final String fileName,
            @ApiParam(required = false, value = "Type") @QueryParam("type") String type,
            @ApiParam(required = false, value = "Output") @QueryParam("output") String output)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException,
            NotAllowedException, PreconditionFailedException,
            RequestedRangeNotSatisfiableException, WorkspaceNotEnabledException {


        String fullName = workspaceId + "/product-instances/" + serialNumber + "/iterations/" + iteration + "/" + fileName;
        BinaryResource binaryResource = getBinaryResource(fullName);
        BinaryResourceDownloadMeta binaryResourceDownloadMeta = new BinaryResourceDownloadMeta(binaryResource, output, type);

        // Check cache precondition
        Response.ResponseBuilder rb = request.evaluatePreconditions(binaryResourceDownloadMeta.getLastModified(), binaryResourceDownloadMeta.getETag());
        if (rb != null) {
            return rb.build();
        }
        InputStream binaryContentInputStream = null;

        ProductInstanceMasterKey productInstanceMasterKey = new ProductInstanceMasterKey(serialNumber, workspaceId, configurationItemId);

        boolean workingCopy = productInstanceManagerLocal.getProductInstanceIterations(productInstanceMasterKey).size() == iteration;

        boolean isToBeCached = !workingCopy;

        try {
            binaryContentInputStream = storageManager.getBinaryResourceInputStream(binaryResource);
            return BinaryResourceDownloadResponseBuilder.prepareResponse(binaryContentInputStream, binaryResourceDownloadMeta, range, isToBeCached);
        } catch (StorageException e) {
            Streams.close(binaryContentInputStream);
            return BinaryResourceDownloadResponseBuilder.downloadError(e, fullName);
        }

    }

    @POST
    @ApiOperation(value = "Upload path data iteration file",
            response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "upload", paramType = "formData", dataType = "file", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Upload success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("pathdata/{pathDataId}/iterations/{iteration}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Response uploadFilesToPathDataIteration(
            @Context HttpServletRequest request,
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Serial number") @PathParam("serialNumber") String serialNumber,
            @ApiParam(required = true, value = "Product instance iteration") @PathParam("iteration") int iteration,
            @ApiParam(required = true, value = "PathDataMaster Id") @PathParam("pathDataId") int pathDataId)
            throws EntityNotFoundException, UserNotActiveException, NotAllowedException, AccessRightException,
            EntityAlreadyExistsException, CreationException, WorkspaceNotEnabledException {

        try {
            String fileName = null;
            Collection<Part> formParts = request.getParts();

            for (Part formPart : formParts) {
                fileName = uploadAFileToPathDataIteration(workspaceId, formPart, configurationItemId, serialNumber, pathDataId, iteration);
            }

            if (formParts.size() == 1) {
                return BinaryResourceUpload.tryToRespondCreated(request.getRequestURI() + URLEncoder.encode(fileName, "UTF-8"));
            }
            return Response.noContent().build();

        } catch (IOException | ServletException | StorageException e) {
            return BinaryResourceUpload.uploadError(e);
        }

    }

    @GET
    @ApiOperation(value = "Download path data file",
            response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("pathdata/{pathDataId}/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileFromPathData(
            @Context Request request,
            @ApiParam(required = false, value = "Range") @HeaderParam("Range") String range,
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") String workspaceId,
            @ApiParam(required = true, value = "Serial number") @PathParam("serialNumber") String serialNumber,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Path data master id") @PathParam("pathDataId") final int pathDataId,
            @ApiParam(required = true, value = "File name") @PathParam("fileName") final String fileName,
            @ApiParam(required = false, value = "Type") @QueryParam("type") String type,
            @ApiParam(required = false, value = "Output") @QueryParam("output") String output)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException,
            NotAllowedException, PreconditionFailedException,
            RequestedRangeNotSatisfiableException, WorkspaceNotEnabledException {


        String fullName = workspaceId + "/product-instances/" + serialNumber + "/pathdata/" + pathDataId + "/" + fileName;
        BinaryResource binaryResource = getPathDataBinaryResource(fullName);
        BinaryResourceDownloadMeta binaryResourceDownloadMeta = new BinaryResourceDownloadMeta(binaryResource, output, type);

        // Check cache precondition
        Response.ResponseBuilder rb = request.evaluatePreconditions(binaryResourceDownloadMeta.getLastModified(), binaryResourceDownloadMeta.getETag());
        if (rb != null) {
            return rb.build();
        }

        InputStream binaryContentInputStream = null;

        // TODO : It seems this method is not used anywhere (to be confirmed). This variable is set only for refactoring consideration
        boolean isToBeCached = false;

        try {
            binaryContentInputStream = storageManager.getBinaryResourceInputStream(binaryResource);
            return BinaryResourceDownloadResponseBuilder.prepareResponse(binaryContentInputStream, binaryResourceDownloadMeta, range, isToBeCached);
        } catch (StorageException e) {
            Streams.close(binaryContentInputStream);
            return BinaryResourceDownloadResponseBuilder.downloadError(e, fullName);
        }
    }

    @GET
    @ApiOperation(value = "Download path data iteration file",
            response = File.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("pathdata/{pathDataId}/iterations/{iteration}/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileFromPathDataIteration(
            @Context Request request,
            @ApiParam(required = false, value = "Range") @HeaderParam("Range") String range,
            @ApiParam(required = true, value = "Workspace id") @PathParam("workspaceId") final String workspaceId,
            @ApiParam(required = true, value = "Serial number") @PathParam("serialNumber") final String serialNumber,
            @ApiParam(required = true, value = "Configuration item id") @PathParam("ciId") String configurationItemId,
            @ApiParam(required = true, value = "Path data master id") @PathParam("pathDataId") final int pathDataId,
            @ApiParam(required = true, value = "Path data iteration number") @PathParam("iteration") final int iteration,
            @ApiParam(required = true, value = "File name id") @PathParam("fileName") final String fileName,
            @ApiParam(required = false, value = "Type") @QueryParam("type") String type,
            @ApiParam(required = false, value = "Output") @QueryParam("output") String output)
            throws EntityNotFoundException, UserNotActiveException, AccessRightException, NotAllowedException,
            PreconditionFailedException, RequestedRangeNotSatisfiableException, WorkspaceNotEnabledException {


        String fullName = workspaceId + "/product-instances/" + serialNumber + "/pathdata/" + pathDataId + "/iterations/" + iteration + '/' + fileName;
        BinaryResource binaryResource = getPathDataBinaryResource(fullName);
        BinaryResourceDownloadMeta binaryResourceDownloadMeta = new BinaryResourceDownloadMeta(binaryResource, output, type);

        // Check cache precondition
        Response.ResponseBuilder rb = request.evaluatePreconditions(binaryResourceDownloadMeta.getLastModified(), binaryResourceDownloadMeta.getETag());
        if (rb != null) {
            return rb.build();
        }

        InputStream binaryContentInputStream = null;

        ProductInstanceIterationKey productInstanceIterationKey = new ProductInstanceIterationKey(serialNumber, workspaceId, configurationItemId, iteration);
        ProductInstanceIteration productInstanceIteration = productInstanceManagerLocal.getProductInstanceIteration(productInstanceIterationKey).getProductInstanceMaster().getLastIteration();
        List<PathDataMaster> pathDataMasterList = productInstanceIteration.getPathDataMasterList();

        PathDataMaster pathDataMaster = pathDataMasterList.stream()
                .filter(x -> pathDataId == x.getId())
                .findAny()
                .orElse(null);

        boolean workingCopy = false;
        if(pathDataMaster != null && pathDataMaster.getLastIteration() != null){
            workingCopy = pathDataMaster.getLastIteration().getIteration() == iteration;
        }

        boolean isToBeCached = !workingCopy;

        try {
            binaryContentInputStream = storageManager.getBinaryResourceInputStream(binaryResource);
            return BinaryResourceDownloadResponseBuilder.prepareResponse(binaryContentInputStream, binaryResourceDownloadMeta, range, isToBeCached);
        } catch (StorageException e) {
            Streams.close(binaryContentInputStream);
            return BinaryResourceDownloadResponseBuilder.downloadError(e, fullName);
        }
    }

    private BinaryResource getBinaryResource(String fullName)
            throws NotAllowedException, AccessRightException, UserNotActiveException, EntityNotFoundException, WorkspaceNotEnabledException {

        if (contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)) {
            return productInstanceManagerLocal.getBinaryResource(fullName);
        } else {
            return publicEntityManager.getBinaryResourceForProductInstance(fullName);
        }
    }

    private BinaryResource getPathDataBinaryResource(String fullName)
            throws NotAllowedException, AccessRightException, UserNotActiveException, EntityNotFoundException, WorkspaceNotEnabledException {
        if (contextManager.isCallerInRole(UserGroupMapping.REGULAR_USER_ROLE_ID)) {
            return productInstanceManagerLocal.getPathDataBinaryResource(fullName);
        } else {
            return publicEntityManager.getBinaryResourceForPathData(fullName);
        }
    }


    private String uploadAFile(String workspaceId, Part formPart, ProductInstanceIterationKey pdtIterationKey)
            throws EntityNotFoundException, EntityAlreadyExistsException, AccessRightException, NotAllowedException, CreationException, UserNotActiveException, StorageException, IOException, WorkspaceNotEnabledException {

        String fileName = Normalizer.normalize(formPart.getSubmittedFileName(), Normalizer.Form.NFC);
        // Init the binary resource with a null length
        BinaryResource binaryResource = productInstanceManagerLocal.saveFileInProductInstance(workspaceId, pdtIterationKey, fileName, 0);
        OutputStream outputStream = storageManager.getBinaryResourceOutputStream(binaryResource);
        long length = BinaryResourceUpload.uploadBinary(outputStream, formPart);
        productInstanceManagerLocal.saveFileInProductInstance(workspaceId, pdtIterationKey, fileName, (int) length);
        return fileName;
    }

    private String uploadAFileToPathData(String workspaceId, Part formPart, String configurationItemId, String serialNumber, int pathDataId, int iteration)
            throws EntityNotFoundException, EntityAlreadyExistsException, AccessRightException, NotAllowedException, CreationException, UserNotActiveException, StorageException, IOException, WorkspaceNotEnabledException {

        String fileName = Normalizer.normalize(formPart.getSubmittedFileName(), Normalizer.Form.NFC);
        // Init the binary resource with a null length
        BinaryResource binaryResource = productInstanceManagerLocal.saveFileInPathData(workspaceId, configurationItemId, serialNumber, pathDataId, iteration, fileName, 0);
        OutputStream outputStream = storageManager.getBinaryResourceOutputStream(binaryResource);
        long length = BinaryResourceUpload.uploadBinary(outputStream, formPart);
        productInstanceManagerLocal.saveFileInPathData(workspaceId, configurationItemId, serialNumber, pathDataId, iteration, fileName, (int) length);
        return fileName;
    }

    private String uploadAFileToPathDataIteration(String workspaceId, Part formPart, String configurationItemId, String serialNumber, int pathDataId, int iteration)
            throws EntityNotFoundException, EntityAlreadyExistsException, AccessRightException, NotAllowedException, CreationException, UserNotActiveException, StorageException, IOException, WorkspaceNotEnabledException {

        String fileName = Normalizer.normalize(formPart.getSubmittedFileName(), Normalizer.Form.NFC);
        // Init the binary resource with a null length
        BinaryResource binaryResource = productInstanceManagerLocal.saveFileInPathDataIteration(workspaceId, configurationItemId, serialNumber, pathDataId, iteration, fileName, 0);
        OutputStream outputStream = storageManager.getBinaryResourceOutputStream(binaryResource);
        long length = BinaryResourceUpload.uploadBinary(outputStream, formPart);
        productInstanceManagerLocal.saveFileInPathDataIteration(workspaceId, configurationItemId, serialNumber, pathDataId, iteration, fileName, (int) length);
        return fileName;
    }
}
