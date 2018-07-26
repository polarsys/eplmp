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

import org.polarsys.eplmp.core.change.ChangeIssue;
import org.polarsys.eplmp.core.change.ChangeItem;
import org.polarsys.eplmp.core.change.ChangeOrder;
import org.polarsys.eplmp.core.change.ChangeRequest;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.ChangeOrderNotFoundException;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.PartRevisionKey;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Stateless(name = "ChangeItemDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ChangeItemDAO {

    private static final String DOCUMENT_MASTER_ID = "documentMasterId";
    private static final String VERSION = "version";
    private static final String PART_MASTER_NUMBER = "partMasterNumber";
    private static final String FOLDER = "folder";
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ACLDAO aclDAO;

    @Inject
    private FolderDAO folderDAO;

    private Locale mLocale;

    private static final String WORKSPACE_ID = "workspaceId";

    public ChangeItemDAO() {
        mLocale = Locale.getDefault();
    }


    public List<ChangeIssue> findAllChangeIssues(String pWorkspaceId) {
        return em.createNamedQuery("ChangeIssue.findChangeIssuesByWorkspace", ChangeIssue.class)
                 .setParameter(WORKSPACE_ID, pWorkspaceId)
                 .getResultList();
    }
    public List<ChangeRequest> findAllChangeRequests(String pWorkspaceId) {
        return em.createNamedQuery("ChangeRequest.findChangeRequestsByWorkspace", ChangeRequest.class)
                 .setParameter(WORKSPACE_ID, pWorkspaceId)
                 .getResultList();
    }
    public List<ChangeOrder> findAllChangeOrders(String pWorkspaceId) {
        return em.createNamedQuery("ChangeOrder.findChangeOrdersByWorkspace", ChangeOrder.class)
                 .setParameter(WORKSPACE_ID, pWorkspaceId)
                 .getResultList();
    }
    
    public ChangeIssue loadChangeIssue(int pId) {
        return  em.find(ChangeIssue.class, pId);
    }

    public ChangeIssue loadChangeIssue(Locale pLocale, int pId) {
        mLocale = pLocale;
        return loadChangeIssue(pId);
    }

    public ChangeOrder loadChangeOrder(int pId) {
        return em.find(ChangeOrder.class, pId);
    }

    public ChangeOrder loadChangeOrder(Locale pLocale, int pId) {
        mLocale = pLocale;
        return loadChangeOrder(pId);
    }

    public ChangeRequest loadChangeRequest(int pId) {
        return em.find(ChangeRequest.class, pId);
    }

    public ChangeRequest loadChangeRequest(Locale pLocale, int pId) {
        mLocale = pLocale;
        return loadChangeRequest(pId);
    }

    public void createChangeItem(ChangeItem pChange) {
        if(pChange.getACL()!=null){
            aclDAO.createACL(pChange.getACL());
        }

        em.persist(pChange);
        em.flush();
    }
    public void deleteChangeItem(ChangeItem pChange) {
        em.remove(pChange);
        em.flush();
    }
    public ChangeItem removeTag(ChangeItem pChange, String tagName){
        Tag tagToRemove = new Tag(pChange.getWorkspace(), tagName);
        pChange.getTags().remove(tagToRemove);
        return pChange;
    }

    public List<ChangeIssue> findAllChangeIssuesWithReferenceLike(String pWorkspaceId, String reference, int maxResults) {
        return em.createNamedQuery("ChangeIssue.findByName",ChangeIssue.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId).setParameter("name", "%" + reference + "%").setMaxResults(maxResults).getResultList();
    }

    public List<ChangeRequest> findAllChangeRequestsWithReferenceLike(String pWorkspaceId, String reference, int maxResults) {
        return em.createNamedQuery("ChangeRequest.findByName",ChangeRequest.class)
                .setParameter(WORKSPACE_ID, pWorkspaceId).setParameter("name", "%" + reference + "%").setMaxResults(maxResults).getResultList();
    }

    public List<ChangeItem> findChangeItemByTag(String pWorkspaceId, Tag tag){
        List<ChangeItem> changeItems = new ArrayList<>();
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeIssue c WHERE :tag MEMBER OF c.tags AND c.workspace.id = :workspaceId ", ChangeIssue.class)
                .setParameter("tag", tag)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeRequest c WHERE :tag MEMBER OF c.tags AND c.workspace.id = :workspaceId ", ChangeRequest.class)
                .setParameter("tag", tag)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeOrder c WHERE :tag MEMBER OF c.tags AND c.workspace.id = :workspaceId ", ChangeOrder.class)
                .setParameter("tag", tag)
                .setParameter(WORKSPACE_ID, pWorkspaceId)
                .getResultList());
        return changeItems;
    }

    public List<ChangeItem> findChangeItemByDoc(DocumentRevisionKey documentRevisionKey){
        String workspaceId = documentRevisionKey.getDocumentMaster().getWorkspace();
        String id = documentRevisionKey.getDocumentMaster().getId();
        List<ChangeItem> changeItems = new ArrayList<>();
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeIssue c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.documentMaster.workspace.id = :workspaceId AND i.documentRevision.version = :version AND i.documentRevision.documentMasterId = :documentMasterId", ChangeIssue.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(DOCUMENT_MASTER_ID, id)
                .setParameter(VERSION, documentRevisionKey.getVersion())
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeRequest c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.documentMaster.workspace.id = :workspaceId AND i.documentRevision.version = :version AND i.documentRevision.documentMasterId = :documentMasterId", ChangeRequest.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(DOCUMENT_MASTER_ID, id)
                .setParameter(VERSION, documentRevisionKey.getVersion())
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeOrder c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.documentMaster.workspace.id = :workspaceId AND i.documentRevision.version = :version AND i.documentRevision.documentMasterId = :documentMasterId", ChangeOrder.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(DOCUMENT_MASTER_ID, id)
                .setParameter(VERSION, documentRevisionKey.getVersion())
                .getResultList());
        return changeItems;
    }

    public List<ChangeItem> findChangeItemByPart(PartRevisionKey partRevisionKey){
        String workspaceId = partRevisionKey.getPartMaster().getWorkspace();
        String id = partRevisionKey.getPartMasterNumber();
        List<ChangeItem> changeItems = new ArrayList<>();
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeIssue c , PartIteration i WHERE i member of c.affectedParts AND i.partRevision.partMaster.workspace.id = :workspaceId AND i.partRevision.version = :version AND i.partRevision.partMasterNumber = :partMasterNumber", ChangeIssue.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(PART_MASTER_NUMBER, id)
                .setParameter(VERSION, partRevisionKey.getVersion())
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeRequest c , PartIteration i WHERE i member of c.affectedParts AND i.partRevision.partMaster.workspace.id = :workspaceId AND i.partRevision.version = :version AND i.partRevision.partMasterNumber = :partMasterNumber", ChangeRequest.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(PART_MASTER_NUMBER, id)
                .setParameter(VERSION, partRevisionKey.getVersion())
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeOrder c , PartIteration i WHERE i member of c.affectedParts AND i.partRevision.partMaster.workspace.id = :workspaceId AND i.partRevision.version = :version AND i.partRevision.partMasterNumber = :partMasterNumber", ChangeOrder.class)
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter(PART_MASTER_NUMBER, id)
                .setParameter(VERSION, partRevisionKey.getVersion())
                .getResultList());
        return changeItems;
    }
    
    public boolean hasChangeItems(DocumentRevisionKey documentRevisionKey){
        return !findChangeItemByDoc(documentRevisionKey).isEmpty();
    }

    public boolean hasChangeItems(PartRevisionKey partRevisionKey){
        return !findChangeItemByPart(partRevisionKey).isEmpty();
    }


    public List<ChangeItem> findChangeItemByFolder(Folder folder){
        List<ChangeItem> changeItems = new ArrayList<>();
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeIssue c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.location = :folder", ChangeIssue.class)
                .setParameter(FOLDER, folder)
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeRequest c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.location = :folder", ChangeRequest.class)
                .setParameter(FOLDER, folder)
                .getResultList());
        changeItems.addAll(em.createQuery("SELECT c FROM ChangeOrder c , DocumentIteration i WHERE i member of c.affectedDocuments AND i.documentRevision.location = :folder", ChangeOrder.class)
                .setParameter(FOLDER, folder)
                .getResultList());
        return changeItems;
    }


    public boolean hasConstraintsInFolderHierarchy(Folder pFolder) {
        boolean hasConstraints = false;

        for(Folder subFolder : folderDAO.getSubFolders(pFolder)){
            hasConstraints |= hasConstraintsInFolderHierarchy(subFolder);
        }

        return hasConstraints || hasChangeItems(pFolder);

    }

    public boolean hasConstraintsInFolderHierarchy(Locale pLocale, Folder pFolder) {
        mLocale = pLocale;
        return hasConstraintsInFolderHierarchy(pFolder);
    }

    public boolean hasChangeItems(Folder pFolder) {
        return !findChangeItemByFolder(pFolder).isEmpty();
    }

    public boolean hasChangeRequestsLinked(ChangeIssue changeIssue) {
        return !findAllChangeRequestsByChangeIssue(changeIssue).isEmpty();
    }
    
    public boolean hasChangeOrdersLinked(ChangeRequest changeRequest) {
        return !findAllChangeOrdersByChangeRequest(changeRequest).isEmpty();
    }

    private List<ChangeRequest> findAllChangeRequestsByChangeIssue(ChangeIssue changeIssue) {
        return em.createNamedQuery("ChangeRequest.findByChangeIssue", ChangeRequest.class)
                .setParameter(WORKSPACE_ID, changeIssue.getWorkspaceId())
                .setParameter("changeIssue", changeIssue)
                .getResultList();
    }

    private List<ChangeOrder> findAllChangeOrdersByChangeRequest(ChangeRequest changeRequest) {
        return em.createNamedQuery("ChangeOrder.findByChangeRequest", ChangeOrder.class)
                .setParameter(WORKSPACE_ID, changeRequest.getWorkspaceId())
                .setParameter("changeRequest", changeRequest)
                .getResultList();
    }

}
