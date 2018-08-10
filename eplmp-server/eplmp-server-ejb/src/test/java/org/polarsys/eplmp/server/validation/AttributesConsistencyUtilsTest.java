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

package org.polarsys.eplmp.server.validation;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.polarsys.eplmp.core.meta.DefaultAttributeTemplate;
import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.meta.InstanceAttributeTemplate;
import org.polarsys.eplmp.core.meta.InstanceTextAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Charles Fallourd
 * @version 2.5, 16/07/15
 */
@RunWith(Parameterized.class)
public class AttributesConsistencyUtilsTest extends TestCase {
    //TODO: use mockito for the test
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] parameters = new Object[4][4];
        List<InstanceAttribute> current = new ArrayList<>();
        List<InstanceAttribute> newAttrs = new ArrayList<>();
        InstanceAttribute attribute = new InstanceTextAttribute("name","value1",false);
        InstanceAttribute attribute1 = new InstanceTextAttribute("name","value2",true);
        attribute1.setLocked(true);
        InstanceAttribute attribute2 = new InstanceTextAttribute("test",null,false);
        current.add(attribute);
        current.add(attribute1);
        current.add(attribute2);
        newAttrs.add(attribute);
        newAttrs.add(attribute1);

        parameters[0][0] = current;
        parameters[0][1] = newAttrs;
        parameters[0][2] = false;
        parameters[0][3] = true;

        current = new ArrayList<>();
        newAttrs = new ArrayList<>();
        current.add(new InstanceTextAttribute("name", "value", true));
        newAttrs.add(new InstanceTextAttribute("name", "value", true));

        parameters[1][0] = current;
        parameters[1][1] = newAttrs;
        parameters[1][2] = false;
        //should fail, the attribute is not locked.
        parameters[1][3] = false;

        attribute = new InstanceTextAttribute("name","value",true);
        attribute.setLocked(true);
        attribute1 = new InstanceTextAttribute("name",null,true);
        attribute1.setLocked(true);
        current = new ArrayList<>();
        newAttrs = new ArrayList<>();
        current.add(attribute);
        newAttrs.add(attribute1);
        parameters[2][0] = current;
        parameters[2][1] = newAttrs;
        parameters[2][2] = false;
        //should fail, the attribute is mandatory but null value.
        parameters[2][3] = false;

        attribute = new InstanceTextAttribute("name","value",true);
        attribute.setLocked(true);
        attribute1 = new InstanceTextAttribute("name","",true);
        attribute1.setLocked(true);
        current = new ArrayList<>();
        newAttrs = new ArrayList<>();
        current.add(attribute);
        newAttrs.add(attribute1);
        parameters[3][0] = current;
        parameters[3][1] = newAttrs;
        parameters[3][2] = false;
        //should fail, the attribute is mandatory but empty value.
        parameters[3][3] = false;
        return Arrays.asList(parameters);
    }
    @Parameterized.Parameter
    public List<InstanceAttribute> currentAttrs;

    @Parameterized.Parameter(value = 1)
    public List<InstanceAttribute> pAttributes;

    @Parameterized.Parameter(value = 2)
    public boolean isAttributesLocked;

    @Parameterized.Parameter(value = 3)
    public boolean expect;

    @Test
    public void testHasValidChange() throws Exception {
        Assert.assertEquals("Wrong result from hasValidChange", expect, AttributesConsistencyUtils.hasValidChange(pAttributes, isAttributesLocked, currentAttrs));
    }

    @Test
    public void testIsTemplateAttributesValid() throws Exception {
        List<InstanceAttributeTemplate> instanceAttributeTemplates = new ArrayList<>();

        //bad attribute: can not be "not locked" and mandatory
        InstanceAttributeTemplate attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.BOOLEAN);
        attr.setLocked(false);
        attr.setMandatory(true);
        instanceAttributeTemplates.add(attr);

        attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.TEXT);
        attr.setLocked(true);
        attr.setMandatory(true);
        instanceAttributeTemplates.add(attr);

        //bad attribute: can not be "not locked" and mandatory
        attr = new DefaultAttributeTemplate("test1", DefaultAttributeTemplate.AttributeType.LONG_TEXT);
        attr.setLocked(false);
        attr.setMandatory(true);
        instanceAttributeTemplates.add(attr);

        //Should failed either attributesLocked or not.
        Assert.assertFalse(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates,false));
        Assert.assertFalse(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates,true));

        instanceAttributeTemplates.clear();
        attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.BOOLEAN);
        attr.setLocked(false);
        attr.setMandatory(false);
        instanceAttributeTemplates.add(attr);
        attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.TEXT);
        attr.setLocked(true);
        attr.setMandatory(true);
        instanceAttributeTemplates.add(attr);
        attr = new DefaultAttributeTemplate("test1", DefaultAttributeTemplate.AttributeType.LONG_TEXT);
        attr.setLocked(true);
        attr.setMandatory(false);
        instanceAttributeTemplates.add(attr);

        //Should fail if attributesLocked, some attributes are not locked.
        Assert.assertTrue(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates,false));
        Assert.assertFalse(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates,true));

        instanceAttributeTemplates.clear();
        attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.BOOLEAN);
        attr.setLocked(true);
        attr.setMandatory(false);
        instanceAttributeTemplates.add(attr);
        attr = new DefaultAttributeTemplate("test", DefaultAttributeTemplate.AttributeType.TEXT);
        attr.setLocked(true);
        attr.setMandatory(true);
        instanceAttributeTemplates.add(attr);
        attr = new DefaultAttributeTemplate("test1", DefaultAttributeTemplate.AttributeType.LONG_TEXT);
        attr.setLocked(true);
        attr.setMandatory(false);
        instanceAttributeTemplates.add(attr);

        //All attributes are locked and valid, should succeed for either attributesLocked true or false.
        Assert.assertTrue(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates, false));
        Assert.assertTrue(AttributesConsistencyUtils.isTemplateAttributesValid(instanceAttributeTemplates,true));
    }

}
