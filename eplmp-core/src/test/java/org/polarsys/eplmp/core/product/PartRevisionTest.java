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

package org.polarsys.eplmp.core.product;

import org.polarsys.eplmp.core.common.User;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Fallourd
 * @version 2.5, 13/01/16
 */
@RunWith(MockitoJUnitRunner.class)
public class PartRevisionTest extends TestCase {

    private PartRevision partRevision;
    private User user;
    private List<PartIteration> partIterations;

    @Before
    public void setUp() {
        partRevision = new PartRevision();
        user = Mockito.spy(new User());
        partIterations = new ArrayList<>();
        partRevision.setAuthor(user);
        partRevision.setPartIterations(partIterations);
    }
    @Test
    public void testGetLastAccessibleIteration() throws Exception {
        PartIteration partIteration = Mockito.mock(PartIteration.class);
        partIterations.add(partIteration);

        //No checkedOut user, if any iteration present, should send back the last one.
        Assert.assertTrue(partRevision.getLastAccessibleIteration(new User()) != null);

        partRevision.setCheckOutUser(user);

        //The only iteration has been checked-out, should return null since it's not the same user
        Assert.assertTrue(partRevision.getLastAccessibleIteration(new User()) == null);

        //The user who checked-out the part can access it
        Assert.assertTrue(partRevision.getLastAccessibleIteration(user) != null);

        partIterations.add(Mockito.mock(PartIteration.class));
        //Any other user should have access to the previous iteration
        Assert.assertTrue(partRevision.getLastAccessibleIteration(new User()) == partIteration);
    }
}
