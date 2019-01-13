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

package org.polarsys.eplmp.core.configuration;

import java.io.Serializable;

/**
 * Identity class of {@link ProductInstanceIteration} objects.
 *
 * @author Florent Garin
 */
public class ProductInstanceIterationKey implements Serializable {

    private ProductInstanceMasterKey productInstanceMaster;
    private int iteration;

    public ProductInstanceIterationKey() {
    }

    public ProductInstanceIterationKey(String serialNumber, String pWorkspaceId, String pId, int pIteration) {
        this(new ProductInstanceMasterKey(serialNumber, pWorkspaceId, pId), pIteration);
    }

    public ProductInstanceIterationKey(ProductInstanceMasterKey pProductInstanceMaster, int pIteration) {
        productInstanceMaster = pProductInstanceMaster;
        iteration = pIteration;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public ProductInstanceMasterKey getProductInstanceMaster() {
        return productInstanceMaster;
    }

    public void setProductInstanceMaster(ProductInstanceMasterKey productInstanceMaster) {
        this.productInstanceMaster = productInstanceMaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductInstanceIterationKey that = (ProductInstanceIterationKey) o;

        return iteration == that.iteration && productInstanceMaster.equals(that.productInstanceMaster);

    }

    @Override
    public int hashCode() {
        int result = productInstanceMaster.hashCode();
        result = 31 * result + iteration;
        return result;
    }

    @Override
    public String toString() {
        return productInstanceMaster + "-" + iteration;
    }


}
