/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
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
import org.polarsys.eplmp.core.configuration.ProductConfiguration;
import org.polarsys.eplmp.core.product.ConfigurationItem;
import org.polarsys.eplmp.core.product.DateBasedEffectivity;
import org.polarsys.eplmp.core.product.LotBasedEffectivity;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class LotBasedEffectivityConfigSpecTest {

    private LotBasedEffectivityConfigSpec LBECS;
    @Mock private ConfigurationItem configurationItem,tmp_CI;
    @Mock private ProductConfiguration configuration;
    @Mock private LotBasedEffectivity LBE;

    @Before
    public void setUp() {

        LBECS = mock(LotBasedEffectivityConfigSpec.class,CALLS_REAL_METHODS);
        initMocks(this);
        when(configurationItem.getId()).thenReturn("ITEM-001");
        when(tmp_CI.getId()).thenReturn("ITEM-002");
        Whitebox.setInternalState(LBECS, "lotId", "5");
        when(configuration.getConfigurationItem()).thenReturn(configurationItem);
    }

    @Test
    public void isEffectiveTest(){

        //--------------- TEST : Different Configuration item ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(LBECS,"configurationItem",tmp_CI);
        when(LBE.getConfigurationItem()).thenReturn(configurationItem);
        //#### END CONFIGURATION

        boolean result = LBECS.isEffective(LBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION

        //--------------- TEST : StartLotId less than lotId ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(LBECS,"configurationItem",configurationItem);
        when(LBE.getStartLotId()).thenReturn("15");
        //#### END CONFIGURATION

        result = LBECS.isEffective(LBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION


        //--------------- TEST : EndLotId greater than lotId ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(LBECS,"configurationItem",configurationItem);
        when(LBE.getStartLotId()).thenReturn("1");
        when(LBE.getEndLotId()).thenReturn("4");
        //#### END CONFIGURATION

        result = LBECS.isEffective(LBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION

        //--------------- TEST : Everything is right  ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(LBECS,"configurationItem",configurationItem);
        when(LBE.getStartLotId()).thenReturn("1");
        when(LBE.getEndLotId()).thenReturn("10");
        //#### END CONFIGURATION

        result = LBECS.isEffective(LBE);

        //#### BEGIN VERIFICATION
        Assert.assertTrue(result);
        //#### END VERIFICATION

        //--------------- TEST : Not serial based effectivity ---------------
        result = LBECS.isEffective(new DateBasedEffectivity());

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION
    }

    @Test
    public void test_constructor_field_from_parent(){

        //Check if configuration item was set correctly
        LotBasedEffectivityConfigSpec snb = new LotBasedEffectivityConfigSpec("A",configurationItem);
        Assert.assertNotNull(snb.getConfigurationItem());
        snb = new LotBasedEffectivityConfigSpec("A",configuration);
        Assert.assertNotNull(snb.getConfigurationItem());

        //Test getters and setters
        snb.setLotId("BX15ZX");
        Assert.assertEquals("BX15ZX",snb.getLotId());
    }

}