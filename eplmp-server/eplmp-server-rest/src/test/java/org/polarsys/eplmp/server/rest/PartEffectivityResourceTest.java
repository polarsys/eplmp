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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.services.IEffectivityManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.EffectivityDTO;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.mockito.MockitoAnnotations.initMocks;

public class PartEffectivityResourceTest {

    @InjectMocks
    private PartEffectivityResource partEffectivityResource = new PartEffectivityResource();

    @Mock
    private IEffectivityManagerLocal effectivityManager;

    @Mock
    private IProductManagerLocal productManager;

    private String workspaceId = "wks";
    private String partNumber = "P1";
    private String partVersion = "A";

    @Before
    public void setup() throws Exception {
        initMocks(this);
        partEffectivityResource.init();
    }

    @Test
    public void createEffectivityTest() throws ApplicationException {
        EffectivityDTO effectivity = new EffectivityDTO();

        // date

        effectivity.setTypeEffectivity(TypeEffectivity.DATEBASEDEFFECTIVITY);
        Mockito.when(effectivityManager.createDateBasedEffectivity(workspaceId, partNumber,
                partVersion, effectivity.getName(), effectivity.getDescription(), null, effectivity.getStartDate(),
                effectivity.getEndDate()))
                .thenReturn(new DateBasedEffectivity());

        EffectivityDTO result = partEffectivityResource.createEffectivity(effectivity, workspaceId, partNumber, partVersion);
        Assert.assertEquals(TypeEffectivity.DATEBASEDEFFECTIVITY, result.getTypeEffectivity());

        // SN

        effectivity.setTypeEffectivity(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY);
        Mockito.when(effectivityManager.createSerialNumberBasedEffectivity(
                workspaceId, partNumber, partVersion, effectivity.getName(), effectivity.getDescription(), null, effectivity.getStartNumber(),
                effectivity.getEndNumber()))
                .thenReturn(new SerialNumberBasedEffectivity());

        result = partEffectivityResource.createEffectivity(effectivity, workspaceId, partNumber, partVersion);
        Assert.assertEquals(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY, result.getTypeEffectivity());

        // lot
        effectivity.setTypeEffectivity(TypeEffectivity.LOTBASEDEFFECTIVITY);
        Mockito.when(effectivityManager.createLotBasedEffectivity(
                workspaceId, partNumber, partVersion, effectivity.getName(), effectivity.getDescription(), null, effectivity.getStartLotId(),
                effectivity.getEndLotId()))
                .thenReturn(new LotBasedEffectivity());

        result = partEffectivityResource.createEffectivity(effectivity, workspaceId, partNumber, partVersion);
        Assert.assertEquals(TypeEffectivity.LOTBASEDEFFECTIVITY, result.getTypeEffectivity());
    }

    @Test
    public void getEffectivitiesTest() throws ApplicationException {
        PartRevision part = new PartRevision();
        Set<Effectivity> list = new HashSet<>();
        SerialNumberBasedEffectivity serialEffectivity = new SerialNumberBasedEffectivity();
        DateBasedEffectivity dateEffectivity = new DateBasedEffectivity();
        LotBasedEffectivity lotEffectivity = new LotBasedEffectivity();

        list.add(serialEffectivity);
        list.add(dateEffectivity);
        list.add(lotEffectivity);

        part.setEffectivities(list);
        Mockito.when(productManager.getPartRevision(new PartRevisionKey(workspaceId, partNumber, partVersion)))
                .thenReturn(part);
        Response res = partEffectivityResource.getEffectivities(workspaceId, partNumber, partVersion);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList resultList = (ArrayList) entity;
        Object e1 = resultList.get(0);
        Object e2 = resultList.get(1);
        Object e3 = resultList.get(2);
        Assert.assertTrue(e1.getClass().isAssignableFrom(EffectivityDTO.class));
        Assert.assertTrue(e2.getClass().isAssignableFrom(EffectivityDTO.class));
        Assert.assertTrue(e3.getClass().isAssignableFrom(EffectivityDTO.class));

        List<TypeEffectivity> typeEffectivities = Arrays.asList(
                ((EffectivityDTO) e1).getTypeEffectivity(),
                ((EffectivityDTO) e2).getTypeEffectivity(),
                ((EffectivityDTO) e3).getTypeEffectivity()
        );

        Assert.assertEquals(list.size(), typeEffectivities.size());
        Assert.assertTrue(typeEffectivities.contains(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY));
        Assert.assertTrue(typeEffectivities.contains(TypeEffectivity.DATEBASEDEFFECTIVITY));
        Assert.assertTrue(typeEffectivities.contains(TypeEffectivity.LOTBASEDEFFECTIVITY));
    }

    @Test
    public void deleteEffectivityTest() throws ApplicationException {
        int effectivityId = 42;
        Mockito.doNothing().when(effectivityManager)
                .deleteEffectivity(workspaceId, partNumber, partVersion, effectivityId);
        Response response = partEffectivityResource.deleteEffectivity(workspaceId, partNumber, partVersion, effectivityId);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

}
