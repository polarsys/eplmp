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

import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.ListOfValues;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;
import org.polarsys.eplmp.core.meta.NameValuePair;

import java.util.List;

/**
 * @author Julien Lebeau
 */
public interface ILOVManagerLocal {

    List<ListOfValues> findLOVFromWorkspace(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    ListOfValues findLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException;

    void createLov(String workspaceId, String name, List<NameValuePair> nameValuePairList) throws ListOfValuesAlreadyExistsException, CreationException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, WorkspaceNotEnabledException;

    void deleteLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, EntityConstraintException, WorkspaceNotEnabledException;

    ListOfValues updateLov(ListOfValuesKey lovKey, String name, String workspaceId, List<NameValuePair> nameValuePairList) throws ListOfValuesAlreadyExistsException, CreationException, ListOfValuesNotFoundException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException;

    boolean isLOVDeletable(ListOfValuesKey lovKey);
}
