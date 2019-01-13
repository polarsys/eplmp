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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.*;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePartIterationPSFilterTest {

    private UpdatePartIterationPSFilter updatePartIterationPSFilter;

    @Mock
    private PartMaster partMaster;
    @Mock
    private PartRevision partRevision;
    @Mock
    private PartIteration partIteration;
    @Mock
    PartIteration lastIteration;
    @Mock
    PartIteration lastCheckedInIteration;
    @Mock
    private PartMasterKey rootKey;
    @Mock
    private PartIterationKey partIterationKey;
    @Mock
    private PartRevisionKey partRevisionKey;
    @Mock
    private PartMasterKey partMasterKey;
    @Mock
    private PartMasterKey partMasterKey_1;
    @Mock
    private PartLink partLink;
    @Mock
    private PartLink partLink_2;
    @Mock
    PartSubstituteLink partSubstituteLink;

    @Before
    public void setUp() throws Exception {

        initMocks(this);
    }

    @Test
    public void filter(){

        //******* I - Check filter with a PartMaster as parameter

        //-> BEGIN INITIALIZATION

        //## BEGIN CONFIGURATION
        when(partIteration.getKey()).thenReturn(partIterationKey);
        when(partIterationKey.getPartRevision()).thenReturn(partRevisionKey);
        when(partRevisionKey.getPartMaster()).thenReturn(partMasterKey);
        //## END CONFIGURATION

        updatePartIterationPSFilter = new UpdatePartIterationPSFilter(partIteration);

        //-> END INITIALIZATION

        //------------------------ Test : Same partMasterKey detected ------------------------
        //## BEGIN CONFIGURATION
        when(partMaster.getKey()).thenReturn(partMasterKey);
        //## END CONFIGURATION

        List<PartIteration> partIterations = updatePartIterationPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.size() == 1);
        Assert.assertEquals(partIteration,partIterations.get(0));
        //## END VERIFICATION

        //------------------------ Test : PartRevision checked out and lastCheckedInIteration not null ------------------------
        //## BEGIN CONFIGURATION
        when(partMaster.getKey()).thenReturn(partMasterKey_1);
        when(partMaster.getLastRevision()).thenReturn(partRevision);
        when(partRevision.getLastIteration()).thenReturn(lastIteration);
        when(partRevision.getLastCheckedInIteration()).thenReturn(lastCheckedInIteration);
        when(partRevision.isCheckedOut()).thenReturn(true);
        //## END CONFIGURATION

        partIterations = updatePartIterationPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.size() == 2);
        Assert.assertEquals(lastCheckedInIteration,partIterations.get(0));
        Assert.assertEquals(lastIteration,partIterations.get(1));
        //## END VERIFICATION

        //------------------------ Test : PartRevision not checked out and lastCheckedInIteration not null ------------------------
        //## BEGIN CONFIGURATION
        when(partRevision.isCheckedOut()).thenReturn(false);
        //## END CONFIGURATION

        partIterations = updatePartIterationPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.size() == 1);
        Assert.assertEquals(lastIteration,partIterations.get(0));
        //## END VERIFICATION

        //------------------------ Test : PartRevision not checked out and lastCheckedInIteration is null ------------------------
        //## BEGIN CONFIGURATION
        when(partRevision.getLastCheckedInIteration()).thenReturn(null);
        //## END CONFIGURATION

        partIterations = updatePartIterationPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.size() == 1);
        Assert.assertEquals(lastIteration,partIterations.get(0));
        //## END VERIFICATION

        //******* I - Check filter with a List<PartLink> as parameter

        //------------------------ Test : No substitutes links------------------------
        List<PartLink> partLinks =  updatePartIterationPSFilter.filter(Collections.singletonList(partLink));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertEquals(1,partLinks.size());
        Assert.assertEquals(partLink,partLinks.get(0));
        //## END VERIFICATION

        //------------------------ Test : With substitutes links------------------------
        //## BEGIN CONFIGURATION
        when(partLink.getSubstitutes()).thenReturn(Collections.singletonList(partSubstituteLink));
        //## END CONFIGURATION

        partLinks =  updatePartIterationPSFilter.filter(Collections.singletonList(partLink));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertEquals(2,partLinks.size());
        Assert.assertEquals(partLink,partLinks.get(0));
        Assert.assertEquals(partSubstituteLink,partLinks.get(1));
        //## END VERIFICATION
    }

}