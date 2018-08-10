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

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.polarsys.eplmp.i18n.PropertiesLoader;

import java.util.Collection;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class LanguageResourceTest {

    @InjectMocks
    private LanguagesResource languagesResource = new LanguagesResource();


    @Before
    public void setup() throws Exception {
        initMocks(this);
    }

    @Test
    public void getLanguagesTest() {
        List<String> languages = languagesResource.getLanguages();
        List<String> supportedLanguages = PropertiesLoader.getSupportedLanguages();
        Assert.assertFalse(languages.isEmpty());
        Assert.assertEquals(supportedLanguages.size(),languages.size());
        Collection intersection = CollectionUtils.intersection(languages, supportedLanguages);
        Assert.assertEquals(supportedLanguages.size(),intersection.size());
    }

}
