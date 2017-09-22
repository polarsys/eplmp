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
package org.polarsys.eplmp.server.documents;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IDocumentBaselineManagerLocal;
import org.polarsys.eplmp.core.services.IDocumentManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.dao.DocumentBaselineDAO;
import org.polarsys.eplmp.server.dao.DocumentRevisionDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IDocumentBaselineManagerLocal.class)
@Stateless(name = "DocumentBaselineManagerBean")
public class DocumentBaselineManagerBean implements IDocumentBaselineManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IDocumentManagerLocal documentService;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public DocumentBaseline createBaseline(String workspaceId, String name, DocumentBaselineType type, String description, List<DocumentRevisionKey> documentRevisionKeys)
            throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, FolderNotFoundException, UserNotActiveException, DocumentRevisionNotFoundException, NotAllowedException, WorkspaceNotEnabledException {

        User user = userManager.checkWorkspaceWriteAccess(workspaceId);
        DocumentBaseline baseline = new DocumentBaseline(user, name, type, description);
        baseline.getDocumentCollection().setCreationDate(new Date());
        baseline.getDocumentCollection().setAuthor(user);
        new DocumentBaselineDAO(em, new Locale(user.getLanguage())).createBaseline(baseline);
        snapshotDocuments(baseline, workspaceId, documentRevisionKeys);
        if (baseline.getDocumentCollection().getBaselinedDocuments().isEmpty()) {
            throw new NotAllowedException(new Locale(user.getLanguage()), "NotAllowedException66");
        }
        return baseline;
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<DocumentBaseline> getBaselines(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        DocumentBaselineDAO documentBaselineDAO = new DocumentBaselineDAO(em, new Locale(user.getLanguage()));
        return documentBaselineDAO.findBaselines(workspaceId);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void deleteBaseline(String workspaceId, int baselineId) throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, BaselineNotFoundException, UserNotActiveException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceWriteAccess(workspaceId);
        DocumentBaseline documentBaseline = getBaselineLight(workspaceId, baselineId);
        new DocumentBaselineDAO(em, new Locale(user.getLanguage())).deleteBaseline(documentBaseline);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public DocumentBaseline getBaselineLight(String workspaceId, int baselineId) throws BaselineNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        return new DocumentBaselineDAO(em, new Locale(user.getLanguage())).loadBaseline(baselineId);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public DocumentCollection getACLFilteredDocumentCollection(String workspaceId, int baselineId) throws BaselineNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        DocumentBaseline documentBaseline = new DocumentBaselineDAO(em).loadBaseline(baselineId);
        DocumentCollection filteredDocumentCollection = new DocumentCollection();

        for (Map.Entry<BaselinedDocumentKey, BaselinedDocument> map : documentBaseline.getDocumentCollection().getBaselinedDocuments().entrySet()) {
            DocumentIteration documentIteration = map.getValue().getTargetDocument();
            DocumentRevision documentRevision = filterDocumentRevisionAccessRight(user, documentIteration.getDocumentRevision());
            if (documentRevision != null) {
                filteredDocumentCollection.addBaselinedDocument(documentIteration);
            }
        }

        return filteredDocumentCollection;
    }
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<BaselinedDocumentBinaryResourceCollection> getBinaryResourcesFromBaseline(String workspaceId, int baselineId)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException {

        userManager.checkWorkspaceReadAccess(workspaceId);
        List<BaselinedDocumentBinaryResourceCollection> result = new ArrayList<>();

        DocumentBaseline documentBaseline = getBaselineLight(workspaceId, baselineId);
        Set<Map.Entry<BaselinedDocumentKey, BaselinedDocument>> entries = documentBaseline.getDocumentCollection().getBaselinedDocuments().entrySet();

        for (Map.Entry<BaselinedDocumentKey, BaselinedDocument> entry : entries) {
            DocumentIteration documentIteration = entry.getValue().getTargetDocument();
            BaselinedDocumentBinaryResourceCollection collection = new BaselinedDocumentBinaryResourceCollection(documentBaseline.getName() + "/" + documentIteration.toString());
            Set<BinaryResource> attachedFiles = documentIteration.getAttachedFiles();

            if (!attachedFiles.isEmpty()) {
                collection.setAttachedFiles(attachedFiles);
            }

            if (!collection.hasNoFiles()) {
                result.add(collection);
            }
        }

        return result;
    }

    private DocumentRevision filterDocumentRevisionAccessRight(User user, DocumentRevision documentRevision){
        if(!user.isAdministrator()
                && (documentRevision.getACL()!=null)
                && !(documentRevision.getACL().hasReadAccess(user))) {
            return null;
        }
        return documentRevision;
    }

    private void snapshotDocuments(DocumentBaseline baseline, String workspaceId, List<DocumentRevisionKey> documentRevisionKeys) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, DocumentRevisionNotFoundException, FolderNotFoundException {
        for (DocumentRevisionKey revisionKey : documentRevisionKeys) {
            // Ignore already existing document
            if (baseline.hasBaselinedDocument(revisionKey)) {
                continue;
            }

            DocumentIteration documentIteration = filterBaselinedDocument(baseline.getType(), revisionKey);

            if (documentIteration != null) {
                baseline.addBaselinedDocument(documentIteration);
            }
        }
    }

    private DocumentIteration filterBaselinedDocument(DocumentBaselineType type, DocumentRevisionKey documentRevisionKey) throws DocumentRevisionNotFoundException {
        DocumentRevisionDAO documentRevisionDAO = new DocumentRevisionDAO(em);
        DocumentRevision documentRevision = documentRevisionDAO.loadDocR(documentRevisionKey);

        if (type.equals(DocumentBaselineType.RELEASED)) {
            return (documentRevision.isReleased() || documentRevision.isObsolete()) ? documentRevision.getLastIteration() : null;

        } else {
            if (documentRevision.isCheckedOut()) {
                em.detach(documentRevision);
                documentRevision.removeLastIteration();
            }
            return documentRevision.getLastIteration();
        }
    }
}
