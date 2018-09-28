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
import org.polarsys.eplmp.core.product.SerialNumberBasedEffectivity;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SerialNumberBasedEffectivityConfigSpecTest {

    private SerialNumberBasedEffectivityConfigSpec SNBECS;
    @Mock private ConfigurationItem configurationItem,tmp_CI;
    @Mock private SerialNumberBasedEffectivity SNBE;
    @Mock private ProductConfiguration configuration;

    @Before
    public void setUp() {

        SNBECS = mock(SerialNumberBasedEffectivityConfigSpec.class,CALLS_REAL_METHODS);
        initMocks(this);
        when(configurationItem.getId()).thenReturn("ITEM-001");
        when(tmp_CI.getId()).thenReturn("ITEM-002");
        Whitebox.setInternalState(SNBECS,"number","20");
        when(configuration.getConfigurationItem()).thenReturn(configurationItem);
    }

    @Test
    public void isEffectiveTest(){

        //--------------- TEST : Different Configuration item ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(SNBECS,"configurationItem",tmp_CI);
        when(SNBE.getConfigurationItem()).thenReturn(configurationItem);
        //#### END CONFIGURATION

        boolean result = SNBECS.isEffective(SNBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION

        //--------------- TEST : StartNumber less than number ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(SNBECS,"configurationItem",configurationItem);
        when(SNBE.getStartNumber()).thenReturn("30");
        //#### END CONFIGURATION

        result = SNBECS.isEffective(SNBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION

        //--------------- TEST : EndNumber greater than number ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(SNBECS,"configurationItem",configurationItem);
        when(SNBE.getStartNumber()).thenReturn("15");
        when(SNBE.getEndNumber()).thenReturn("15");
        //#### END CONFIGURATION

        result = SNBECS.isEffective(SNBE);

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION

        //--------------- TEST : Everything is right  ---------------
        //#### BEGIN CONFIGURATION
        Whitebox.setInternalState(SNBECS,"configurationItem",configurationItem);
        when(SNBE.getStartNumber()).thenReturn("15");
        when(SNBE.getEndNumber()).thenReturn("30");
        //#### END CONFIGURATION

        result = SNBECS.isEffective(SNBE);

        //#### BEGIN VERIFICATION
        Assert.assertTrue(result);
        //#### END VERIFICATION

        //--------------- TEST : Not serial based effectivity ---------------
        result = SNBECS.isEffective(new DateBasedEffectivity());

        //#### BEGIN VERIFICATION
        Assert.assertFalse(result);
        //#### END VERIFICATION
    }

    @Test
    public void test_constructor_field_from_parent(){

        //Check if configuration item was set correctly
        SerialNumberBasedEffectivityConfigSpec snb = new SerialNumberBasedEffectivityConfigSpec("A",configurationItem);
        Assert.assertNotNull(snb.getConfigurationItem());
        snb = new SerialNumberBasedEffectivityConfigSpec("A",configuration);
        Assert.assertNotNull(snb.getConfigurationItem());

        //Test getters and setters
        snb.setNumber("BX15ZX");
        Assert.assertEquals("BX15ZX",snb.getNumber());
    }
}