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

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IUserManagerLocal userManager;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public List<ListOfValues> findLOVFromWorkspace(String workspaceId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {

        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        LOVDAO lovDAO = new LOVDAO(new Locale(user.getLanguage()), em);
        return lovDAO.loadLOVList(workspaceId);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ListOfValues findLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        LOVDAO lovDAO = new LOVDAO(new Locale(user.getLanguage()), em);

        return lovDAO.loadLOV(lovKey);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void createLov(String workspaceId, String name, List<NameValuePair> nameValuePairList) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, ListOfValuesAlreadyExistsException, CreationException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(workspaceId);
        userManager.checkWorkspaceWriteAccess(workspaceId);
        Locale locale = new Locale(user.getLanguage());
        LOVDAO lovDAO = new LOVDAO(locale, em);

        if (name == null || name.trim().isEmpty()) {
            throw new CreationException("LOVNameEmptyException");
        }

        if (nameValuePairList == null || nameValuePairList.isEmpty()) {
            throw new CreationException("LOVPossibleValueException");
        }

        WorkspaceDAO workspaceDAO = new WorkspaceDAO(locale, em);
        Workspace workspace = workspaceDAO.loadWorkspace(workspaceId);

        ListOfValues lov = new ListOfValues(workspace, name);
        lov.setValues(nameValuePairList);

        lovDAO.createLOV(lov);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void deleteLov(ListOfValuesKey lovKey) throws ListOfValuesNotFoundException, UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, AccessRightException, EntityConstraintException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        userManager.checkWorkspaceWriteAccess(lovKey.getWorkspaceId());
        Locale locale = new Locale(user.getLanguage());
        LOVDAO lovDAO = new LOVDAO(locale, em);

        if (isLovUsedInDocumentMasterTemplate(lovKey)) {
            throw new EntityConstraintException(locale, "EntityConstraintException14");
        }

        if (isLovUsedInPartMasterTemplate(lovKey)) {
            throw new EntityConstraintException(locale, "EntityConstraintException15");
        }

        ListOfValues lov = lovDAO.loadLOV(lovKey);
        lovDAO.deleteLOV(lov);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ListOfValues updateLov(ListOfValuesKey lovKey, String name, String workspaceId, List<NameValuePair> nameValuePairList) throws ListOfValuesAlreadyExistsException, CreationException, ListOfValuesNotFoundException, UserNotFoundException, WorkspaceNotFoundException, UserNotActiveException, AccessRightException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(lovKey.getWorkspaceId());
        userManager.checkWorkspaceWriteAccess(lovKey.getWorkspaceId());
        LOVDAO lovDAO = new LOVDAO(new Locale(user.getLanguage()), em);

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
        DocumentMasterTemplateDAO documentMasterTemplateDAO = new DocumentMasterTemplateDAO(em);
        List<DocumentMasterTemplate> documentsUsingLOV = documentMasterTemplateDAO.findAllDocMTemplatesFromLOV(lovKey);
        return documentsUsingLOV != null && !documentsUsingLOV.isEmpty();
    }

    private boolean isLovUsedInPartMasterTemplate(ListOfValuesKey lovKey) {
        PartMasterTemplateDAO partMasterTemplateDAO = new PartMasterTemplateDAO(em);
        List<PartMasterTemplate> partsUsingLOV = partMasterTemplateDAO.findAllPartMTemplatesFromLOV(lovKey);
        return partsUsingLOV != null && !partsUsingLOV.isEmpty();
    }

    private boolean isLovUsedInPartIterationInstanceAttributeTemplates(ListOfValuesKey lovKey) {
        PartIterationDAO partIterationDAO = new PartIterationDAO(em);
        List<PartIteration> partsUsingLOV = partIterationDAO.findAllPartIterationFromLOV(lovKey);
        return partsUsingLOV != null && !partsUsingLOV.isEmpty();
    }

}
