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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.server.converters.CADConverter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class BeanLocatorTest {

    static Context ctx;

    @BeforeClass
    public static void setup() throws Exception {
        ctx = new InitialContext(new Hashtable<>(Collections.singletonMap(Context.INITIAL_CONTEXT_FACTORY,
                "org.osjava.sj.memory.MemoryContextFactory")));
        CADConverter converter1 = Mockito.mock(CADConverter.class);
        Mockito.when(converter1.canConvertToOBJ("format")).thenReturn(true);
        CADConverter converter2 = Mockito.mock(CADConverter.class);
        Mockito.when(converter2.canConvertToOBJ("format")).thenReturn(true);
        ctx.createSubcontext("java:global");
        ctx.createSubcontext("java:global/application");
        ctx.createSubcontext("java:global/application/module");
        ctx.bind("java:global/application/module/c1Bean!org.polarsys.eplmp.server.converters.CADConverter", converter1);
        ctx.bind("java:global/application/module/c1Bean", converter1);
        ctx.bind("java:global/application/module/c2Bean", converter2);
        ctx.bind("java:global/application/module/c2Bean!org.polarsys.eplmp.server.converters.CADConverter", converter2);
    }

    BeanLocator locator = new BeanLocator();

    @Test
    public void testSearch() throws Exception {
        List<CADConverter> converters = locator.search(CADConverter.class, ctx);

        Assert.assertEquals(2, converters.size());
        for (CADConverter c : converters) {
            Assert.assertTrue(c.canConvertToOBJ("format"));
        }
    }
}
