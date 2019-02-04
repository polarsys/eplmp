/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.server.rest;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.MockitoAnnotations.initMocks;

public class TimeZoneResourceTest {

    @InjectMocks
    private TimeZoneResource timeZoneResource = new TimeZoneResource();


    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    @Test
    public void getTimeZonesTest() {
        List<String> timeZones = timeZoneResource.getTimeZones();
        Assert.assertFalse(timeZones.isEmpty());
        List<String> availableTimeZones = Arrays.asList(TimeZone.getAvailableIDs());
        Collection intersection = CollectionUtils.intersection(timeZones, availableTimeZones);
        Assert.assertEquals(timeZones.size(),intersection.size());
    }

}
