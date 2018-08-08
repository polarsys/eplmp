/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.server.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.exceptions.PlatformHealthException;
import org.polarsys.eplmp.core.services.IPlatformHealthManagerLocal;
import org.polarsys.eplmp.server.rest.dto.PlatformHealthDTO;

import static org.mockito.MockitoAnnotations.initMocks;

public class PlatformResourceTest {

    @InjectMocks
    private PlatformResource platformResource = new PlatformResource();

    @Mock
    private IPlatformHealthManagerLocal platformHealthManager;

    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    @Test
    public void getPlatformHealthStatusTest() throws ApplicationException {
        Mockito.doNothing().when(platformHealthManager).runHealthCheck();
        PlatformHealthDTO platformHealthStatus = platformResource.getPlatformHealthStatus();
        Assert.assertEquals("ok", platformHealthStatus.getStatus());

        Mockito.doThrow(new PlatformHealthException("ooops")).when(platformHealthManager).runHealthCheck();
        try {
            platformResource.getPlatformHealthStatus();
            Assert.fail("Should have thrown");
        } catch (PlatformHealthException e){
            Assert.assertEquals("ooops",e.getMessage());
        }

    }

}
