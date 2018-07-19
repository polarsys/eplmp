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

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "BinaryResourceDAO")
public class BinaryResourceDAO {
    private static final Logger LOGGER = Logger.getLogger(BinaryResourceDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public BinaryResourceDAO() {
        mLocale = Locale.getDefault();
    }

    public void createBinaryResource(BinaryResource pBinaryResource) throws FileAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs    
            em.persist(pBinaryResource);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINER,null,pEEEx);
            throw new FileAlreadyExistsException(mLocale, pBinaryResource);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            LOGGER.log(Level.FINER,null,pPEx);
            throw new CreationException(mLocale);
        }
    }

    public void createBinaryResource(Locale pLocale, BinaryResource pBinaryResource) throws FileAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createBinaryResource(pBinaryResource);
    }

    public void removeBinaryResource(String pFullName) throws FileNotFoundException {
        BinaryResource file = loadBinaryResource(pFullName);
        em.remove(file);
    }

    public void removeBinaryResource(BinaryResource pBinaryResource) {
        em.remove(pBinaryResource);
        em.flush();
    }

    public BinaryResource loadBinaryResource(String pFullName) throws FileNotFoundException {
        BinaryResource file = em.find(BinaryResource.class, pFullName);
        if(null == file){
            throw new FileNotFoundException(mLocale,pFullName);
        }
        return file;
    }

    public BinaryResource loadBinaryResource(Locale pLocale, String pFullName) throws FileNotFoundException {
        mLocale = pLocale;
        return loadBinaryResource(pFullName);
    }

    public PartIteration getPartHolder(BinaryResource pBinaryResource) {
        TypedQuery<PartIteration> query;
        String fileType = pBinaryResource.getFileType();
        if(pBinaryResource instanceof Geometry){
            query = em.createQuery("SELECT p FROM PartIteration p WHERE :binaryResource MEMBER OF p.geometries", PartIteration.class);
        }else if("nativecad".equals(fileType)){
            query = em.createQuery("SELECT p FROM PartIteration p WHERE p.nativeCADFile = :binaryResource", PartIteration.class);
        }else{
            query = em.createQuery("SELECT p FROM PartIteration p WHERE :binaryResource MEMBER OF p.attachedFiles", PartIteration.class);
        }
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }
    
    public DocumentIteration getDocumentHolder(BinaryResource pBinaryResource) {
        TypedQuery<DocumentIteration> query = em.createQuery("SELECT d FROM DocumentIteration d WHERE :binaryResource MEMBER OF d.attachedFiles", DocumentIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }
    public ProductInstanceIteration getProductInstanceIterationHolder(BinaryResource pBinaryResource) {
        TypedQuery<ProductInstanceIteration> query = em.createQuery("SELECT d FROM ProductInstanceIteration d WHERE :binaryResource MEMBER OF d.attachedFiles", ProductInstanceIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }

    public PathDataIteration getPathDataHolder(BinaryResource pBinaryResource) {
        TypedQuery<PathDataIteration> query = em.createQuery("SELECT p FROM PathDataIteration p WHERE :binaryResource MEMBER OF p.attachedFiles", PathDataIteration.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }


    public DocumentMasterTemplate getDocumentTemplateHolder(BinaryResource pBinaryResource) {
        TypedQuery<DocumentMasterTemplate> query = em.createQuery("SELECT t FROM DocumentMasterTemplate t WHERE :binaryResource MEMBER OF t.attachedFiles", DocumentMasterTemplate.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }

    public PartMasterTemplate getPartTemplateHolder(BinaryResource pBinaryResource) {
        TypedQuery<PartMasterTemplate> query = em.createQuery("SELECT t FROM PartMasterTemplate t WHERE t.attachedFile = :binaryResource", PartMasterTemplate.class);
        try {
            return query.setParameter("binaryResource", pBinaryResource).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }

    public BinaryResource findNativeCadBinaryResourceInWorkspace(String workspaceId, String cadFileName) {
        TypedQuery<BinaryResource> query = em.createQuery("SELECT br FROM BinaryResource br WHERE br.fullName like :name", BinaryResource.class);
        try {
            return query.setParameter("name", workspaceId + "/parts/%/nativecad/" + cadFileName).getSingleResult();
        } catch (NoResultException pNREx) {
            LOGGER.log(Level.FINER,null,pNREx);
            return null;
        }
    }



    private boolean isNativeCADFile(String pFullName){
        String[] parts = pFullName.split("/");
        return parts.length==7 && "nativecad".equals(parts[5]);
    }

    private boolean isAttachedFile(String pFullName) {
        String[] parts = pFullName.split("/");
        return parts.length==7 && "attachedfiles".equals(parts[5]);
    }

}
