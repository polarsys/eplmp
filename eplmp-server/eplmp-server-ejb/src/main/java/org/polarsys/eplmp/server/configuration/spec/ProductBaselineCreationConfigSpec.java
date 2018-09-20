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

import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.configuration.ProductConfigSpec;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan Guimard
 */
public class ProductBaselineCreationConfigSpec extends ProductConfigSpec {

    private List<PartIteration> partIterations;
    private List<String> substituteLinks;
    private List<String> optionalUsageLinks;
    private ProductBaselineType type;


    public ProductBaselineCreationConfigSpec(ProductBaselineType type, List<PartIteration> partIterations, List<String> substituteLinks, List<String> optionalUsageLinks) {
        this.partIterations = partIterations;
        this.substituteLinks = substituteLinks;
        this.optionalUsageLinks = optionalUsageLinks;
        this.type = type;
    }

    @Override
    public PartIteration filterPartIteration(PartMaster partMaster) {

        if (type.equals(ProductBaselineType.RELEASED)) {

            for (PartIteration pi : partIterations) {
                if (pi.getPartRevision().getPartMaster().getKey().equals(partMaster.getKey())) {
                    retainedPartIterations.add(pi);
                    return pi;
                }
            }
            // Else, take the latest released
            PartRevision lastReleasedRevision = partMaster.getLastReleasedRevision();
            if (lastReleasedRevision != null) {
                PartIteration pi = lastReleasedRevision.getLastIteration();
                retainedPartIterations.add(pi);
                return pi;
            }

        } else if (type.equals(ProductBaselineType.LATEST)) {

            PartIteration pi = partMaster.getLastRevision().getLastCheckedInIteration();

            if (pi != null) {
                retainedPartIterations.add(pi);
                return pi;
            }
        }

        return null;
    }

    @Override
    public PartLink filterPartLink(List<PartLink> path) {

        // No ambiguities here, must return 1 value
        // Check if optional or substitute, nominal link else

        PartLink nominalLink = path.get(path.size() - 1);

        String pathAsString = Tools.getPathAsString(path);

        if (nominalLink.isOptional() && optionalUsageLinks.contains(pathAsString)) {
            retainedOptionalUsageLinks.add(pathAsString);
        }

        for (PartSubstituteLink substituteLink : nominalLink.getSubstitutes()) {

            List<PartLink> substitutePath = new ArrayList<>(path);
            substitutePath.set(substitutePath.size() - 1, substituteLink);

            String substitutePathAsString = Tools.getPathAsString(substitutePath);
            if (substituteLinks.contains(substitutePathAsString)) {
                retainedSubstituteLinks.add(substitutePathAsString);
                return substituteLink;
            }

        }

        return nominalLink;
    }

}
