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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * This class holds the definition of a custom attribute of a document,
 * a part or any other entity.
 * 
 * @author Florent Garin
 * @version 1.1, 23/01/12
 * @since   V1.0
 */
@Table(name="INSTANCEATTRIBUTETEMPLATE")
@XmlSeeAlso({DefaultAttributeTemplate.class, ListOfValuesAttributeTemplate.class})
@Inheritance()
@Entity
public abstract class InstanceAttributeTemplate implements Serializable, Cloneable {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    protected int id;

    @Column(length=100)
    protected String name = "";

    protected boolean mandatory;

    protected boolean locked;


    public InstanceAttributeTemplate() {
    }

    public InstanceAttributeTemplate(String pName) {
        name = pName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public abstract InstanceAttribute createInstanceAttribute();

    @Override
    public InstanceAttributeTemplate clone() {
        try {
            return (InstanceAttributeTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InstanceAttributeTemplate that = (InstanceAttributeTemplate) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
