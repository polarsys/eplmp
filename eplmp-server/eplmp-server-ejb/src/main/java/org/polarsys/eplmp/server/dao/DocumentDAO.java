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

import org.polarsys.eplmp.core.document.DocumentIteration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless(name = "DocumentDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DocumentDAO {

    @PersistenceContext
    private EntityManager em;

    public void updateDoc(DocumentIteration pDoc){
        em.merge(pDoc);
    }

    public void removeDoc(DocumentIteration pDoc){
        em.remove(pDoc);
    }
}
