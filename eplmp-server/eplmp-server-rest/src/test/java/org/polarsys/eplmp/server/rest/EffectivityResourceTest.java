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
import org.polarsys.eplmp.core.product.DateBasedEffectivity;
import org.polarsys.eplmp.core.product.LotBasedEffectivity;
import org.polarsys.eplmp.core.product.SerialNumberBasedEffectivity;
import org.polarsys.eplmp.core.product.TypeEffectivity;
import org.polarsys.eplmp.core.services.IEffectivityManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.server.rest.dto.EffectivityDTO;

import javax.ws.rs.core.Response;
import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;


/**
 * @author Morgan Guimard
 */
public class EffectivityResourceTest {

    @InjectMocks
    private EffectivityResource effectivityResource = new EffectivityResource();

    @Mock
    private IEffectivityManagerLocal effectivityManager;

    @Mock
    private IProductManagerLocal productManager;

    @Before
    public void init(){
        initMocks(this);
        effectivityResource.init();
    }

    @Test
    public void getEffectivityTest() throws ApplicationException {
        int effectivityId = 42;
        String workspaceId = "wks";

        Mockito.when(effectivityManager.getEffectivity(workspaceId, effectivityId))
                .thenReturn(new SerialNumberBasedEffectivity());
        EffectivityDTO effectivityDTO = effectivityResource.getEffectivity("wks", effectivityId);
        Assert.assertNotNull(effectivityDTO);
        Assert.assertEquals(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY, effectivityDTO.getTypeEffectivity());


        Mockito.when(effectivityManager.getEffectivity(workspaceId, effectivityId))
                .thenReturn(new DateBasedEffectivity());
        effectivityDTO = effectivityResource.getEffectivity("wks", effectivityId);
        Assert.assertNotNull(effectivityDTO);
        Assert.assertEquals(TypeEffectivity.DATEBASEDEFFECTIVITY, effectivityDTO.getTypeEffectivity());


        Mockito.when(effectivityManager.getEffectivity(workspaceId, effectivityId))
                .thenReturn(new LotBasedEffectivity());
        effectivityDTO = effectivityResource.getEffectivity("wks", effectivityId);
        Assert.assertNotNull(effectivityDTO);
        Assert.assertEquals(TypeEffectivity.LOTBASEDEFFECTIVITY, effectivityDTO.getTypeEffectivity());

    }

    @Test
    public void updateEffectivityTest() throws ApplicationException {
        String workspaceId = "wks";
        int effectivityId = 42;

        // serial number

        EffectivityDTO effectivityDTO = new EffectivityDTO();
        effectivityDTO.setId(effectivityId);
        effectivityDTO.setDescription("Ola ola");
        effectivityDTO.setTypeEffectivity(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY);
        effectivityDTO.setStartNumber("ABCDEF");
        effectivityDTO.setEndNumber("GHIJKL");

        Mockito.when(effectivityManager.updateSerialNumberBasedEffectivity(workspaceId, effectivityId,
                effectivityDTO.getName(), effectivityDTO.getDescription(),
                effectivityDTO.getStartNumber(), effectivityDTO.getEndNumber()))
                .thenReturn(new SerialNumberBasedEffectivity());

        Response res = effectivityResource.updateEffectivity(workspaceId, effectivityId, effectivityDTO);
        Object entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(EffectivityDTO.class));
        EffectivityDTO result = (EffectivityDTO) entity;
        Assert.assertEquals(TypeEffectivity.SERIALNUMBERBASEDEFFECTIVITY, result.getTypeEffectivity());

        // date

        effectivityDTO.setTypeEffectivity(TypeEffectivity.DATEBASEDEFFECTIVITY);
        effectivityDTO.setStartDate(new Date());
        effectivityDTO.setEndDate(new Date());

        Mockito.when( effectivityManager.updateDateBasedEffectivity(workspaceId, effectivityId,
                effectivityDTO.getName(), effectivityDTO.getDescription(),
                effectivityDTO.getStartDate(), effectivityDTO.getEndDate()))
                .thenReturn(new DateBasedEffectivity());

        res = effectivityResource.updateEffectivity(workspaceId, effectivityId, effectivityDTO);
        entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(EffectivityDTO.class));
        result = (EffectivityDTO) entity;
        Assert.assertEquals(TypeEffectivity.DATEBASEDEFFECTIVITY, result.getTypeEffectivity());

        // lot

        effectivityDTO.setTypeEffectivity(TypeEffectivity.LOTBASEDEFFECTIVITY);
        effectivityDTO.setStartLotId("ABC");
        effectivityDTO.setEndLotId("DEF");

        Mockito.when(effectivityManager.updateLotBasedEffectivity(workspaceId, effectivityId,
                effectivityDTO.getName(), effectivityDTO.getDescription(),
                effectivityDTO.getStartLotId(), effectivityDTO.getEndLotId()))
                .thenReturn(new LotBasedEffectivity());

        res = effectivityResource.updateEffectivity(workspaceId, effectivityId, effectivityDTO);
        entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(EffectivityDTO.class));
        result = (EffectivityDTO) entity;
        Assert.assertEquals(TypeEffectivity.LOTBASEDEFFECTIVITY, result.getTypeEffectivity());

        // unset type

        effectivityDTO.setTypeEffectivity(null);
        Mockito.when( effectivityManager.updateEffectivity(workspaceId, effectivityId,
                effectivityDTO.getName(), effectivityDTO.getDescription()))
                .thenReturn(new LotBasedEffectivity());
        res = effectivityResource.updateEffectivity(workspaceId, effectivityId, effectivityDTO);
        entity = res.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(EffectivityDTO.class));
        result = (EffectivityDTO) entity;
        Assert.assertEquals(null, result.getTypeEffectivity());


    }
}
