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
import org.mockito.MockitoAnnotations;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.meta.InstanceTextAttribute;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeDTO;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeType;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class AttributesResourceTest {

    @InjectMocks
    private AttributesResource attributesResource = new AttributesResource();

    @Mock
    private IProductManagerLocal productManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        attributesResource.init();
    }

    @Test
    public void getPartIterationsAttributesTest() throws ApplicationException {
        String workspaceId = "wks";

        List<InstanceAttribute> list = new ArrayList<>();
        InstanceAttribute attribute = new InstanceTextAttribute("name","value",false);
        list.add(attribute);
        list.add(null);
        Mockito.when(productManager.getPartIterationsInstanceAttributesInWorkspace(workspaceId))
                .thenReturn(list);

        Response res = attributesResource.getPartIterationsAttributes(workspaceId);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));

        ArrayList result = (ArrayList) entity;
        Assert.assertEquals(1, result.size());

        Object o = result.get(0);
        Assert.assertTrue(o.getClass().isAssignableFrom(InstanceAttributeDTO.class));
        InstanceAttributeDTO attr = (InstanceAttributeDTO) o;
        Assert.assertEquals(attribute.getName(), attr.getName());
        Assert.assertEquals(null, attr.getValue());
        Assert.assertEquals(false, attr.isLocked());
        Assert.assertEquals(false, attr.isMandatory());
        Assert.assertEquals(InstanceAttributeType.TEXT, attr.getType());

    }

    @Test
    public void getPathDataAttributesTest() throws ApplicationException{
        String workspaceId = "wks";

        List<InstanceAttribute> list = new ArrayList<>();
        InstanceAttribute attribute = new InstanceTextAttribute("name","value",false);
        list.add(attribute);
        list.add(null);
        Mockito.when(productManager.getPathDataInstanceAttributesInWorkspace(workspaceId))
                .thenReturn(list);

        Response res = attributesResource.getPathDataAttributes(workspaceId);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));

        ArrayList result = (ArrayList) entity;
        Assert.assertEquals(1, result.size());

        Object o = result.get(0);
        Assert.assertTrue(o.getClass().isAssignableFrom(InstanceAttributeDTO.class));
        InstanceAttributeDTO attr = (InstanceAttributeDTO) o;
        Assert.assertEquals(attribute.getName(), attr.getName());
        Assert.assertEquals(null, attr.getValue());
        Assert.assertEquals(false, attr.isLocked());
        Assert.assertEquals(false, attr.isMandatory());
        Assert.assertEquals(InstanceAttributeType.TEXT, attr.getType());
    }
}
