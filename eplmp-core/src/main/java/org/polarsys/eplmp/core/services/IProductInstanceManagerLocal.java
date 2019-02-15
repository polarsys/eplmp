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
package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PathToPathLink;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Taylor Labejof
 * @version 2.0, 26/09/14
 * @since   V2.0
 */
public interface IProductInstanceManagerLocal {
    List<ProductInstanceMaster> getProductInstanceMasters(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    List<ProductInstanceMaster> getProductInstanceMasters(ConfigurationItemKey configurationItemKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    ProductInstanceMaster getProductInstanceMaster(ProductInstanceMasterKey productInstanceMasterKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;
    List<ProductInstanceIteration> getProductInstanceIterations(ProductInstanceMasterKey productInstanceMasterKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;
    ProductInstanceIteration getProductInstanceIteration(ProductInstanceIterationKey productInstanceIterationKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceIterationNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;
    ProductInstanceMaster createProductInstance(String workspaceId, ConfigurationItemKey configurationItemKey, String serialNumber, Integer baselineId, ProductBaselineType pType, Map<String, String> userEntries, Map<String, String> groupEntries, List<InstanceAttribute> attributes, DocumentRevisionKey[] links, String[] documentLinkComments, Date effectiveDate, String effectiveSerialNumber, String effectiveLotId) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, BaselineNotFoundException, CreationException, ProductInstanceAlreadyExistsException, NotAllowedException, EntityConstraintException, UserNotActiveException, PathToPathLinkAlreadyExistsException, PartMasterNotFoundException, ProductInstanceMasterNotFoundException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
    void deleteProductInstance(String workspaceId, String configurationItemId, String serialNumber) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, UserNotActiveException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;

    void updateACLForProductInstanceMaster(String workspaceId, String configurationItemId, String serialNumber, Map<String, String> userEntries, Map<String, String> groupEntries) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void removeACLFromProductInstanceMaster(String workspaceId, String configurationItemId, String serialNumber) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;

    BinaryResource saveFileInProductInstance(String workspaceId, ProductInstanceIterationKey pdtIterationKey, String fileName, int pSize) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, ProductInstanceMasterNotFoundException, AccessRightException, ProductInstanceIterationNotFoundException, FileAlreadyExistsException, CreationException, WorkspaceNotEnabledException;
    BinaryResource getBinaryResource(String fullName) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, FileNotFoundException, NotAllowedException, WorkspaceNotEnabledException;
    BinaryResource renameFileInProductInstance(String pFullName, String pNewName, String serialNumber,String cId,int iteration) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, FileNotFoundException, ProductInstanceMasterNotFoundException, NotAllowedException, AccessRightException, FileAlreadyExistsException, CreationException, StorageException, WorkspaceNotEnabledException;
    ProductInstanceMaster removeFileFromProductInstanceIteration(String workspaceId, int iteration, String fullName,ProductInstanceMasterKey productInstanceMasterKey) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, UserNotActiveException, FileNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;

    ProductInstanceMaster updateProductInstance(String workspaceId,int iteration,String iterationNote, ConfigurationItemKey configurationItemKey, String serialNumber, int baselineId, List<InstanceAttribute> attributes, DocumentRevisionKey[] links, String[] documentLinkComments) throws ProductInstanceMasterNotFoundException, UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ProductInstanceIterationNotFoundException, UserNotActiveException, BaselineNotFoundException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
    ProductInstanceMaster rebaseProductInstance(String workspaceId, String serialNumber, ConfigurationItemKey configurationItemKey, int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, BaselineNotFoundException, NotAllowedException, ConfigurationItemNotFoundException, PathToPathLinkAlreadyExistsException, PartMasterNotFoundException, CreationException, EntityConstraintException, WorkspaceNotEnabledException;

    PathDataMaster addNewPathDataIteration(String workspaceId, String configurationItemId, String serialNumber, int pathDataId, List<InstanceAttribute> attributes, String note, DocumentRevisionKey[] links, String[] documentLinkComments) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, UserNotActiveException, NotAllowedException, PathDataAlreadyExistsException, FileAlreadyExistsException, CreationException, PathDataMasterNotFoundException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
    PathDataMaster updatePathData(String workspaceId, String configurationItemId, String serialNumber, int pathDataMasterId, int iteration, List<InstanceAttribute> attributes, String description, DocumentRevisionKey[] pLinkKeys, String[] documentLinkComments) throws UserNotActiveException, WorkspaceNotFoundException, UserNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, NotAllowedException, DocumentRevisionNotFoundException, WorkspaceNotEnabledException;
    void deletePathData(String workspaceId, String configurationItemId, String serialNumber, int pathDataId) throws UserNotActiveException, WorkspaceNotFoundException, UserNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, NotAllowedException, WorkspaceNotEnabledException;

    PathDataMaster getPathDataByPath(String workspaceId, String configurationItemId, String serialNumber, String pathAsString) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException;
    boolean canWrite(String workspaceId, String configurationItemId, String serialNumber);

    BinaryResource saveFileInPathData(String workspaceId, String configurationItemId,  String serialNumber, int pathDataId,int iteration, String fileName, int pSize) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, AccessRightException, ProductInstanceMasterNotFoundException, FileAlreadyExistsException, CreationException, WorkspaceNotEnabledException;
    BinaryResource getPathDataBinaryResource(String fullName) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, FileNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    BinaryResource renameFileInPathData(String workspaceId, String configurationItemId, String serialNumber, int pathDataId,int iteration, String pFullName, String pNewName) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, FileNotFoundException, ProductInstanceMasterNotFoundException, NotAllowedException, AccessRightException, FileAlreadyExistsException, CreationException, WorkspaceNotEnabledException;
    ProductInstanceMaster removeFileFromPathData(String workspaceId, String configurationItemId,  String serialNumber, int pathDataId,int iteration, String fullName, ProductInstanceMaster productInstanceMaster) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, UserNotActiveException, NotAllowedException, FileNotFoundException, WorkspaceNotEnabledException;


    BinaryResource saveFileInPathDataIteration(String workspaceId, String configurationItemId, String serialNumber, int pathDataId, int iteration, String fileName, int i) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, AccessRightException, ProductInstanceMasterNotFoundException, FileAlreadyExistsException, CreationException, PathDataMasterNotFoundException, WorkspaceNotEnabledException;

    PathDataMaster createPathDataMaster(String workspaceId, String configurationItemId, String serialNumber, String path, List<InstanceAttribute> attributes, String iterationNote) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    PathToPathLink getPathToPathLink(String workspaceId, String configurationItemId, String serialNumber, int pathToPathLinkId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, PathToPathLinkNotFoundException, WorkspaceNotEnabledException;
    List<String> getPathToPathLinkTypes(String workspaceId, String configurationItemId, String serialNumber) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    List<PathToPathLink> getPathToPathLinks(String workspaceId, String configurationItemId, String serialNumber) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    List<PathToPathLink> getPathToPathLinkFromSourceAndTarget(String workspaceId, String configurationItemId, String serialNumber, String sourcePath, String targetPath) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    List<PathToPathLink> getRootPathToPathLinks(String workspaceId, String configurationItemId, String serialNumber, String type) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    List<ProductInstanceMaster> getProductInstanceMasters(PartRevision pPartRevision) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

}
