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
        createReleasedRevisionsForTestWith("PART-001");
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

    //############################################ HELPERS METHODS ############################################

    private void createReleasedRevisionsForTestWith(String partNumber){

        PartMaster partMaster = getPartMasterWith(partNumber);

        //Configure members of partMaster
        String[] membersPartMaster = {"PART-006","PART-003","PART-005","PART-008","PART-007"};
        String[] membersPartMasterRevisionK = {"PART-011","PART-012","PART-008","PART-007","PART-016"};

        //configure substitutes of members
        String[] subtitutesForPart011 = {"PART-014"};
        String[] subtitutesForPart012 = {"PART-015"};
        String[] subtitutesForPart016 = {"PART-018","PART-015","PART-009","PART-012"};
        String[] subtitutesForPart007 = {"PART-002","PART-005"};
        String[] subtitutesForPart008 = {"PART-004"};

        // true <=> released
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, true);//  revision B
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, false);// revision C
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, true);//  revision D
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, false);// revision E
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, false);// revision F
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, true);//  revision G
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, true);//  revision H
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, false);// revision I
        addRevisionWithPartLinkTo(partMaster, membersPartMaster, true);//  revision J
        addRevisionWithPartLinkTo(partMaster, membersPartMasterRevisionK, true);//  revision K

        //Add substitutes to members in revision K
        addSubstituteInLastIterationOfLastRevisionTo(partMaster,subtitutesForPart011,"PART-011");
        addSubstituteInLastIterationOfLastRevisionTo(partMaster,subtitutesForPart012,"PART-012");
        addSubstituteInLastIterationOfLastRevisionTo(partMaster,subtitutesForPart016,"PART-016");
        addSubstituteInLastIterationOfLastRevisionTo(partMaster,subtitutesForPart007,"PART-007");
        addSubstituteInLastIterationOfLastRevisionTo(partMaster,subtitutesForPart008,"PART-008");
    }
}