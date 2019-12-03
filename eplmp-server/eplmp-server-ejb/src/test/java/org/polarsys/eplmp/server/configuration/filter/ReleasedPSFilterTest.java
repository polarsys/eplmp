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

package org.polarsys.eplmp.server.configuration.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class ReleasedPSFilterTest {

    private ReleasedPSFilter releasedPSFilter;

    @Before
    public void setup() throws Exception {

        createTestableParts();
        releasedPSFilter = new ReleasedPSFilter(false);
        generateSomeReleasedRevisionWithSubstitutesFor("PART-001");
    }

    @Test
    public void filterTestWithPartMasterAsParameter(){

        List<PartIteration> result = releasedPSFilter.filter(getPartMasterWith("PART-001"));
        assertFalse(result.isEmpty());
        assertEquals(6,result.size());

        assertEquals("K",result.get(0).getPartVersion());
        assertEquals("J",result.get(1).getPartVersion());
        assertEquals("H",result.get(2).getPartVersion());
        assertEquals("G",result.get(3).getPartVersion());
        assertEquals("D",result.get(4).getPartVersion());
        assertEquals("B", result.get(5).getPartVersion());
    }

    @Test
    public void filterTestWithListPartLinkAsParameterTest(){

        //diverge not enable
        PartMaster partMaster = getPartMasterWith("PART-001");

        List<PartLink> links =  new ArrayList<>((partMaster.getLastRevision().getLastIteration().getComponents()));
        List<PartLink> result  = releasedPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(1,result.size());

        //enable diverge
        releasedPSFilter = new ReleasedPSFilter(true);
        result  = releasedPSFilter.filter(links);

        assertFalse(result.isEmpty());
        assertEquals(5,result.size());
        assertEquals("PART-016-UsageLink",result.get(0).getReferenceDescription());
        assertEquals("PART-018-Substitute",result.get(1).getReferenceDescription());
        assertEquals("PART-015-Substitute",result.get(2).getReferenceDescription());
        assertEquals("PART-009-Substitute",result.get(3).getReferenceDescription());
        assertEquals("PART-012-Substitute",result.get(4).getReferenceDescription());
    }
}