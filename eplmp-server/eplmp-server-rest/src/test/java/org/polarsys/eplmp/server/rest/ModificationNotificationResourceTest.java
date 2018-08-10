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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.exceptions.AccessRightException;
import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.exceptions.WorkspaceNotEnabledException;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.ModificationNotificationDTO;

import javax.ws.rs.core.Response;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Morgan Guimard
 */
public class ModificationNotificationResourceTest {

    @InjectMocks
    private ModificationNotificationResource modificationNotificationResource = new ModificationNotificationResource();

    @Mock
    private IProductManagerLocal productService;

    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    @Test
    public void acknowledgeNotificationTest() throws EntityNotFoundException, WorkspaceNotEnabledException, AccessRightException {
        ModificationNotificationDTO notificationDTO = new ModificationNotificationDTO();
        Mockito.doNothing().when(productService).updateModificationNotification(Matchers.anyString(), Matchers.anyInt(), Matchers.anyString());
        Response res = modificationNotificationResource.acknowledgeNotification("wks", 0, notificationDTO);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), res.getStatus());
    }

}
