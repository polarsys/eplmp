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
import java.util.ArrayList;
import java.util.List;

/**
 * {@link InstanceAttributeTemplate} implementation that can instantiate list of values based
 * attributes.
 * 
 * @author Florent Garin
 * @version 2.0, 02/03/15
 * @since   V2.0
 */
@Table(name="ILOVATTRIBUTETEMPLATE")
@Entity
public class ListOfValuesAttributeTemplate extends InstanceAttributeTemplate {


    @JoinColumns({
            @JoinColumn(name = "LOV_NAME", referencedColumnName = "NAME"),
            @JoinColumn(name = "LOV_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ListOfValues lov;

    public ListOfValuesAttributeTemplate() {
    }

    public ListOfValuesAttributeTemplate(String pName, ListOfValues pLov) {
        super(pName);
        lov=pLov;
    }

    public ListOfValues getLov() {
        return lov;
    }

    public void setLov(ListOfValues lov) {
        this.lov = lov;
    }

    public String getLovName(){
        return this.lov != null ? this.lov.getName():null;
    }

    @Override
    public InstanceAttribute createInstanceAttribute() {
        InstanceListOfValuesAttribute attr = new InstanceListOfValuesAttribute();
        List<NameValuePair> items = new ArrayList<>(lov.getValues());
        attr.setItems(items);
        attr.setName(name);
        attr.setMandatory(mandatory);
        attr.setLocked(locked);

        return attr;
    }



    @Override
    public String toString() {
        return name + "-" + lov;
    }
}
