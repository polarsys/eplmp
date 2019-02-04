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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.document.DocumentIterationKey;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;
import org.polarsys.eplmp.core.exceptions.DocumentRevisionNotFoundException;
import org.polarsys.eplmp.core.exceptions.FileNotFoundException;
import org.polarsys.eplmp.core.exceptions.PartRevisionNotFoundException;
import org.polarsys.eplmp.core.product.PartIterationKey;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.product.PartRevisionKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IPublicEntityManagerLocal;
import org.polarsys.eplmp.server.converters.OnDemandConverter;
import org.polarsys.eplmp.server.dao.BinaryResourceDAO;
import org.polarsys.eplmp.server.dao.DocumentRevisionDAO;
import org.polarsys.eplmp.server.dao.PartRevisionDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * EJB that trusts REST layer. Provide public documents, parts and binary resources services.
 *
 * @author Morgan Guimard
 **/

@DeclareRoles({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IPublicEntityManagerLocal.class)
@Stateless(name = "PublicEntityBean")
public class PublicEntityManagerBean implements IPublicEntityManagerLocal {

    @Inject
    private EntityManager em;

    @Inject
    private BinaryResourceDAO binaryResourceDAO;

    @Inject
    private DocumentRevisionDAO documentRevisionDAO;

    @Inject
    private PartRevisionDAO partRevisionDAO$;

    @Inject
    @Any
    private Instance<OnDemandConverter> documentResourceGetters;

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public PartRevision getPublicPartRevision(PartRevisionKey partRevisionKey) {
        PartRevision partRevision = em.find(PartRevision.class, partRevisionKey);
        if (partRevision != null && partRevision.isPublicShared()) {
            if (partRevision.isCheckedOut()) {
                em.detach(partRevision);
                partRevision.removeLastIteration();
            }
            return partRevision;
        }
        return null;
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public DocumentRevision getPublicDocumentRevision(DocumentRevisionKey documentRevisionKey) {
        DocumentRevision documentRevision = em.find(DocumentRevision.class, documentRevisionKey);
        if (documentRevision != null && documentRevision.isPublicShared()) {
            if (documentRevision.isCheckedOut()) {
                em.detach(documentRevision);
                documentRevision.removeLastIteration();
            }
            return documentRevision;
        }
        return null;
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public BinaryResource getPublicBinaryResourceForDocument(String fullName) throws FileNotFoundException {
        BinaryResource binaryResource = binaryResourceDAO.loadBinaryResource(fullName);
        String workspaceId = binaryResource.getWorkspaceId();
        String documentMasterId = binaryResource.getHolderId();
        String documentVersion = binaryResource.getHolderRevision();
        DocumentRevision documentRevision = getPublicDocumentRevision(new DocumentRevisionKey(workspaceId, documentMasterId, documentVersion));
        return documentRevision != null ? binaryResource : null;
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public BinaryResource getPublicBinaryResourceForPart(String fileName) throws FileNotFoundException {
        BinaryResource binaryResource = binaryResourceDAO.loadBinaryResource(fileName);
        String workspaceId = binaryResource.getWorkspaceId();
        String partNumber = binaryResource.getHolderId();
        String partVersion = binaryResource.getHolderRevision();
        PartRevision partRevision = getPublicPartRevision(new PartRevisionKey(workspaceId, partNumber, partVersion));
        return partRevision != null ? binaryResource : null;
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public BinaryResource getBinaryResourceForSharedEntity(String fileName) throws FileNotFoundException {
        return binaryResourceDAO.loadBinaryResource(fileName);
    }


    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public boolean canAccess(PartIterationKey partIKey) throws PartRevisionNotFoundException {
        PartRevision partRevision = partRevisionDAO$.loadPartR(partIKey.getPartRevision());
        return partRevision.isPublicShared() && partRevision.getLastCheckedInIteration().getIteration() >= partIKey.getIteration();
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public boolean canAccess(DocumentIterationKey docIKey) throws DocumentRevisionNotFoundException {
        DocumentRevision documentRevision = documentRevisionDAO.loadDocR(docIKey.getDocumentRevision());
        return documentRevision.isPublicShared() && documentRevision.getLastCheckedInIteration().getIteration() >= docIKey.getIteration();
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public BinaryResource getBinaryResourceForProductInstance(String fullName) throws FileNotFoundException {
        return binaryResourceDAO.loadBinaryResource(fullName);
    }

    @Override
    @RolesAllowed({UserGroupMapping.GUEST_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public BinaryResource getBinaryResourceForPathData(String fullName) throws FileNotFoundException {
        return binaryResourceDAO.loadBinaryResource(fullName);
    }

}
