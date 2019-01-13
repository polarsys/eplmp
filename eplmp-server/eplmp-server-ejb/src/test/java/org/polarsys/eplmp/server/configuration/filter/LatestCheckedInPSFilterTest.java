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

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.product.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LatestCheckedInPSFilterTest {

    @InjectMocks
    private LatestCheckedInPSFilter latestCheckedInPSFilter = new LatestCheckedInPSFilter(false);

    @Mock
    private PartRevision partRevision;
    @Mock
    private PartIteration partIteration;
    @Mock
    private PartLink partLink;
    @Mock
    private PartLink partLink_2;
    @Mock
    PartSubstituteLink partSubstituteLink;

    @Spy
    private PartMaster partMaster = new PartMaster();

    @Before
    public void setUp() {

        initMocks(this);
    }

    @Test
    public void filter(){

        //******* I - Check filter with a PartMaster as parameter

        //------------------------ Test : Check if iteration was added ------------------------
        //## BEGIN CONFIGURATION
        when(partMaster.getLastRevision()).thenReturn(partRevision);
        when(partRevision.getLastCheckedInIteration()).thenReturn(partIteration);
        //## END CONFIGURATION

        List<PartIteration> partIterations = latestCheckedInPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.contains(partIteration));
        //## END VERIFICATION

        //******* II - Check filter with a List<PartLink> as parameter

        //------------------------ Test : Check behavior when diverge false ------------------------

        Assert.assertFalse((boolean)Whitebox.getInternalState(latestCheckedInPSFilter,"diverge"));
        List<PartLink> partLinks = latestCheckedInPSFilter.filter(Arrays.asList(partLink,partLink_2));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() == 1);
        Assert.assertTrue(partLinks.contains(partLink_2));
        //## END VERIFICATION

        //------------------------ Test : Check behavior when diverge true ------------------------

        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(latestCheckedInPSFilter,"diverge",true);
        when(partLink.getSubstitutes()).thenReturn(Collections.singletonList(partSubstituteLink));
        //## END CONFIGURATION

        partLinks = latestCheckedInPSFilter.filter(Collections.singletonList(partLink));

        //## BEGIN VERIFICATION
        Assert.assertTrue((boolean)Whitebox.getInternalState(latestCheckedInPSFilter,"diverge"));
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() == 2);
        Assert.assertTrue(partLinks.contains(partLink));
        Assert.assertTrue(partLinks.contains(partSubstituteLink));
        //## END VERIFICATION

        //------------------------ Test : partSubstitutes is empty ------------------------
        //## BEGIN CONFIGURATION
        when(partLink.getSubstitutes()).thenReturn(new ArrayList<PartSubstituteLink>());
        //## END CONFIGURATION

        partLinks = latestCheckedInPSFilter.filter(Collections.singletonList(partLink));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() == 1);
        Assert.assertTrue(partLinks.contains(partLink));
        //## END VERIFICATION RULE
    }
}