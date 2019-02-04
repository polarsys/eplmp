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
import org.polarsys.eplmp.core.document.DocumentIterationKey;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.DocumentRevisionNotFoundException;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.exceptions.PartRevisionNotFoundException;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PartRevisionKey;

/**
 *
 * @author Morgan Guimard
 */
public interface IPublicEntityManagerLocal {
    PartRevision getPublicPartRevision(PartRevisionKey partRevisionKey);
    DocumentRevision getPublicDocumentRevision(DocumentRevisionKey documentRevisionKey);
    BinaryResource getPublicBinaryResourceForDocument(String fullName) throws FileNotFoundException;
    BinaryResource getPublicBinaryResourceForPart(String fileName) throws FileNotFoundException;
    BinaryResource getBinaryResourceForSharedEntity(String fileName) throws FileNotFoundException;
    boolean canAccess(PartIterationKey partIKey) throws PartRevisionNotFoundException;
    boolean canAccess(DocumentIterationKey partIKey) throws DocumentRevisionNotFoundException;
    BinaryResource getBinaryResourceForProductInstance(String fullName) throws FileNotFoundException;
    BinaryResource getBinaryResourceForPathData(String fullName) throws FileNotFoundException;
}
