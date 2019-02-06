/*******************************************************************************
 * Copyright (c) 2017-2019 DocDoku.
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
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.meta.RevisionStatus;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartRevision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductBaselineCreationConfigSpecTest {

    private ProductBaselineCreationConfigSpec pB2CS;
    private PartMaster partMaster;

    @Before
    public void setUp() throws Exception {

        createTestableParts();
        generateSomeReleasedRevisionWithSubstitutesFor("PART-001");
        partMaster = getPartMasterWith("PART-001");
        List<PartIteration> iterations = new ArrayList<>();

        for(PartRevision revision : partMaster.getAllReleasedRevisions()){

            iterations.addAll(revision.getPartIterations());
        }

        pB2CS = new ProductBaselineCreationConfigSpec(ProductBaselineType.RELEASED,
                iterations,
                Collections.singletonList(""), Collections.singletonList(""));
    }

    @Test
    public void filterPartIterationTest(){

        //Test with RELEASED TYPE
        PartIteration result = pB2CS.filterPartIteration(partMaster);
        assertNotNull(result);
        areThoseOfRevision("K", Collections.singletonList(result),partMaster.getNumber());
        assertFalse(pB2CS.getRetainedPartIterations().isEmpty());
        assertEquals(1,pB2CS.getRetainedPartIterations().size());
        assertTrue(pB2CS.getRetainedPartIterations().contains(result));

        //Change partMaster
        //Expected null cause have not released revision
        partMaster = getPartMasterWith("PART-006");
        result = pB2CS.filterPartIteration(partMaster);
        assertNull(result);

        //add one released version
        addIterationTo(
                partMaster.getNumber(),
                partMaster.getLastRevision().createNextIteration(user));
        partMaster.getLastRevision().setStatus(RevisionStatus.RELEASED);

        result = pB2CS.filterPartIteration(partMaster);
        assertNotNull(result);
        assertEquals(partMaster.getLastReleasedRevision().getLastIteration(),result);

        //Test with LATEST TYPE
        pB2CS = new ProductBaselineCreationConfigSpec(ProductBaselineType.LATEST,
                new ArrayList<>(),
                Collections.singletonList(""), Collections.singletonList(""));
        partMaster = getPartMasterWith("PART-005");
        result = pB2CS.filterPartIteration(partMaster);
        assertNotNull(result);
        assertEquals(partMaster.getLastRevision().getLastCheckedInIteration(),result);
    }

    @Test
    public void filterPartLinkTest(){

        //PartLinks in list are optionals but not in the optionalUsageLinks list of pB2CS instance
        PartMaster partMaster = getPartMasterWith("PART-001");
        List<PartLink> links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));

        PartLink result =  pB2CS.filterPartLink(links);
        assertNotNull(result);
        assertEquals(links.get(links.size()-1),result);
        assertTrue(pB2CS.getRetainedOptionalUsageLinks().isEmpty());
        assertTrue(pB2CS.getRetainedSubstituteLinks().isEmpty());

        //Now, add those optional links to the optionalUsageLinks list of pB2CS instance
        //Expected the nominal link because we didn't set the substitutesUsageLinks list of pB2CS instance
        // nominal link <=> last partLink into given list

        pB2CS = new ProductBaselineCreationConfigSpec(ProductBaselineType.LATEST,
                new ArrayList<>(),
                Collections.singletonList(""),
                Collections.singletonList("u0-u0-u0-u0-u0"));

        result =  pB2CS.filterPartLink(links);
        assertNotNull(result);
        assertEquals(links.get(links.size() - 1), result);
        assertFalse(pB2CS.getRetainedOptionalUsageLinks().isEmpty());
        assertTrue(pB2CS.getRetainedSubstituteLinks().isEmpty());

        //Now, add substitutes of those optional links
        pB2CS = new ProductBaselineCreationConfigSpec(ProductBaselineType.LATEST,
                new ArrayList<>(),
                Collections.singletonList("u0-u0-u0-u0-s0"),
                Collections.singletonList("u0-u0-u0-u0-u0"));

        result =  pB2CS.filterPartLink(links);
        assertNotNull(result);
        assertEquals("PART-018-Substitute", result.getReferenceDescription());
        assertFalse(pB2CS.getRetainedOptionalUsageLinks().isEmpty());
        assertFalse(pB2CS.getRetainedSubstituteLinks().isEmpty());
    }
}