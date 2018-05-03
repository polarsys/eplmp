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

import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.query.DocumentSearchQuery;
import org.polarsys.eplmp.core.query.PartSearchQuery;

import java.util.List;

public interface IIndexerManagerLocal {

    void createWorkspaceIndex(String workspaceId);

    void deleteWorkspaceIndex(String workspaceId) throws AccountNotFoundException;

    void indexDocumentIteration(DocumentIteration documentIteration);

    void indexDocumentIterations(List<DocumentIteration> documentIterations);

    void indexPartIteration(PartIteration partIteration);

    void indexPartIterations(List<PartIteration> partIterations);

    void removeDocumentIterationFromIndex(DocumentIteration documentIteration);

    void removePartIterationFromIndex(PartIteration partIteration);

    List<DocumentRevision> searchDocumentRevisions(DocumentSearchQuery documentSearchQuery, int from, int size) throws AccountNotFoundException, NotAllowedException;

    List<PartRevision> searchPartRevisions(PartSearchQuery partSearchQuery, int from, int size) throws AccountNotFoundException, NotAllowedException;

    void indexAllWorkspacesData() throws AccountNotFoundException;

    void indexWorkspaceData(String workspaceId) throws AccountNotFoundException;

    boolean ping();
}
