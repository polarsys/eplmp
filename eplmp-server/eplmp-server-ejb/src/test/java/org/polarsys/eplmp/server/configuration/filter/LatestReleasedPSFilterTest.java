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

package org.polarsys.eplmp.server.configuration.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMaster;

import java.util.List;

import static org.junit.Assert.*;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class LatestReleasedPSFilterTest {

    private LatestReleasedPSFilter latestReleasedPSFilter;

    @Before
    public void setup() throws Exception {

        createTestableParts();
        latestReleasedPSFilter = new LatestReleasedPSFilter(false);
    }

    @Test
    public void filterTestWithPartMasterAsParameterTest(){

        PartMaster partMaster = getPartMasterWith("PART-001");
        String[] members = {};

        // released <=> true
        addRevisionWithPartLinkTo(partMaster, members, false); // revision B
        addRevisionWithPartLinkTo(partMaster, members, false); // revision C
        addRevisionWithPartLinkTo(partMaster, members, true);  // revision D

        List<PartIteration> result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        areThoseOfRevision("D", result, partMaster.getNumber());

        //--------------------------

        partMaster = getPartMasterWith("PART-002");
        partMaster.getLastRevision().release(user);
        addRevisionWithPartLinkTo(partMaster, members, false); // revision B
        addRevisionWithPartLinkTo(partMaster, members, false); // revision C
        addRevisionWithPartLinkTo(partMaster, members, false); // revision D

        result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        areThoseOfRevision("A", result, "PART-002");

        //--------------------------

        partMaster = getPartMasterWith("PART-003");
        addRevisionWithPartLinkTo(partMaster, members, false); // revision B
        addRevisionWithPartLinkTo(partMaster, members, false); // revision C
        addRevisionWithPartLinkTo(partMaster, members, false); // revision D

        result =  latestReleasedPSFilter.filter(partMaster);

        assertTrue(result.isEmpty());

        //--------------------------

        partMaster = getPartMasterWith("PART-004");
        partMaster.getLastRevision().release(user);
        addRevisionWithPartLinkTo(partMaster, members, true); // revision B
        addRevisionWithPartLinkTo(partMaster, members, true); // revision C
        addRevisionWithPartLinkTo(partMaster, members, false);// revision D

        result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        areThoseOfRevision("C", result, "PART-004");
    }
}