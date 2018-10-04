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

package org.polarsys.eplmp.server.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartRevision;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductUtilTest {

    private int [][] mappingUsageLink_tab = {

            {6,7}, {8,9}, {10}, {}, {11},//related to : part1, part2, part3, part4, part5
            {12}, {}, {}, {}, {},     //related to    : part6, part7, part8, part9, part10
            {13}, {14}, {15}, {}, {16},//related to   : part11, part12, part13, part14, part15
            {17}, {}, {18}, {19}, {}//related to      : part16, part17, part18, part19, part20
    };

    @Before
    public void setUp() throws Exception {

        initMocks(this);
        setMappingUsageLink_tab(mappingUsageLink_tab);//reset to default
        createTestableParts();
    }

    @Test
    public void createTestablePartsTest() {

        //Verify we've got right parts with expected assembly
        //Have a look into ProductUtil.java if you want to see the expected parts assembly
        //Only six first are enough to test all default parts
        //BEGIN : CHECKING PARTS ASSEMBLY

        assertNotNull(existingPart_list);
        assertEquals(20, existingPart_list.size());
        assertEquals(defaultPartsNumber_list[0], existingPart_list.get(0).getNumber());
        //Part1
        assertEquals(1, existingPart_list.get(0).getPartRevisions().size());
        assertEquals(1, existingPart_list.get(0).getLastRevision().getPartIterations().size());
        assertEquals(2, existingPart_list.get(0).getLastRevision().getLastIteration().getComponents().size());

        //Part2
        assertEquals(1, existingPart_list.get(1).getPartRevisions().size());
        assertEquals(1, existingPart_list.get(1).getLastRevision().getPartIterations().size());
        assertEquals(2, existingPart_list.get(1).getLastRevision().getLastIteration().getComponents().size());

        //Part3
        assertEquals(1,
                existingPart_list.get(2)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(2)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(0,
                existingPart_list.get(2)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        //Part4
        assertEquals(0,
                existingPart_list.get(3)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().size()
        );

        //Part5
        assertEquals(1,
                existingPart_list.get(4)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(4)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(4)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(0,
                existingPart_list.get(4)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        //Part6
        assertEquals(1,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(1,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );

        assertEquals(0,
                existingPart_list.get(5)
                        .getLastRevision()
                        .getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().get(0)
                        .getComponent().getLastRevision().getLastIteration()
                        .getComponents().size()
        );
        //END : CHECKING PARTS ASSEMBLY
    }

    @Test
    public void getPartMetaDataTest() {

        //allow test if metadata was created correctly
        assertNotNull(existingPart_list);
        assertEquals(20, existingPart_list.size());
        assertEquals(defaultPartsNumber_list.length, existingPart_list.size());
        assertEquals(defaultPartsNumber_list[0], existingPart_list.get(0).getNumber());
        metadata_as_same_as_partNumberList(defaultPartsNumber_list.length -1);
    }

    @Test
    public void add_revision_to_part_with_test(){

        assertNotNull(existingPart_list);
        assertEquals(20, existingPart_list.size());
        assertEquals(defaultPartsNumber_list.length, existingPart_list.size());
        assertEquals(defaultPartsNumber_list[0], existingPart_list.get(0).getNumber());
        can_add_new_revision_to_part(defaultPartsNumber_list[0]);
    }

    @Test
    public void add_iteration_to_revision_test(){

        assertNotNull(existingPart_list);
        assertEquals(20, existingPart_list.size());
        assertEquals(defaultPartsNumber_list.length, existingPart_list.size());
        assertEquals(defaultPartsNumber_list[0], existingPart_list.get(0).getNumber());
        can_add_new_iteration_to_revision(defaultPartsNumber_list[0]);
    }

    @Test
    public void loop_detection() {

        int [][] MappingUsageLink_tab = {

                {6,7}, {8,9}, {10}, {}, {11},//related to : part1, part2, part3, part4, part5
                {12}, {}, {}, {}, {},     //related to    : part6, part7, part8, part9, part10
                {13}, {14}, {15}, {}, {16},//related to   : part11, part12, part13, part14, part15
                {17}, {}, {18}, {19}, {15}//related to      : part16, part17, part18, part19, part20
        };

        setMappingUsageLink_tab(MappingUsageLink_tab);
        try {

            createTestableParts();
            Assert.fail();
        } catch (Exception e) {

            Assert.assertTrue(true);
        }
    }

    private void can_add_new_iteration_to_revision(String forPartNumber){

        PartRevision revision = get_partRevision_with("A",forPartNumber);
        PartIteration it = new PartIteration();
        it.setIteration(1);

        //exiting iteration;
        add_iteration_to_revision(revision.getVersion(),forPartNumber, it);
        assertEquals(1,get_partIterations_of(forPartNumber).size());

        //non existing iteration
        it.setIteration(12);
        add_iteration_to_revision(revision.getVersion(),forPartNumber, it);
        assertEquals(2,get_partIterations_of(forPartNumber).size());
    }

    private void can_add_new_revision_to_part(String partNumber){

        PartRevision partRevision = new PartRevision();
        partRevision.setVersion("A");
        add_revision_to_part_with(partNumber,partRevision, false);

        //already have version
        assertEquals(1, get_partRevisions_of(partNumber).size());

        //not have this version
        partRevision.setVersion("B");
        add_revision_to_part_with(partNumber,partRevision,false);
        assertEquals(2, get_partRevisions_of(partNumber).size());
    }

    private void metadata_as_same_as_partNumberList(int lastIndex) {

        if (lastIndex < 0) {

            return;
        }
        assertNotNull(get_partMaster_with(defaultPartsNumber_list[lastIndex]));
        boolean same_instance = get_partMaster_with(defaultPartsNumber_list[lastIndex]) instanceof PartMaster;
        assertTrue(same_instance);
        PartMaster partMaster_extractFromMetaData = get_partMaster_with(defaultPartsNumber_list[lastIndex]);

        assertNotNull(existingPart_list.get(lastIndex));
        PartMaster partMaster_extractFromList = existingPart_list.get(lastIndex);

        assertEquals(partMaster_extractFromList, partMaster_extractFromMetaData);

        //check revision
        assertNotNull(get_partRevisions_of(defaultPartsNumber_list[lastIndex]));
        same_instance = (get_partRevisions_of(defaultPartsNumber_list[lastIndex]) instanceof List);
        assertTrue(same_instance);
        List<PartRevision> partMaster_revisions_extractFromMetaData = get_partRevisions_of(defaultPartsNumber_list[lastIndex]);

        assertNotNull(partMaster_extractFromList.getPartRevisions());
        List<PartRevision> partMaster_revisions_extractFromList = partMaster_extractFromList.getPartRevisions();

        assertEquals(partMaster_revisions_extractFromList.size(),partMaster_revisions_extractFromMetaData.size());

        //check iteration
        assertNotNull(get_partIterations_of(defaultPartsNumber_list[lastIndex]));
        same_instance = (get_partIterations_of(defaultPartsNumber_list[lastIndex]) instanceof List);
        assertTrue(same_instance);
        List<PartIteration> partMaster_iterations_extractFromMetaData = get_partIterations_of(defaultPartsNumber_list[lastIndex]);

        //cause only one iteration by revision by default
        assertNotNull(partMaster_extractFromList.getLastRevision().getPartIterations());
        List<PartIteration> partMaster_iterations_extractFromList =  partMaster_extractFromList.getLastRevision().getPartIterations();
        assertEquals(partMaster_iterations_extractFromMetaData.size(), partMaster_iterations_extractFromList.size());
        metadata_as_same_as_partNumberList(lastIndex - 1);

        //check revision version
        String partMaster_revision_version_extractFromMetaData = partMaster_extractFromMetaData.getLastRevision().getVersion();
        String partMaster_revision_version_extractFromList = partMaster_extractFromList.getLastRevision().getVersion();

        assertEquals( partMaster_revision_version_extractFromMetaData, partMaster_revision_version_extractFromList );

        //check revision instance
        PartRevision partRevision_instance_fromMetadata = get_partRevision_with(partMaster_revision_version_extractFromList,partMaster_extractFromList.getNumber());

        assertNotNull(partRevision_instance_fromMetadata);
        assertEquals(partRevision_instance_fromMetadata, partMaster_extractFromList.getLastRevision());
    }
}