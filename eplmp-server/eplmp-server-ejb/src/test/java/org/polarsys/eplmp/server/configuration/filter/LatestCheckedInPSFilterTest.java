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
    public void filterTest_with_partMaster_as_parameter(){

        /**
         *
         * Test level : 1.1.0 Many parts revision
         * Purpose : Check if we have right partIteration.
         *
         */
        setPartForLatestCheckInTest();
        PartMaster partMaster = get_partMaster_with("PART-001");
        List<PartIteration> result =  latestCheckedInPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        are_those_of_revision("D",result,partMaster.getNumber());

        //---------------------------
        /**
         * Test level : 1.1.1 Lastest is check out
         *
         * Purpose : Check behavior when first revision is check in and other
         * are check out in list of revision
         *
         */
        partMaster.getLastRevision().setCheckOutUser(user);
        result =  latestCheckedInPSFilter.filter(partMaster);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterTest_with_list_partLink_as_parameter(){

        /**
         *
         * Test level : 1.1.0 Diverge link with boolean false
         *
         * Structure for test
         *
         *      |-> PART-001
         *              |-> REV-A
         *                    |-> iteration 1
         *                               |-> PART-007
         *                               |       |-> REV-A
         *                               |             |-> iteration 1
         *                               |-> PART-008
         *                                       |-> REV-A
         *                                             |-> iteration 1
         *
         */
        PartMaster partMaster = get_partMaster_with("PART-001");

        List<PartLink> links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        List<PartLink> result  = latestCheckedInPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        assertEquals("PART-008-UsageLink",result.get(0).getReferenceDescription());

        //----------------------
        /**
         *
         * Test level : 1.2.0 Diverge link with boolean true no substitutes
         *
         * Structure for test
         *
         *      |-> PART-001
         *              |-> REVISION-B
         *                    |-> iteration 1
         *                               |-> PART-007
         *                               |       |-> REVISION-A
         *                               |              |-> iteration 1
         *                               |-> PART-008
         *                                       |-> REVISION-A
         *                                       |     |-> iteration 1
         *                                       |-> REVISION-B
         *                                             |-> iteration 1
         *                                                      |-> PART-004
         *                                                      |       |-> REVISION-A
         *                                                      |              |-> iteration 1
         *                                                      |-> PART-015
         *                                                              |-> REVISION-A
         *                                                                   |-> iteration 1
         *                                                                             |-> PART-017
         *                                                                                     |-> REVISION-A
         *                                                                                             |-> iteration 1
         */
        latestCheckedInPSFilter = new LatestCheckedInPSFilter(true);

        String[] members_008 = {"PART-004","PART-015"};
        add_revision_with_partLink_to(get_partMaster_with("PART-008"),members_008,false);

        String[] members =  {"PART-007","PART-008"};
        add_revision_with_partLink_to(partMaster,members,false);

        links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        result  = latestCheckedInPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        assertEquals("PART-008-UsageLink",result.get(0).getReferenceDescription());

        //----------------------
        /**
         *
         * Test level : 1.2.1 Diverge link with boolean true with substitutes
         *
         * Structure for test : same as before
         *
         */
        String[] substitutes_008 = {"PART-005", "PART-002","PART-004"};// check structure in ProductUtil.java
        add_Substitute_in_last_iteration_of_last_revision_to(partMaster,substitutes_008,"PART-008");

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

    /**
     *
     *  We'll add some revision parts to an existing which we'll transform
     *  according to following description :
     *
     *      PART-OO1
     *          |-> Default ( version A ) : check In with 1 PartIterations
     *          |-> REV-001 ( version B ) : check out with 3 PartIterations
     *          |-> REV-002 ( version C ) : check out with 2 PartIterations
     *          |-> REV-003 ( version D ) : check In with 2 PartIterations
     *
     */
    private void setPartForLatestCheckInTest(){

        // Notice : each parts created by ProductUtil class have a least one revision with one iteration
        PartMaster part = get_partMaster_with("PART-001");

        //Create REV-001
        PartRevision revision = part.createNextRevision(user);
        add_revision_to_part_with("PART-001",revision,true);
        add_iteration_to_revision(revision.getVersion(), "PART-001",revision.createNextIteration(user));
        add_iteration_to_revision(revision.getVersion(), "PART-001",revision.createNextIteration(user));

        //Create REV-002
        revision = part.createNextRevision(user);
        add_revision_to_part_with("PART-001",revision,true);
        add_iteration_to_revision(revision.getVersion(), "PART-001",revision.createNextIteration(user));

        //Create REV-003
        revision = part.createNextRevision(user);
        add_revision_to_part_with("PART-001",revision,false);
        add_iteration_to_revision(revision.getVersion(), "PART-001",revision.createNextIteration(user));
    }
}