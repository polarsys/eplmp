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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.document.DocumentMasterTemplate;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.ListOfValues;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;
import org.polarsys.eplmp.core.meta.NameValuePair;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMasterTemplate;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.ILOVManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.dao.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

/**
 * @author Lebeau Julien on 03/03/15.
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(ILOVManagerLocal.class)
@Stateless(name = "LOVManagerBean")
public class LOVManagerBean implements ILOVManagerLocal {

    @Inject
    private LOVDAO lovDAO;

    @Inject
    private DocumentMasterTemplateDAO documentMasterTemplateDAO;

    @Inject
    private PartIterationDAO partIterationDAO;

    @Inject
    private PartMasterTemplateDAO partMasterTemplateDAO;

    @Inject
    private WorkspaceDAO workspaceDAO;

    @Inject
    private IUserManagerLocal userManager;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<ListOfValues> findLOVFromWorkspace(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        userManager.checkWorkspaceReadAccess(workspaceId);
        return lovDAO.loadLOVList(workspaceId);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ListOfValues findLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        return lovDAO.loadLOV(user.getLocale(), lovKey);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void createLov(String workspaceId, String name, List<NameValuePair> nameValuePairList) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, ListOfValuesAlreadyExistsException, CreationException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        userManager.checkWorkspaceWriteAccess(workspaceId);

        if (name == null || name.trim().isEmpty()) {
            throw new CreationException("LOVNameEmptyException");
        }

        if (nameValuePairList == null || nameValuePairList.isEmpty()) {
            throw new CreationException("LOVPossibleValueException");
        }

        Locale userLocale = user.getLocale();
        Workspace workspace = workspaceDAO.loadWorkspace(userLocale, workspaceId);

        ListOfValues lov = new ListOfValues(workspace, name);
        lov.setValues(nameValuePairList);

        lovDAO.createLOV(userLocale, lov);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void deleteLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, EntityConstraintException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        userManager.checkWorkspaceWriteAccess(lovKey.getWorkspaceId());
        Locale userLocale = user.getLocale();

        if (isLovUsedInDocumentMasterTemplate(lovKey)) {
            throw new EntityConstraintException(userLocale, "EntityConstraintException14");
        }

        if (isLovUsedInPartMasterTemplate(lovKey)) {
            throw new EntityConstraintException(userLocale, "EntityConstraintException15");
        }

        ListOfValues lov = lovDAO.loadLOV(userLocale, lovKey);
        lovDAO.deleteLOV(lov);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ListOfValues updateLov(ListOfValuesKey lovKey, String name, String workspaceId, List<NameValuePair> nameValuePairList) throws ListOfValuesAlreadyExistsException, CreationException, ListOfValuesNotFoundException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {
        userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        userManager.checkWorkspaceWriteAccess(lovKey.getWorkspaceId());
        ListOfValues lovToUpdate = findLov(lovKey);
        lovToUpdate.setValues(nameValuePairList);
        return lovDAO.updateLOV(lovToUpdate);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public boolean isLOVDeletable(ListOfValuesKey lovKey) {
        return !isLovUsedInDocumentMasterTemplate(lovKey) && !isLovUsedInPartMasterTemplate(lovKey) && !isLovUsedInPartIterationInstanceAttributeTemplates(lovKey);
    }

    private boolean isLovUsedInDocumentMasterTemplate(ListOfValuesKey lovKey) {
        List<DocumentMasterTemplate> documentsUsingLOV = documentMasterTemplateDAO.findAllDocMTemplatesFromLOV(lovKey);
        return documentsUsingLOV != null && !documentsUsingLOV.isEmpty();
    }

    private boolean isLovUsedInPartMasterTemplate(ListOfValuesKey lovKey) {
        List<PartMasterTemplate> partsUsingLOV = partMasterTemplateDAO.findAllPartMTemplatesFromLOV(lovKey);
        return partsUsingLOV != null && !partsUsingLOV.isEmpty();
    }

    private boolean isLovUsedInPartIterationInstanceAttributeTemplates(ListOfValuesKey lovKey) {
        List<PartIteration> partsUsingLOV = partIterationDAO.findAllPartIterationFromLOV(lovKey);
        return partsUsingLOV != null && !partsUsingLOV.isEmpty();
    }

}
