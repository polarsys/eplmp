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

import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.meta.InstanceAttributeTemplate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * {@link InstanceAttributeTemplate} implementation that can instantiate attributes
 * which value is a part number.
 * 
 * @author Florent Garin
 * @version 2.5, 27/09/16
 * @since   V2.5
 */
@Table(name="PARTNUMBERIATTRIBUTETEMPLATE")
@Entity
public class PartNumberAttributeTemplate extends InstanceAttributeTemplate {


    public PartNumberAttributeTemplate() {
    }

    public PartNumberAttributeTemplate(String pName) {
        super(pName);
    }


    @Override
    public InstanceAttribute createInstanceAttribute() {
        InstancePartNumberAttribute attr = new InstancePartNumberAttribute();
        attr.setName(name);
        attr.setMandatory(mandatory);
        attr.setLocked(locked);

        return attr;
    }

    @Override
    public String toString() {
        return name + "-" + "PART_NUMBER";
    }
}
