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
package org.polarsys.eplmp.core.meta;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A generic implementation of {@link InstanceAttributeTemplate} that can instantiate
 * simple primitive based attributes.
 * 
 * @author Florent Garin
 * @version 1.1, 23/01/12
 * @since   V1.0
 */
@Table(name="DEFAULTIATTRIBUTETEMPLATE")
@Entity
public class DefaultAttributeTemplate extends InstanceAttributeTemplate {


    private AttributeType attributeType;

    public enum AttributeType {
        TEXT, NUMBER, DATE, BOOLEAN, URL, LONG_TEXT
    }

    public DefaultAttributeTemplate() {
    }

    public DefaultAttributeTemplate(String pName, AttributeType pAttributeType) {
        super(pName);
        attributeType = pAttributeType;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public InstanceAttribute createInstanceAttribute() {
        InstanceAttribute attr = null;
        if(attributeType!=null){
            switch (attributeType) {
                case TEXT:
                    attr = new InstanceTextAttribute();
                    break;
                case NUMBER:
                    attr = new InstanceNumberAttribute();
                    break;
                case BOOLEAN:
                    attr = new InstanceBooleanAttribute();
                    break;
                case DATE:
                    attr = new InstanceDateAttribute();
                    break;
                case URL:
                    attr = new InstanceURLAttribute();
                    break;
                case LONG_TEXT:
                    attr = new InstanceLongTextAttribute();
                    break;
                default:
                    return null;
            }

            attr.setName(name);
            attr.setMandatory(mandatory);
            attr.setLocked(locked);
        }

        return attr;
    }

    @Override
    public String toString() {
        return name + "-" + attributeType;
    }
}
