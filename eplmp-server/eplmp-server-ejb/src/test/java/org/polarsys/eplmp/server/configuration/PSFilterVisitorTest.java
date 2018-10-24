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

package org.polarsys.eplmp.server.configuration;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.polarsys.eplmp.server.util.ProductUtil.*;

import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.exceptions.PartMasterNotFoundException;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.server.configuration.filter.LatestCheckedInPSFilter;
import org.polarsys.eplmp.server.configuration.filter.LatestReleasedPSFilter;
import org.polarsys.eplmp.server.dao.PartMasterDAO;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PSFilterVisitor.class)
public class PSFilterVisitorTest {

    @InjectMocks
    private PSFilterVisitor psFilterVisitor = new PSFilterVisitor();

    @Mock
    private PartMasterDAO partMasterDAO;

    private PSFilterVisitorCallbacks callbacks;

    @Before
    public void setUp() throws Exception {

        initMocks(this);
        createTestableParts();
        buildBasicStructure();
        this.initDaoBahavior();
        callbacks = this.createCallBack();
    }

    @Test
    public void visitLatestCheckedInFilterTest() throws Exception {

        //#### TEST VISIT METHOD WITH PART MASTER AS PARAMETER
        //Notice : refer to BASIC STRUCTURE in ProductUtil.java

        //~ TEST : With simple structure ( No diverge )
        Component result = psFilterVisitor.visit(WORKSPACE_ID, new LatestCheckedInPSFilter(false), getPartMasterWith("PART-006"), -1, callbacks);
        assertNotNull(result);
        assertEquals("B",result.getRetainedIteration().getVersion());
        assertEquals("PART-006",result.getPartMaster().getNumber());

        assertEquals(3, result.getComponents().size());
        assertEquals(1, result.getComponents().get(0).getComponents().size());
        assertEquals("A",result.getComponents().get(0).getRetainedIteration().getPartVersion());
        assertEquals("PART-012",result.getComponents().get(0).getPartMaster().getNumber());

        assertEquals(3, result.getComponents().get(1).getComponents().size());
        assertEquals("B",result.getComponents().get(1).getRetainedIteration().getPartVersion());
        assertEquals("PART-002",result.getComponents().get(1).getPartMaster().getNumber());

        assertEquals(0, result.getComponents().get(2).getComponents().size());
        assertEquals("A",result.getComponents().get(2).getRetainedIteration().getPartVersion());
        assertEquals("PART-004",result.getComponents().get(2).getPartMaster().getNumber());

        //~ TEST : Complex structured parts ( No diverge )

        //Only one iteration on last checked out revision
        assertEquals("E",getPartMasterWith("PART-001").getLastRevision().getVersion());
        assertTrue(getPartMasterWith("PART-001").getLastRevision().isCheckedOut());
        assertEquals(1,getPartMasterWith("PART-001").getLastRevision().getPartIterations().size());
        try {

            psFilterVisitor.visit(WORKSPACE_ID, new LatestCheckedInPSFilter(false), getPartMasterWith("PART-001"), -1, callbacks);
            fail();
        }catch (NotAllowedException e){

            assertTrue(true);
        }

        //Add new iteration with same members than last iteration
        addIterationTo("PART-001",
                getPartMasterWith("PART-001").getLastRevision().createNextIteration(user));

        assertEquals("E",getPartMasterWith("PART-001").getLastRevision().getVersion());
        assertTrue(getPartMasterWith("PART-001").getLastRevision().isCheckedOut());
        assertEquals(2,getPartMasterWith("PART-001").getLastRevision().getPartIterations().size());

        result = psFilterVisitor.visit(WORKSPACE_ID, new LatestCheckedInPSFilter(false), getPartMasterWith("PART-001"), -1, callbacks);
        assertNotNull(result);
        assertEquals(1, result.getRetainedIteration().getIteration());

        //#### TEST VISIT METHOD WITH PART LINKS AS PARAMETER
    }

    @Test
    public void visitLatestReleasedFilterTest() throws Exception {

        //#### TEST VISIT METHOD WITH PART MASTER AS PARAMETER
        //~ TEST : None Released
        try{

            psFilterVisitor.visit(WORKSPACE_ID, new LatestReleasedPSFilter(false), getPartMasterWith(defaultPartsNumber_list[5]), -1, callbacks);
            Assert.fail();
        }catch (NotAllowedException e){

            Assert.assertTrue(true);
        }
    }

    //############################## HELPER METHODS ##############################

    private PSFilterVisitorCallbacks createCallBack(){

        return new PSFilterVisitorCallbacks() {
            @Override
            public void onIndeterminateVersion(PartMaster partMaster, List<PartIteration> partIterations) throws NotAllowedException {
                throw new NotAllowedException("NotAllowedException48");
            }

            @Override
            public void onUnresolvedVersion(PartMaster partMaster) throws NotAllowedException {
                throw new NotAllowedException("NotAllowedException49", partMaster.getNumber());
            }

            @Override
            public void onIndeterminatePath(List<PartLink> pCurrentPath, List<PartIteration> pCurrentPathPartIterations) throws NotAllowedException {
                throw new NotAllowedException("NotAllowedException50");
            }

            @Override
            public void onUnresolvedPath(List<PartLink> pCurrentPath, List<PartIteration> partIterations) throws NotAllowedException {
                throw new NotAllowedException("NotAllowedException51");
            }

            @Override
            public boolean onPathWalk(List<PartLink> path, List<PartMaster> parts) {
                return true;
            }
        };
    }

    private void initDaoBahavior() throws PartMasterNotFoundException {

        for(String partNumber :  defaultPartsNumber_list){

            when(partMasterDAO.loadPartM(new PartMasterKey(WORKSPACE_ID,partNumber))).thenReturn(getPartMasterWith(partNumber));
        }
    }
}