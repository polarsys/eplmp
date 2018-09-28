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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class LatestReleasedPSFilterTest {

    @InjectMocks
    private LatestReleasedPSFilter latestReleasedPSFilter = new LatestReleasedPSFilter(false);

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
        when(partMaster.getLastReleasedRevision()).thenReturn(partRevision);
        when(partRevision.getLastIteration()).thenReturn(partIteration);
        //## END CONFIGURATION

        List<PartIteration> result = latestReleasedPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.size() == 1);
        //## END VERIFICATION

        //------------------------ Test : Empty list when no iteration founded ------------------------
        //## BEGIN CONFIGURATION
        when(partMaster.getLastReleasedRevision()).thenReturn(null);
        //## END CONFIGURATION

        result = latestReleasedPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
        //## END VERIFICATION

        //******* II - Check filter with a List<PartLink> as parameter

        //------------------------ Test : Diverge false ( at least last PartLink of list parameter must be added ) ------------------------

        List<PartLink> partLinks = latestReleasedPSFilter.filter(Arrays.asList(partLink,partLink_2));

        //## BEGIN VERIFICATION
        Assert.assertFalse((boolean) Whitebox.getInternalState(latestReleasedPSFilter, "diverge"));
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() ==1 );
        Assert.assertTrue(partLink_2.equals(partLinks.get(0)));
        //## END VERIFICATION

        //------------------------ Test : Diverge true ( last PartLink of list parameter and all his substitutes must be added ) ------------------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(latestReleasedPSFilter,"diverge",true);
        when(partLink_2.getSubstitutes()).thenReturn(Collections.singletonList(partSubstituteLink));
        //## BEGIN CONFIGURATION

        partLinks = latestReleasedPSFilter.filter(Arrays.asList(partLink,partLink_2));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() == 2 );
        Assert.assertTrue(partLink_2.equals(partLinks.get(0)));
        Assert.assertTrue(partSubstituteLink.equals(partLinks.get(1)));
        //## END VERIFICATION
    }
}