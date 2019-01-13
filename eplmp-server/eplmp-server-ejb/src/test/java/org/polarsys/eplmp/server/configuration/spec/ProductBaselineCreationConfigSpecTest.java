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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.product.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ProductBaselineCreationConfigSpecTest {

    private ProductBaselineCreationConfigSpec pB2CS;

    @Mock private PartMaster partMaster,pM1,pM2;
    @Mock private PartIteration pI1,pI2,lastIteration,lastChekedIn;
    @Mock private PartRevision pR1,pR2,lastReleased,lastRevision;
    @Mock private PartMasterKey pMK1,pMK2;

    @Mock private PartLink pL1,pL2,pL3;
    @Mock private PartSubstituteLink pSL1,pSL2;

    @Before
    public void setUp(){

        pB2CS = mock(ProductBaselineCreationConfigSpec.class,CALLS_REAL_METHODS);
        initMocks(this);

        //Init list from constructor
        Whitebox.setInternalState(pB2CS,"partIterations", Arrays.asList(pI1,pI2));
        Whitebox.setInternalState(pB2CS,"substituteLinks",new ArrayList<>());
        Whitebox.setInternalState(pB2CS,"optionalUsageLinks",new ArrayList<>());

        //Init set from Parent
        Whitebox.setInternalState(pB2CS,"retainedPartIterations",new HashSet<>());
        Whitebox.setInternalState(pB2CS,"retainedOptionalUsageLinks",new HashSet<>());
        Whitebox.setInternalState(pB2CS,"retainedSubstituteLinks",new HashSet<>());

        when(pL1.getFullId()).thenReturn("optLink1");
        when(pL2.getFullId()).thenReturn("optLink2");
        when(pL3.getFullId()).thenReturn("optLink3");

        when(pSL1.getFullId()).thenReturn("subsLink1");
        when(pSL2.getFullId()).thenReturn("subsLink2");
    }

    @Test
    public void filterPartIterationTest(){

        //****** I - RELEASED PRODUCT BASELINE TYPED
        //------------ TEST : No same PartMasterKey ------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(pB2CS,"type", ProductBaselineType.RELEASED);

        when(pI1.getPartRevision()).thenReturn(pR1);
        when(pI2.getPartRevision()).thenReturn(pR2);

        when(pR1.getPartMaster()).thenReturn(pM1);
        when(pR2.getPartMaster()).thenReturn(pM2);

        when(partMaster.getKey()).thenReturn(new PartMasterKey());
        when(pM1.getKey()).thenReturn(pMK1);
        when(pM2.getKey()).thenReturn(pMK2);

        when(partMaster.getLastReleasedRevision()).thenReturn(lastReleased);
        when(lastReleased.getLastIteration()).thenReturn(lastIteration);
        //#### END CONFIGURATION

        PartIteration result = pB2CS.filterPartIteration(partMaster);

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(lastIteration,result);
        //#### END VERIFICATION

        //------------ TEST : One with same PartMasterKey ------------
        //#### BEGIN CONFIGURATION
        when(partMaster.getKey()).thenReturn(pMK2);
        //#### END CONFIGURATION

        result = pB2CS.filterPartIteration(partMaster);

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(pI2,result);
        //#### END VERIFICATION

        //------------ TEST : Nothing to return ------------
        //#### BEGIN CONFIGURATION
        when(pM1.getKey()).thenReturn(pMK1);
        when(pM2.getKey()).thenReturn(pMK1);
        when(partMaster.getLastReleasedRevision()).thenReturn(null);
        //#### END CONFIGURATION

        result = pB2CS.filterPartIteration(partMaster);

        //#### BEGIN VERIFICATION
        Assert.assertNull(result);
        //#### END VERIFICATION

        //****** I - LATEST PRODUCT BASELINE TYPED
        //------------ TEST : One with same PartMasterKey ------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(pB2CS,"type", ProductBaselineType.LATEST);
        when(partMaster.getLastRevision()).thenReturn(lastRevision);
        when(lastRevision.getLastCheckedInIteration()).thenReturn(lastChekedIn);
        //#### END CONFIGURATION

        result = pB2CS.filterPartIteration(partMaster);

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(lastChekedIn,result);
        //#### END VERIFICATION

        //------------ TEST : Nothing to return ------------
        //#### BEGIN CONFIGURATION
        when(lastRevision.getLastCheckedInIteration()).thenReturn(null);
        //#### END CONFIGURATION

        result = pB2CS.filterPartIteration(partMaster);

        //#### BEGIN VERIFICATION
        Assert.assertNull(result);
        //#### END VERIFICATION

        //#### BEGIN VERIFICATION : Retained Iteration count number
        HashSet<String> retainedIt =(HashSet) Whitebox.getInternalState(pB2CS, "retainedPartIterations");
        Assert.assertEquals(3, retainedIt.size());
        //#### END VERIFICATION : Retained Iteration count number
    }

    @Test
    public void filterPartLinkTest(){

        //----------- TEST : Optional link not retain and no substitute link -----------
        //#### BEGIN CONFIGURATION
        when(pL3.isOptional()).thenReturn(true);//pL3 because implementation take last of list in parameter
        //#### END CONFIGURATION

        PartLink result = pB2CS.filterPartLink(Arrays.asList(pL1,pL2,pL3));

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(pL3,result);//Nominal link must be returned
        //#### END VERIFICATION

        //----------- TEST : Optional link retain and no substitute link -----------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(pB2CS,"optionalUsageLinks", Collections.singletonList("optLink1-optLink2-optLink3"));
        //#### END CONFIGURATION

        result = pB2CS.filterPartLink(Arrays.asList(pL1,pL2,pL3));

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(pL3,result);//Nominal link must be returned
        //#### END VERIFICATION

        //----------- TEST : Two optional links one retained and one substitute link detected -----------
        //#### BEGIN CONFIGURATION
        when(pL1.isOptional()).thenReturn(true);
        when(pL2.isOptional()).thenReturn(true);
        when(pL2.getSubstitutes()).thenReturn(Arrays.asList(pSL1,pSL2));
        Whitebox.setInternalState(pB2CS,"optionalUsageLinks", Collections.singletonList("optLink1-optLink2"));
        Whitebox.setInternalState(pB2CS,"substituteLinks", Collections.singletonList("optLink1-subsLink1"));
        //#### END CONFIGURATION

        result = pB2CS.filterPartLink(Arrays.asList(pL1,pL2));

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(pSL1,result);//Nominal link must be returned
        //#### END VERIFICATION


        //----------- TEST : Optional link retained and no substitute link retained -----------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(pB2CS,"optionalUsageLinks", Collections.singletonList("optLink1"));
        Whitebox.setInternalState(pB2CS,"substituteLinks", Collections.singletonList(""));
        when(pL1.getSubstitutes()).thenReturn(Arrays.asList(pSL1));
        //#### END CONFIGURATION

        result = pB2CS.filterPartLink(Arrays.asList(pL1));

        //#### BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(pL1,result);//Nominal link must be returned
        //#### END VERIFICATION

        //#### BEGIN VERIFICATION : Retained Link count number
        HashSet<String> retainedOpt =(HashSet) Whitebox.getInternalState(pB2CS, "retainedOptionalUsageLinks");
        HashSet<String> retainedSubs =(HashSet) Whitebox.getInternalState(pB2CS, "retainedSubstituteLinks");
        Assert.assertEquals(3, retainedOpt.size());
        Assert.assertEquals(1, retainedSubs.size());
        //#### END VERIFICATION : Retained Link count number
    }
}