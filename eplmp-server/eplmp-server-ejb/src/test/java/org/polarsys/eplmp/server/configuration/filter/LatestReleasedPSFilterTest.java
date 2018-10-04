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
    public void filterTest_with_partMaster_as_parameter(){

        /**
         *
         * Test level : 1.1.0 Many parts revision last of list is released
         *
         * Structure for test :
         *
         *          PART-001
         *              |-> DEFAULT  A ( not released )
         *              |-> REVISION B ( not released )
         *              |-> REVISION C ( not released )
         *              |-> REVISION D ( released )
         *
         */
        PartMaster partMaster = get_partMaster_with("PART-001");
        String[] members = {};
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members, true);

        List<PartIteration> result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        are_those_of_revision("D",result,partMaster.getNumber());

        //--------------------------
        /**
         *
         * Test level : 1.1.1 Only first of list released
         *
         * Structure for test :
         *
         *          PART-002
         *              |-> DEFAULT  A ( released )
         *              |-> REVISION B ( not released )
         *              |-> REVISION C ( not released )
         *              |-> REVISION D ( not released )
         *
         */
        partMaster = get_partMaster_with("PART-002");
        partMaster.getLastRevision().release(user);
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members, false);

        result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        are_those_of_revision("A",result,"PART-002");

        //--------------------------
        /**
         *
         * Test level : 1.1.2 Any released
         *
         * Structure for test :
         *
         *          PART-003
         *              |-> DEFAULT  A ( not released )
         *              |-> REVISION B ( not released )
         *              |-> REVISION C ( not released )
         *              |-> REVISION D ( not released )
         *
         */
        partMaster = get_partMaster_with("PART-003");
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members,false);
        add_revision_with_partLink_to(partMaster,members, false);

        result =  latestReleasedPSFilter.filter(partMaster);

        assertTrue(result.isEmpty());

        //--------------------------
        /**
         *
         * Test level : 1.1.3 All released except last of list
         *
         * Structure for test :
         *
         *          PART-004
         *              |-> DEFAULT  A ( released )
         *              |-> REVISION B ( released )
         *              |-> REVISION C ( released )
         *              |-> REVISION D ( not released )
         *
         */
        partMaster = get_partMaster_with("PART-004");
        partMaster.getLastRevision().release(user);
        add_revision_with_partLink_to(partMaster,members,true);
        add_revision_with_partLink_to(partMaster,members,true);
        add_revision_with_partLink_to(partMaster,members, false);

        result =  latestReleasedPSFilter.filter(partMaster);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());
        are_those_of_revision("C",result,"PART-004");
    }
}