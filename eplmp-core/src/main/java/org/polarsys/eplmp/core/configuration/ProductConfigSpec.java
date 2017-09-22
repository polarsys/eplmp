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


package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A ConfigSpec is used to select for each {@link PartMaster}s
 * the right {@link PartIteration} according to specific rules.
 *
 * It also selects from a complete {@link PartLink} path the one
 * which has to be considered (itself or a variant).
 *
 * ProductConfigSpec is a restrictive type of {@link ProductStructureFilter}.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since V1.1
 */
public abstract class ProductConfigSpec implements ProductStructureFilter, Serializable{

    public ProductConfigSpec() {
    }

    // Config specs are strict and returns a single value
    // Do not override them
    @Override
    public final List<PartLink> filter(List<PartLink> path) {
        PartLink partLink = filterPartLink(path);
        if(partLink != null){
            return Arrays.asList(partLink);
        }
        return new ArrayList<>();
    }

    @Override
    public final List<PartIteration> filter(PartMaster partMaster) {
        PartIteration partIteration = filterPartIteration(partMaster);
        if(partIteration != null){
            return Arrays.asList(partIteration);
        }
        return new ArrayList<>();
    }

    // All config specs must implement a strict filter
    public abstract PartIteration filterPartIteration(PartMaster partMaster);
    public abstract PartLink filterPartLink(List<PartLink> path);

}
