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
import java.util.ArrayList;
import java.util.List;

/**
 * A class to store dry run result of a data import.
 *
 * A dry run informs of the actions that will be performed, parts to checkout,
 * parts to create when the import will be executed for real.
 *
 * Instances of this class are not persisted.
 *
 * @author Laurent Le Van
 *
 * @version 2.5, 29/06/2016
 * @since   V2.5
 */
public class ImportPreview implements Serializable {

    /**
     * Part revisions which will be checked out
     */
    private List<PartRevision> partRevsToCheckout = new ArrayList<>();

    /**
     * Part revisions which will be created
     */
    private List<PartMaster> partsToCreate = new ArrayList<>();


    public ImportPreview() {
    }

    public ImportPreview(List<PartRevision> partRevsToCheckout, List<PartMaster> partsToCreate) {
        this.partRevsToCheckout.addAll(partRevsToCheckout);
        this.partsToCreate.addAll(partsToCreate);
    }

    public List<PartRevision> getPartRevsToCheckout() {
        return partRevsToCheckout;
    }

    public void setPartRevsToCheckout(List<PartRevision> partRevsToCheckout) {
        this.partRevsToCheckout = partRevsToCheckout;
    }

    public List<PartMaster> getPartsToCreate() {
        return partsToCreate;
    }

    public void setPartsToCreate(List<PartMaster> partsToCreate) {
        this.partsToCreate = partsToCreate;
    }
}
