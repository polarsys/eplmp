/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.configuration.spec;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.ProductBaseline;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.product.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

/**
 *
 * @author Ludovic Barel
 */

@RunWith(MockitoJUnitRunner.class)
public class ResolvedCollectionConfigSpecTest {

    private ResolvedCollectionConfigSpec r2CS;
    private ConfigurationItem configurationItem;
    private ProductBaseline productBaseline;

    @Before
    public void setup() throws Exception {

        createTestableParts();
        generateSomeReleasedRevisionWithSubstitutesFor("PART-001");
        configurationItem = new ConfigurationItem(user,new Workspace(WORKSPACE_ID),"","");
        configurationItem.setDesignItem(getPartMasterWith("PART-001"));
        productBaseline = new ProductBaseline(user,configurationItem,"prodBase_1", ProductBaselineType.RELEASED,"validated-config");
        productBaseline.addBaselinedPart(getPartMasterWith("PART-001").getLastRevision().getLastIteration());
        r2CS = new ResolvedCollectionConfigSpec(productBaseline);
    }

    @Test
    public void filterPartIterationTest(){

        //Test with productBaselineType set to RELEASED
        PartMaster partMaster = getPartMasterWith("PART-001");
        PartIteration result = r2CS.filterPartIteration(partMaster);
        assertNotNull(result);
        assertEquals(partMaster.getLastRevision().getLastIteration(), result);

        productBaseline.removeAllBaselinedParts();

        result = r2CS.filterPartIteration(partMaster);
        assertNull(result);

        //Test with productBaselineType set to LATEST
        partMaster = getPartMasterWith("PART-006");
        productBaseline.setType(ProductBaselineType.LATEST);

        //Ensure we work with another part
        assertEquals("PART-006", partMaster.getNumber());

        configurationItem.setDesignItem(partMaster);
        productBaseline.addBaselinedPart(partMaster.getLastRevision().getLastIteration());

        result = r2CS.filterPartIteration(partMaster);
        assertNotNull(result);
        assertEquals(partMaster.getLastRevision().getLastIteration(), result);
    }

    @Test
    public void filterPartLinkTest(){

        //PartLinks in list are optionals but not in the optionalUsageLinks list of r2CS instance
        PartMaster partMaster = getPartMasterWith("PART-001");
        List<PartLink> links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));

        PartLink result =  r2CS.filterPartLink(links);
        assertNull(result);

        //Now, add those optional links to the optionalUsageLinks list of r2CS instance
        //Expected the nominal link because we didn't set the substitutesUsageLinks list of r2CS instance
        // nominal link <=> last partLink into given list

        productBaseline.addOptionalUsageLink("u0-u0-u0-u0-u0");
        r2CS = new ResolvedCollectionConfigSpec(productBaseline);

        result =  r2CS.filterPartLink(links);
        assertNotNull(result);
        assertEquals(links.get(links.size()-1), result);

        //Now, add substitutes of those optional links

        productBaseline.addSubstituteLink("u0-u0-u0-u0-s0");
        r2CS = new ResolvedCollectionConfigSpec(productBaseline);

        result =  r2CS.filterPartLink(links);
        assertNotNull(result);
        assertEquals("PART-018-Substitute", result.getReferenceDescription());
    }
}