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
package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Taylor Labejof
 * @version 2.0, 26/09/14
 * @since   V2.0
 */
public interface IProductBaselineManagerLocal {

    ProductBaseline createBaseline(ConfigurationItemKey configurationItemKey, String name, ProductBaselineType type, String description, List<PartIterationKey> partIterationKeys, List<String> substituteLinks, List<String> optionalUsageLinks, Date effectiveDate, String effectiveSerialNumber, String effectiveLotId, boolean dryRun) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, PartRevisionNotReleasedException, PartIterationNotFoundException, UserNotActiveException, NotAllowedException, EntityConstraintException, PartMasterNotFoundException, CreationException, BaselineNotFoundException, PathToPathLinkAlreadyExistsException, WorkspaceNotEnabledException;
    List<ProductBaseline> getAllBaselines(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    List<ProductBaseline> getBaselines(ConfigurationItemKey configurationItemKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    void deleteBaseline(String pWorkspaceId, int baselineId) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, BaselineNotFoundException, UserNotActiveException, EntityConstraintException, WorkspaceNotEnabledException;
    ProductBaseline getBaseline(int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;
    ProductBaseline getBaselineById(int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    List<BaselinedPart> getBaselinedPartWithReference(int baselineId, String q, int maxResults) throws BaselineNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    List<PathChoice> getBaselineCreationPathChoices(ConfigurationItemKey ciKey, ProductBaselineType type) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, PartMasterNotFoundException, NotAllowedException, EntityConstraintException, WorkspaceNotEnabledException;
    List<PartIteration> getBaselineCreationVersionsChoices(ConfigurationItemKey ciKey) throws ConfigurationItemNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartMasterNotFoundException, NotAllowedException, EntityConstraintException, WorkspaceNotEnabledException;

    ProductConfiguration createProductConfiguration(ConfigurationItemKey ciKey, String name, String description, List<String> substituteLinks, List<String> optionalUsageLinks, Map<String,String> userEntries, Map<String,String> groupEntries) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, CreationException, AccessRightException, WorkspaceNotEnabledException;
    List<ProductConfiguration> getAllProductConfigurations(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;
    List<ProductConfiguration> getAllProductConfigurationsByConfigurationItemId(ConfigurationItemKey ciKey) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException;
    ProductConfiguration getProductConfiguration(ConfigurationItemKey ciKey, int productConfigurationId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductConfigurationNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    ProductConfiguration updateProductConfiguration(ConfigurationItemKey ciKey, int productConfigurationId, String name, String description,List<String> substituteLinks, List<String> optionalUsageLinks) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductConfigurationNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void deleteProductConfiguration(ConfigurationItemKey ciKey, int productConfigurationId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductConfigurationNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void updateACLForConfiguration(ConfigurationItemKey ciKey, int productConfigurationId, Map<String,String> userEntries, Map<String,String> groupEntries) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductConfigurationNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    void removeACLFromConfiguration(ConfigurationItemKey ciKey, int productConfigurationId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductConfigurationNotFoundException, AccessRightException, WorkspaceNotEnabledException;
    List<PartRevision> getObsoletePartRevisionsInBaseline(String workspaceId, int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;

    List<PathToPathLink> getPathToPathLinkFromSourceAndTarget(String workspaceId, String configurationItemId, int baselineId, String sourcePath, String targetPath) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;

    List<String> getPathToPathLinkTypes(String workspaceId, String configurationItemId, int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;

}
