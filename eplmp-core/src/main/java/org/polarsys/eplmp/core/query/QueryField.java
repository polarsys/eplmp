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

package org.polarsys.eplmp.core.query;

/**
 * Constants that correspond to searchable fields.
 *
 * @author Morgan Guimard
 */
public interface QueryField {

    String PART_MASTER_NUMBER = "pm.number";
    String PART_MASTER_NAME = "pm.name";
    String PART_MASTER_TYPE = "pm.type";
    String PART_MASTER_IS_STANDARD = "pm.standardPart";

    String PART_REVISION_PART_KEY = "pr.partKey";
    String PART_REVISION_VERSION = "pr.version";
    String PART_REVISION_MODIFICATION_DATE = "pr.modificationDate";
    String PART_REVISION_CHECKIN_DATE = "pr.checkInDate";
    String PART_REVISION_CHECKOUT_DATE = "pr.checkOutDate";
    String PART_REVISION_CREATION_DATE = "pr.creationDate";
    String PART_REVISION_LIFECYCLE_STATE = "pr.lifeCycleState";
    String PART_REVISION_STATUS = "pr.status";
    String PART_ITERATION_LINKED_DOCUMENTS = "pr.linkedDocuments";
    String PART_REVISION_ATTRIBUTES_PREFIX = "attr-";
    String PATH_DATA_ATTRIBUTES_PREFIX = "pd-attr-";

    String AUTHOR_LOGIN = "author.login";
    String AUTHOR_NAME = "author.name";

    String CTX_SERIAL_NUMBER = "ctx.serialNumber";
    String CTX_PRODUCT_ID = "ctx.productId";
    String CTX_DEPTH = "ctx.depth";
    String CTX_AMOUNT = "ctx.amount";
    String CTX_P2P_SOURCE = "ctx.p2p.source";
    String CTX_P2P_TARGET = "ctx.p2p.target";


}
