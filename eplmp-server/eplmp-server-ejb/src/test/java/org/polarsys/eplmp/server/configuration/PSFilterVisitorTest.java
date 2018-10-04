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
        this.initDaoBahavior();
        callbacks = this.createCallBack();
    }

    @Test
    public void visit_latestCheckedIn_filter() throws Exception {

        //#### TEST VISIT METHOD WITH PART MASTER AS PARAMETER

        //~ TESTED STRUCTURE : one release, one iteration and one Part with one child by nodes except last
        Component result = psFilterVisitor.visit(WORKSPACE_ID, new LatestCheckedInPSFilter(false), get_partMaster_with(defaultPartsNumber_list[5]), -1, callbacks);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getComponents().size());
        Assert.assertEquals(1, result.getComponents().get(0).getComponents().size());
        Assert.assertEquals(1, result.getComponents().get(0).getComponents().get(0).getComponents().size());
        Assert.assertEquals(1, result.getComponents().get(0).getComponents().get(0).getComponents().get(0).getComponents().size());
        Assert.assertEquals(0, result.getComponents().get(0).getComponents().get(0).getComponents().get(0).getComponents().get(0).getComponents().get(0).getComponents().size());

        //~ TESTED STRUCTURE : different releases with some different iterations, complex structured parts ( simulate real case )
    }

    @Test
    public void visit_latestReleased_filter() throws Exception {

        //#### TEST VISIT METHOD WITH PART MASTER AS PARAMETER
        //~ TESTED STRUCTURE : Any Released
        try{

            psFilterVisitor.visit(WORKSPACE_ID, new LatestReleasedPSFilter(false),get_partMaster_with(defaultPartsNumber_list[5]), -1, callbacks);
            Assert.fail();
        }catch (NotAllowedException e){

            Assert.assertTrue(true);
        }
    }

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

            when(partMasterDAO.loadPartM(new PartMasterKey(WORKSPACE_ID,partNumber))).thenReturn(get_partMaster_with(partNumber));
        }
    }
}