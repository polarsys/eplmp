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
package org.polarsys.eplmp.server.configuration.spec;

import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartSubstituteLink;
import org.polarsys.eplmp.core.util.Tools;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductConfigSpec} which returns the {@link PartIteration}
 * which belongs to the given baseline.
 *
 * As a baseline should have no ambiguity, if a filter returns null the spec is considered as invalid.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class ProductBaselineConfigSpec extends ProductConfigSpec {

    private ProductBaseline productBaseline;

    public ProductBaselineConfigSpec(ProductBaseline productBaseline) {
        // Prevent NullPointerException
        if(productBaseline == null){
            throw new IllegalArgumentException("Cannot instantiate a BaselineProductConfigSpec without a baseline");
        }
        this.productBaseline = productBaseline;
    }

    public ProductBaseline getProductBaseline() {
        return productBaseline;
    }
    public void setProductBaseline(ProductBaseline productBaseline) {
        this.productBaseline = productBaseline;
    }

    public int getPartCollectionId(){
        return productBaseline.getPartCollection().getId();
    }

    @Override
    public PartIteration filterPartIteration(PartMaster partMaster) {

        PartCollection partCollection = productBaseline.getPartCollection();
        BaselinedPartKey baselinedRootPartKey = new BaselinedPartKey(partCollection.getId(), partMaster.getWorkspaceId(), partMaster.getNumber());
        BaselinedPart baselinedRootPart = partCollection.getBaselinedPart(baselinedRootPartKey);

        if (baselinedRootPart != null) {
            return baselinedRootPart.getTargetPart();
        }

        return null;
    }

    @Override
    public PartLink filterPartLink(List<PartLink> path) {

        // No ambiguities here, must return 1 value
        // Check if optional or substitute, nominal link else

        PartLink nominalLink = path.get(path.size()-1);

        if(nominalLink.isOptional() && !productBaseline.isOptionalLinkRetained(Tools.getPathAsString(path))){
            return null;
        }

        for(PartSubstituteLink substituteLink:nominalLink.getSubstitutes()){

            List<PartLink> substitutePath = new ArrayList<>(path);
            substitutePath.set(substitutePath.size()-1,substituteLink);

            if(productBaseline.hasSubstituteLink(Tools.getPathAsString(substitutePath))){
                return substituteLink;
            }
        }

        return nominalLink;
    }

}
