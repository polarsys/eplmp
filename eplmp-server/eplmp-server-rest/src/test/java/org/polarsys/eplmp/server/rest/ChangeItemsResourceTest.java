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

import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeItemsResourceTest {

    @InjectMocks
    private ChangeItemsResource changeItemsResource = new ChangeItemsResource();

    @Mock
    private ChangeIssuesResource issues;

    @Mock
    private ChangeRequestsResource requests;

    @Mock
    private ChangeOrdersResource orders;

    @Mock
    private MilestonesResource milestones;

    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    @Test
    public void issuesTest() {
        ChangeIssuesResource _issues = changeItemsResource.issues();
        Assert.assertEquals(issues, _issues);
    }

    @Test
    public void requestsTest() {
        ChangeRequestsResource _requests = changeItemsResource.requests();
        Assert.assertEquals(requests, _requests);
    }

    @Test
    public void ordersTest() {
        ChangeOrdersResource _orders = changeItemsResource.orders();
        Assert.assertEquals(orders, _orders);
    }

    @Test
    public void milestonesTest() {
        MilestonesResource _milestones = changeItemsResource.milestones();
        Assert.assertEquals(milestones, _milestones);
    }

}
