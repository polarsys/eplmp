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
package org.polarsys.eplmp.core.product;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an association between two parts
 * inside a Product Breakdown Structure.
 *
 * @author Morgan Guimard
 * @version 2.5, 26/04/15
 * @see PartUsageLink
 * @see PartSubstituteLink
 * @since   V2.5
 */
public interface PartLink {
    int getId();
    Character getCode();
    String getFullId();
    double getAmount();
    String getUnit();
    String getComment();
    boolean isOptional();
    PartMaster getComponent();
    List<PartSubstituteLink> getSubstitutes();
    String getReferenceDescription();
    List<CADInstance> getCadInstances();
}
