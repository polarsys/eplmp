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
import org.polarsys.eplmp.core.configuration.BaselinedPart;
import org.polarsys.eplmp.core.configuration.BaselinedPartKey;
import org.polarsys.eplmp.core.configuration.PartCollection;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartSubstituteLink;
import org.polarsys.eplmp.server.util.ProductUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ResolvedCollectionConfigSpecTest {

    private ResolvedCollectionConfigSpec r2CS;

    @Mock private PartCollection partCollection;
    @Mock private PartMaster partMaster;
    @Mock private BaselinedPart baselinedPart;
    @Mock private PartIteration partIteration;

    @Mock private PartLink link1,link2,link3;
    @Mock private PartSubstituteLink pSLink1,pSLink2,pSLink3;

    @Before
    public void setUp(){

        initMocks(this);
        r2CS = mock(ResolvedCollectionConfigSpec.class,CALLS_REAL_METHODS);
        when(partMaster.getWorkspaceId()).thenReturn(ProductUtil.WORKSPACE_ID);
        when(partMaster.getNumber()).thenReturn("PART-0001");
        when(partCollection.getId()).thenReturn(1);
        when(link1.getFullId()).thenReturn("subLink1");
        when(link2.getFullId()).thenReturn("subLink2");
        when(link3.getFullId()).thenReturn("subLink3");
        when(pSLink1.getFullId()).thenReturn("partSubLink1");
        when(pSLink2.getFullId()).thenReturn("partSubLink2");
        when(pSLink3.getFullId()).thenReturn("partSubLink3");
    }

    @Test
    public void filterPartIterationTest(){

        //----------------- No PartCollection -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"partCollection",null);
        //## END CONFIGURATION

        PartIteration result = r2CS.filterPartIteration(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNull(result);
        //## END VERIFICATION

        //----------------- BaselinedPart found -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"partCollection",partCollection);
        when(partCollection.getBaselinedPart(any(BaselinedPartKey.class))).thenReturn(baselinedPart);
        when(baselinedPart.getTargetPart()).thenReturn(partIteration);
        //## END CONFIGURATION

        result =  r2CS.filterPartIteration(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNotNull(result);
        Assert.assertEquals(partIteration,result);
        //## END VERIFICATION

        //----------------- No BaselinedPart found -----------------
        //## BEGIN CONFIGURATION
        when(baselinedPart.getTargetPart()).thenReturn(null);
        //## END CONFIGURATION

        result = r2CS.filterPartIteration(partMaster);

        //## BEGIN VERIFICATION
        Assert.assertNull(result);
        //## END VERIFICATION
    }

    @Test
    public void filterPartLink(){

        //----------------- Optional link and no optional usage links -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"optionalUsageLinks",new HashSet<>());
        when(link3.isOptional()).thenReturn(true);
        //## END CONFIGURATION
        PartLink result = r2CS.filterPartLink(Arrays.asList(link1,link2,link3));

        //## BEGIN VERIFICATION
        Assert.assertNull(result);
        //## END VERIFICATION

        //----------------- Optional link with substitues and substitutesUsageLinks -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"optionalUsageLinks",
                Collections.singleton("subLink1-subLink2-subLink3"));
        Whitebox.setInternalState(r2CS,"substitutesUsageLinks",
                Collections.singleton("subLink1-subLink2-partSubLink1"));

        when(link3.getSubstitutes()).thenReturn(Arrays.asList(pSLink1,pSLink2,pSLink3));
        //## END CONFIGURATION

        result = r2CS.filterPartLink(Arrays.asList(link1,link2,link3));

        //## BEGIN VERIFICATION
        Assert.assertEquals(pSLink1,result);//pSLink1 because we stop at the first substituteLink founded
        //## END VERIFICATION

        //----------------- Optional link with substitues and no substitutesUsageLinks -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"optionalUsageLinks",
                Collections.singleton("subLink1-subLink2-subLink3"));
        Whitebox.setInternalState(r2CS,"substitutesUsageLinks",new HashSet<>());

        when(link3.getSubstitutes()).thenReturn(Arrays.asList(pSLink1,pSLink2,pSLink3));
        //## END CONFIGURATION

        result = r2CS.filterPartLink(Arrays.asList(link1,link2,link3));

        //## BEGIN VERIFICATION
        Assert.assertEquals(link3,result);
        //## END VERIFICATION

        //----------------- Non Optional link with substitues and substitutesUsageLinks -----------------
        //## BEGIN CONFIGURATION

        when(link3.isOptional()).thenReturn(false);
        Whitebox.setInternalState(r2CS,"substitutesUsageLinks",
                Collections.singleton("subLink1-subLink2-partSubLink1"));
        when(link3.getSubstitutes()).thenReturn(Arrays.asList(pSLink1,pSLink2,pSLink3));
        //## END CONFIGURATION

        result = r2CS.filterPartLink(Arrays.asList(link1,link2,link3));

        //## BEGIN VERIFICATION
        Assert.assertEquals(pSLink1,result);//pSLink1 because we stop at the first substituteLink founded
        //## END VERIFICATION

        //----------------- No Optional link with substitues and no substitutesUsageLinks -----------------
        //## BEGIN CONFIGURATION
        Whitebox.setInternalState(r2CS,"substitutesUsageLinks",new HashSet<>());
        //## END CONFIGURATION

        result = r2CS.filterPartLink(Arrays.asList(link1,link2,link3));

        //## BEGIN VERIFICATION
        Assert.assertEquals(link3,result);
        //## END VERIFICATION
    }
}