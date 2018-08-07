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

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentMasterTemplate;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.FileAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.product.Geometry;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMasterTemplate;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class BinaryResourceDAO {

    private static final Logger LOGGER = Logger.getLogger(BinaryResourceDAO.class.getName());

    @Inject
    private EntityManager em;

    public BinaryResourceDAO() {
    }

    public void createBinaryResource(BinaryResource pBinaryResource) throws FileAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pBinaryResource);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINER, null, pEEEx);
            throw new FileAlreadyExistsException(pBinaryResource);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            LOGGER.log(Level.FINER, null, pPEx);
            throw new CreationException("");
        }
    }

    public void removeBinaryResource(BinaryResource pBinaryResource) {
        em.remove(pBinaryResource);
        em.flush();
    }

    public BinaryResource loadBinaryResource(String pFullName) throws FileNotFoundException {
        BinaryResource file = em.find(BinaryResource.class, pFullName);
        if (null == file) {
            throw new FileNotFoundException(pFullName);
        }
        return file;
    }

    public boolean exists(String pFullName) {
        return em.find(BinaryResource.class, pFullName) != null;
    }

    public PartIteration getPartHolder(BinaryResource pBinaryResource) {
        TypedQuery<PartIteration> query;
        String fileType = pBinaryResource.getFileType();
        if (pBinaryResource instanceof Geometry) {
            query = em.createQuery("SELECT p FROM PartIteration p WHERE :binaryResource MEMBER OF p.geometries", PartIteration.class);
        } else if (PartIteration.NATIVE_CAD_SUBTYPE.equals(fileType)) {
            query = em.createQuery("SELECT p FROM PartIteration p WHERE p.nativeCADFile = :binaryResource", PartIteration.class);
        } else {
            query = em.createQuery("SELECT p FROM PartIteration p WHERE :binaryResource MEMBER OF p.attachedFiles", PartIteration.class);
        }
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

    public DocumentIteration getDocumentHolder(BinaryResource pBinaryResource) {
        TypedQuery<DocumentIteration> query = em.createQuery("SELECT d FROM DocumentIteration d WHERE :binaryResource MEMBER OF d.attachedFiles", DocumentIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

    public ProductInstanceIteration getProductInstanceIterationHolder(BinaryResource pBinaryResource) {
        TypedQuery<ProductInstanceIteration> query = em.createQuery("SELECT d FROM ProductInstanceIteration d WHERE :binaryResource MEMBER OF d.attachedFiles", ProductInstanceIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

    public PathDataIteration getPathDataHolder(BinaryResource pBinaryResource) {
        TypedQuery<PathDataIteration> query = em.createQuery("SELECT p FROM PathDataIteration p WHERE :binaryResource MEMBER OF p.attachedFiles", PathDataIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }


    public DocumentMasterTemplate getDocumentTemplateHolder(BinaryResource pBinaryResource) {
        TypedQuery<DocumentMasterTemplate> query = em.createQuery("SELECT t FROM DocumentMasterTemplate t WHERE :binaryResource MEMBER OF t.attachedFiles", DocumentMasterTemplate.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

    public PartMasterTemplate getPartTemplateHolder(BinaryResource pBinaryResource) {
        TypedQuery<PartMasterTemplate> query = em.createQuery("SELECT t FROM PartMasterTemplate t WHERE t.attachedFile = :binaryResource", PartMasterTemplate.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

    public BinaryResource findNativeCadBinaryResourceInWorkspace(String workspaceId, String cadFileName) {
        TypedQuery<BinaryResource> query = em.createQuery("SELECT br FROM BinaryResource br WHERE br.fullName like :name", BinaryResource.class);
        try {
            return query.setParameter("name", workspaceId + "/parts/%/nativecad/" + cadFileName).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER, null, pNREx);
            return null;
        }
    }

}
