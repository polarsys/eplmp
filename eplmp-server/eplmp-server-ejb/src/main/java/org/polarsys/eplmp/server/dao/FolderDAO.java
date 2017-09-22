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

import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.EntityConstraintException;
import org.polarsys.eplmp.core.exceptions.FolderAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.FolderNotFoundException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderDAO {
    
    private EntityManager em;
    private Locale mLocale;
    private static final Logger LOGGER = Logger.getLogger(FolderDAO.class.getName());
    
    public FolderDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale=pLocale;
    }
    
    public FolderDAO(EntityManager pEM) {
        em = pEM;
        mLocale=Locale.getDefault();
    }
    
    public Folder loadFolder(String pCompletePath) throws FolderNotFoundException {
        Folder folder = em.find(Folder.class,pCompletePath);
        if (folder == null) {
            throw new FolderNotFoundException(mLocale, pCompletePath);
        } else {
            return folder;
        }
    }
    
    public void createFolder(Folder pFolder) throws FolderAlreadyExistsException, CreationException{
        try{
            //the EntityExistsException is thrown only when flush occurs          
            em.persist(pFolder);
            em.flush();
        }catch(EntityExistsException pEEEx){
            LOGGER.log(Level.FINEST,null,pEEEx);
            throw new FolderAlreadyExistsException(mLocale, pFolder);
        }catch(PersistenceException pPEx){
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            LOGGER.log(Level.FINEST,null,pPEx);
            throw new CreationException(mLocale);
        }
    }
    
    public Folder[] getSubFolders(String pCompletePath){
        Folder[] folders;
        TypedQuery<Folder> query = em.createQuery("SELECT DISTINCT f FROM Folder f WHERE f.parentFolder.completePath = :completePath", Folder.class);
        query.setParameter("completePath",pCompletePath);
        List listFolders = query.getResultList();
        folders = new Folder[listFolders.size()];
        for(int i=0;i<listFolders.size();i++) {
            folders[i] = (Folder) listFolders.get(i);
        }
        
        return folders;
    }
    
    public Folder[] getSubFolders(Folder pFolder){
        return getSubFolders(pFolder.getCompletePath());
    }

    public void removeFolder(String pCompletePath) throws FolderNotFoundException, EntityConstraintException {
        Folder folder = em.find(Folder.class,pCompletePath);
        if(folder==null) {
            throw new FolderNotFoundException(mLocale, pCompletePath);
        }
        
        removeFolder(folder);
    }
    
    public void removeFolder(Folder pFolder) throws EntityConstraintException {
        Folder[] subFolders = getSubFolders(pFolder);
        for(Folder subFolder:subFolders) {
            removeFolder(subFolder);
        }
        
        em.remove(pFolder);
        //flush to insure the right delete order to avoid integrity constraint
        //violation on folder.
        em.flush();
    }

    public List<DocumentRevision> moveFolder(Folder pFolder, Folder pNewFolder) throws FolderAlreadyExistsException, CreationException{
        DocumentRevisionDAO docRDAO=new DocumentRevisionDAO(mLocale,em);
        List<DocumentRevision> allDocRs = new LinkedList<>();
        List<DocumentRevision> docRs = docRDAO.findDocRsByFolder(pFolder.getCompletePath());
        allDocRs.addAll(docRs);

        for(DocumentRevision docR:allDocRs){
            docR.setLocation(pNewFolder);
        }

        Folder[] subFolders = getSubFolders(pFolder);
        for(Folder subFolder:subFolders){
            Folder newSubFolder = new Folder(pNewFolder.getCompletePath(),subFolder.getShortName());
            createFolder(newSubFolder);
            allDocRs.addAll(moveFolder(subFolder, newSubFolder));
        }
        em.remove(pFolder);
        //flush to insure the right delete order to avoid integrity constraint
        //violation on folder.
        em.flush();

        return allDocRs;
    }

    public List<DocumentRevision> findDocumentRevisionsInFolder(Folder pFolder) {
        DocumentRevisionDAO docRDAO = new DocumentRevisionDAO(mLocale, em);
        List<DocumentRevision> allDocRs = new LinkedList<>();
        List<DocumentRevision> docRs = docRDAO.findDocRsByFolder(pFolder.getCompletePath());
        allDocRs.addAll(docRs);

        Folder[] subFolders = getSubFolders(pFolder);
        for (Folder subFolder : subFolders) {
            allDocRs.addAll(findDocumentRevisionsInFolder(subFolder));
        }

        return allDocRs;
    }

}
