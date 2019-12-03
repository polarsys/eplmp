/*******************************************************************************
 * Copyright (c) 2017-2019 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.converters;


import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.product.PartIterationKey;

import java.io.Serializable;

public class ConversionOrder implements Serializable {

    private PartIterationKey partIterationKey;

    private BinaryResource binaryResource;

    private String userToken;

    public ConversionOrder(PartIterationKey partIterationKey, BinaryResource binaryResource, String userToken) {
        this.partIterationKey = partIterationKey;
        this.binaryResource = binaryResource;
        this.userToken = userToken;
    }

    public ConversionOrder() {
    }

    public PartIterationKey getPartIterationKey() {

        return partIterationKey;
    }

    public void setPartIterationKey(PartIterationKey partIterationKey) {
        this.partIterationKey = partIterationKey;
    }

    public BinaryResource getBinaryResource() {
        return binaryResource;
    }

    public void setBinaryResource(BinaryResource binaryResource) {
        this.binaryResource = binaryResource;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
