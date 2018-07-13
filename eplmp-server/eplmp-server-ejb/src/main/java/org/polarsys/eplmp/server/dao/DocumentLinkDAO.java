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

import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentLink;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.product.PartIteration;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "DocumentLinkDAO")
public class DocumentLinkDAO {

    public static final String DOCUMENT_REVISION = "documentRevision";
    @PersistenceContext
    private EntityManager em;

    public void removeLink(DocumentLink pLink){
        em.remove(pLink);
    }

    public void createLink(DocumentLink pLink){
        try{
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pLink);
            em.flush();
        }catch(EntityExistsException pEEEx){
            //already created
        }
    }

    public List<DocumentIteration> getInverseDocumentsLinks(DocumentRevision documentRevision){
        return em.createNamedQuery("DocumentLink.findInverseDocumentLinks",DocumentIteration.class)
                .setParameter(DOCUMENT_REVISION,documentRevision)
                .getResultList();
    }

    public List<PartIteration> getInversePartsLinks(DocumentRevision documentRevision){
        return em.createNamedQuery("DocumentLink.findInversePartLinks",PartIteration.class)
                .setParameter(DOCUMENT_REVISION,documentRevision)
                .getResultList();
    }
    public List<ProductInstanceIteration> getInverseProductInstanceIteration(DocumentRevision documentRevision){
        return em.createNamedQuery("DocumentLink.findProductInstanceIteration", ProductInstanceIteration.class)
                .setParameter(DOCUMENT_REVISION,documentRevision)
                .getResultList();
    }
    public List<PathDataIteration> getInversefindPathData(DocumentRevision documentRevision){
        return em.createNamedQuery("DocumentLink.findPathData", PathDataIteration.class)
                .setParameter(DOCUMENT_REVISION,documentRevision)
                .getResultList();
    }


}
