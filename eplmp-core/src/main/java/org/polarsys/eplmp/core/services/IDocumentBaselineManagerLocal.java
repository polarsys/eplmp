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

import org.polarsys.eplmp.core.configuration.BaselinedDocumentBinaryResourceCollection;
import org.polarsys.eplmp.core.configuration.DocumentBaseline;
import org.polarsys.eplmp.core.configuration.DocumentBaselineType;
import org.polarsys.eplmp.core.configuration.DocumentCollection;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.*;

import java.util.List;

/**
 *
 * @author Taylor Labejof
 * @version 2.0, 26/08/14
 * @since   V2.0
 */
public interface IDocumentBaselineManagerLocal {

    DocumentBaseline createBaseline(String workspaceId, String name, DocumentBaselineType type, String description, List<DocumentRevisionKey> documentRevisionKeys) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, FolderNotFoundException, UserNotActiveException, DocumentRevisionNotFoundException, NotAllowedException, WorkspaceNotEnabledException;

    List<DocumentBaseline> getBaselines(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    void deleteBaseline(String workspaceId, int baselineId) throws BaselineNotFoundException, UserNotFoundException, AccessRightException, WorkspaceNotFoundException, UserNotActiveException, WorkspaceNotEnabledException;

    DocumentBaseline getBaselineLight(String workspaceId, int baselineId) throws BaselineNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    DocumentCollection getACLFilteredDocumentCollection(String workspaceId, int baselineId) throws BaselineNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    List<BaselinedDocumentBinaryResourceCollection> getBinaryResourcesFromBaseline(String workspaceId, int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException;
}
