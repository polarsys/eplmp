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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartSubstituteLink;
import org.polarsys.eplmp.core.util.Tools;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link org.polarsys.eplmp.core.configuration.ProductConfigSpec} which returns the {@link org.polarsys.eplmp.core.product.PartIteration} and {@link org.polarsys.eplmp.core.document.DocumentIteration}
 * which belong to the given baseline.
 *
 * @author Florent Garin
 * @version 1.1, 30/10/11
 * @since   V1.1
 */
public class ProductInstanceConfigSpec extends ProductConfigSpec {

    private ProductInstanceIteration productInstanceIteration;
    private User user;

    public ProductInstanceConfigSpec(){
    }
    public ProductInstanceConfigSpec(ProductInstanceIteration productInstanceIteration, User user) {
        this.productInstanceIteration = productInstanceIteration;
        this.user = user;
    }

    public ProductInstanceIteration getProductInstanceIteration() {
        return productInstanceIteration;
    }
    public void setProductInstanceIteration(ProductInstanceIteration productInstanceIteration) {
        this.productInstanceIteration = productInstanceIteration;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public int getPartCollectionId(){
        return productInstanceIteration.getPartCollection().getId();
    }

    @Override
    public PartIteration filterPartIteration(PartMaster part) {
        PartCollection partCollection = productInstanceIteration==null ? null : productInstanceIteration.getPartCollection();// Prevent NullPointerException
        if(partCollection != null) {
            BaselinedPartKey baselinedRootPartKey = new BaselinedPartKey(partCollection.getId(), part.getWorkspaceId(), part.getNumber());
            BaselinedPart baselinedRootPart = productInstanceIteration.getBaselinedPart(baselinedRootPartKey);
            if (baselinedRootPart != null) {
                return baselinedRootPart.getTargetPart();
            }
        }
        return null;
    }

    @Override
    public PartLink filterPartLink(List<PartLink> path) {
        // No ambiguities here, must return 1 value
        // Check if optional or substitute, nominal link else
        PartLink nominalLink = path.get(path.size()-1);

        if(nominalLink.isOptional() && !productInstanceIteration.isOptionalLinkRetained(Tools.getPathAsString(path))){
            return null;
        }

        for(PartSubstituteLink substituteLink:nominalLink.getSubstitutes()){

            List<PartLink> substitutePath = new ArrayList<>(path);
            substitutePath.set(substitutePath.size()-1,substituteLink);

            if(productInstanceIteration.hasSubstituteLink(Tools.getPathAsString(substitutePath))){
                return substituteLink;
            }

        }

        return nominalLink;

    }

}
