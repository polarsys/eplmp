/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.configuration.spec;

import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartSubstituteLink;
import org.polarsys.eplmp.core.util.Tools;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Morgan Guimard
 */
public class ResolvedCollectionConfigSpec extends ProductConfigSpec {

    private PartCollection partCollection;
    private Set<String> optionalUsageLinks;
    private Set<String> substitutesUsageLinks;

    public ResolvedCollectionConfigSpec(@NotNull ResolvedCollection resolvedCollection) {
        this.partCollection = resolvedCollection.getPartCollection();
        this.optionalUsageLinks= resolvedCollection.getOptionalUsageLinks();
        this.substitutesUsageLinks = resolvedCollection.getSubstituteLinks();
    }

    @Override
    public PartIteration filterPartIteration(PartMaster part) {
        if(partCollection != null) {
            BaselinedPartKey baselinedRootPartKey = new BaselinedPartKey(partCollection.getId(), part.getWorkspaceId(), part.getNumber());
            BaselinedPart baselinedRootPart = partCollection.getBaselinedPart(baselinedRootPartKey);
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

        if(nominalLink.isOptional() && !optionalUsageLinks.contains(Tools.getPathAsString(path))){
            return null;
        }

        for(PartSubstituteLink substituteLink:nominalLink.getSubstitutes()){
            List<PartLink> substitutePath = new ArrayList<>(path);
            substitutePath.set(substitutePath.size()-1,substituteLink);

            if(substitutesUsageLinks.contains(Tools.getPathAsString(substitutePath))){
                return substituteLink;
            }

        }

        return nominalLink;

    }

}
