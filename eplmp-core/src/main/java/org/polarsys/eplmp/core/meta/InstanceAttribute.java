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

package org.polarsys.eplmp.core.meta;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Base class for all instance attributes.
 * This class holds a value
 *
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since V1.0
 */
@Table(name = "INSTANCEATTRIBUTE")
@XmlSeeAlso({InstanceTextAttribute.class, InstanceLongTextAttribute.class, InstanceNumberAttribute.class, InstanceDateAttribute.class, InstanceBooleanAttribute.class, InstanceURLAttribute.class, InstanceListOfValuesAttribute.class})
@Inheritance()
@Entity
public abstract class InstanceAttribute implements Serializable, Cloneable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    protected String name = "";

    protected boolean mandatory;

    protected boolean locked;

    public InstanceAttribute() {
    }

    public InstanceAttribute(String pName, boolean pMandatory) {
        name = pName;
        mandatory = pMandatory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameWithoutWhiteSpace() {
        return this.name.replaceAll(" ", "_");
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof InstanceAttribute)) {
            return false;
        }
        InstanceAttribute attribute = (InstanceAttribute) pObj;
        return attribute.id == id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public InstanceAttribute clone() {
        try {
            return (InstanceAttribute) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract Object getValue();

    public abstract boolean setValue(Object pValue);

    public boolean isValueEquals(Object pValue) {
        Object value = getValue();
        return value != null && value.equals(pValue);
    }
}
