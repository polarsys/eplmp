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
public class ReleasedPSFilterTest {

    @InjectMocks
    private ReleasedPSFilter releasedPSFilter = new ReleasedPSFilter(false);

    @Mock
    private PartRevision partRevision;
    @Mock
    private PartRevision partRevision_1;
    @Mock
    private PartRevision partRevision_2;
    @Mock
    private PartRevision partRevision_3;
    @Mock
    private PartRevision partRevision_4;
    @Mock
    private PartIteration partIteration;
    @Mock
    private PartIteration partIteration_1;
    @Mock
    private PartIteration partIteration_2;
    @Mock
    private PartIteration partIteration_3;
    @Mock
    private PartIteration partIteration_4;
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
        //------------------------ Test : Check if return all released parts------------------------
        //## BEGIN CONFIGURATION
        when(partRevision.isReleased()).thenReturn(true);
        when(partRevision_1.isReleased()).thenReturn(true);
        when(partRevision_2.isReleased()).thenReturn(true);
        when(partRevision_3.isReleased()).thenReturn(false);
        when(partRevision_4.isReleased()).thenReturn(false);

        Whitebox.setInternalState(partRevision,"partIterations",Collections.singletonList(partIteration));
        Whitebox.setInternalState(partRevision_1,"partIterations",Collections.singletonList(partIteration_1));
        Whitebox.setInternalState(partRevision_2,"partIterations",Collections.singletonList(partIteration_2));
        Whitebox.setInternalState(partRevision_3,"partIterations",Collections.singletonList(partIteration_3));
        Whitebox.setInternalState(partRevision_4,"partIterations",Collections.singletonList(partIteration_4));

        Whitebox.setInternalState(partMaster,"partRevisions",
                Arrays.asList(partRevision, partRevision_1, partRevision_2, partRevision_3, partRevision_4));
        //## END CONFIGURATION

        List<PartIteration> partIterations = releasedPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertFalse(partIterations.isEmpty());
        Assert.assertTrue(partIterations.size() == 3);
        //## END VERIFICATION

        //------------------------ Test : Check if return empty list if no released part------------------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(partMaster,"partRevisions", Arrays.asList(partRevision_3, partRevision_4));
        //## END CONFIGURATION

        partIterations = releasedPSFilter.filter(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partIterations);
        Assert.assertTrue(partIterations.isEmpty());
        //## END VERIFICATION

        //******* I - Check filter with a List<PartLink> as parameter

        //------------------------ Test : Diverge false ( at least last PartLink of list parameter must be added ) ------------------------

        List<PartLink> partLinks = releasedPSFilter.filter(Arrays.asList(partLink,partLink_2));

        //## BEGIN VERIFICATION
        Assert.assertFalse((boolean) Whitebox.getInternalState(releasedPSFilter, "diverge"));
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() ==1 );
        Assert.assertTrue(partLink_2.equals(partLinks.get(0)));
        //## END VERIFICATION

        //------------------------ Test : Diverge true ( last PartLink of list parameter and all his substitutes must be added ) ------------------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(releasedPSFilter,"diverge",true);
        when(partLink_2.getSubstitutes()).thenReturn(Collections.singletonList(partSubstituteLink));
        //## END CONFIGURATION

        partLinks = releasedPSFilter.filter(Arrays.asList(partLink,partLink_2));

        //## BEGIN VERIFICATION
        Assert.assertNotNull(partLinks);
        Assert.assertFalse(partLinks.isEmpty());
        Assert.assertTrue(partLinks.size() == 2 );
        Assert.assertTrue(partLink_2.equals(partLinks.get(0)));
        Assert.assertTrue(partSubstituteLink.equals(partLinks.get(1)));
        //## BEGIN VERIFICATION
    }
}