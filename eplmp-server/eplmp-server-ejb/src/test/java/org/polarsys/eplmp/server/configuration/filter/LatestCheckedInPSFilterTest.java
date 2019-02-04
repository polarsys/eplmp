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


/**
 *
 * @author Ludovic BAREL on 10/18.
 *
 * */
package org.polarsys.eplmp.server.configuration.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class LatestCheckedInPSFilterTest {

    private LatestCheckedInPSFilter latestCheckedInPSFilter ;

    @Before
    public void setup() throws Exception {

        createTestableParts();
        latestCheckedInPSFilter = new LatestCheckedInPSFilter(false);
    }

    @Test
    public void filterTestWithPartMasterAsParameterTest(){

        setPartForLatestCheckInTest();
        PartMaster partMaster = getPartMasterWith("PART-001");
        List<PartIteration> result =  latestCheckedInPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        areThoseOfRevision("D", result, partMaster.getNumber());

        //---------------------------

        partMaster.getLastRevision().setCheckOutUser(user);
        result =  latestCheckedInPSFilter.filter(partMaster);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterTestWithListPartLinkAsParameterTest(){

        PartMaster partMaster = getPartMasterWith("PART-001");

        List<PartLink> links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        List<PartLink> result  = latestCheckedInPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        assertEquals("PART-008-UsageLink",result.get(0).getReferenceDescription());

        //----------------------

        latestCheckedInPSFilter = new LatestCheckedInPSFilter(true);

        String[] members_008 = {"PART-004","PART-015"};

        //released <=> true, checkout <=> true
        addRevisionWithPartLinkTo(getPartMasterWith("PART-008"), members_008, false, false);

        String[] members =  {"PART-007","PART-008"};
        addRevisionWithPartLinkTo(partMaster, members, false, false);

        links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        result  = latestCheckedInPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        assertEquals("PART-008-UsageLink",result.get(0).getReferenceDescription());

        //----------------------

        String[] substitutes_008 = {"PART-005", "PART-002","PART-004"};// check structure in ProductUtil.java
        addSubstituteInLastIterationOfLastRevisionTo(partMaster, substitutes_008, "PART-008");

        links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        result  = latestCheckedInPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(4,result.size());
        assertEquals("PART-008-UsageLink",result.get(0).getReferenceDescription());
        assertEquals("PART-005-Substitute",result.get(1).getReferenceDescription());
        assertEquals("PART-002-Substitute",result.get(2).getReferenceDescription());
        assertEquals("PART-004-Substitute",result.get(3).getReferenceDescription());
    }

    //############################################ HELPERS METHODS ############################################

    private void setPartForLatestCheckInTest(){

        // Notice : each parts created by ProductUtil class have a least one revision with one iteration
        PartMaster part = getPartMasterWith("PART-001");

        //Create REV-001
        PartRevision revision = part.createNextRevision(user);

        // checkedout <=> true
        addRevisionToPartWith("PART-001", revision, true);
        addIterationTo("PART-001", revision.createNextIteration(user));
        addIterationTo("PART-001", revision.createNextIteration(user));

        //Create REV-002
        revision = part.createNextRevision(user);
        addRevisionToPartWith("PART-001", revision, true);
        addIterationTo("PART-001", revision.createNextIteration(user));

        //Create REV-003
        revision = part.createNextRevision(user);
        addRevisionToPartWith("PART-001", revision, false);
        addIterationTo("PART-001", revision.createNextIteration(user));
    }
}