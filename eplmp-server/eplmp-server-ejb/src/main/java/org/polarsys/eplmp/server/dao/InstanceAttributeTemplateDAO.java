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

package org.polarsys.eplmp.server.dao;

import org.polarsys.eplmp.core.meta.InstanceAttributeTemplate;

import javax.persistence.EntityManager;



public class InstanceAttributeTemplateDAO {

    private EntityManager em;

    public InstanceAttributeTemplateDAO(EntityManager pEM) {
        em=pEM;
    }


    public void removeAttribute(InstanceAttributeTemplate pAttr){
        em.remove(pAttr);
    }
}
