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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IOrganizationManagerLocal;
import org.polarsys.eplmp.server.dao.AccountDAO;
import org.polarsys.eplmp.server.dao.OrganizationDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IOrganizationManagerLocal.class)
@Stateless(name = "OrganizationManagerBean")
public class OrganizationManagerBean implements IOrganizationManagerLocal {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private OrganizationDAO organizationDAO;

    @Inject
    private IContextManagerLocal contextManagerLocal;

    @Inject
    private IAccountManagerLocal accountManager;

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void updateOrganization(Organization pOrganization)
            throws AccountNotFoundException, OrganizationNotFoundException, AccessRightException {

        Organization oldOrganization = organizationDAO.loadOrganization(pOrganization.getName());

        if (accountManager.checkAdmin(oldOrganization) != null) {
            organizationDAO.updateOrganization(pOrganization);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization getOrganizationOfAccount(String pLogin) throws AccountNotFoundException, OrganizationNotFoundException {
        return organizationDAO.findOrganizationOfAccount(accountManager.getMyAccount());
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization getMyOrganization() throws AccountNotFoundException, OrganizationNotFoundException {
        return getOrganizationOfAccount(contextManagerLocal.getCallerPrincipalLogin());
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization createOrganization(String pName, String pDescription) throws AccountNotFoundException, OrganizationAlreadyExistsException, CreationException, NotAllowedException {
        Account me = accountManager.getMyAccount();

        if (organizationDAO.hasOrganization(me)) {
            throw new NotAllowedException("NotAllowedException11");
        } else {
            Organization organization = new Organization(pName, me, pDescription);
            organizationDAO.createOrganization(organization);
            organization.addMember(me);
            return organization;
        }
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public void deleteOrganization(String pName) throws OrganizationNotFoundException, AccessRightException, AccountNotFoundException {
        Organization organization = organizationDAO.loadOrganization(pName);

        if (accountManager.checkAdmin(organization) != null) {
            organizationDAO.deleteOrganization(organization);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void addAccountInOrganization(String pOrganizationName, String pLogin) throws OrganizationNotFoundException, AccountNotFoundException, NotAllowedException, AccessRightException {
        Organization organization = organizationDAO.loadOrganization(pOrganizationName);
        accountManager.checkAdmin(organization);

        Account accountToAdd = accountDAO.loadAccount(pLogin);
        Organization accountToAddOrg = organizationDAO.findOrganizationOfAccount(accountToAdd);
        if (accountToAddOrg != null) {
            throw new NotAllowedException("NotAllowedException12");
        } else {
            organization.addMember(accountToAdd);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void removeAccountsFromOrganization(String pOrganizationName, String[] pLogins) throws OrganizationNotFoundException, AccessRightException, AccountNotFoundException {
        Organization organization = organizationDAO.loadOrganization(pOrganizationName);

        accountManager.checkAdmin(organization);

        for (String login : pLogins) {
            Account accountToRemove = accountDAO.loadAccount(login);
            organization.removeMember(accountToRemove);
        }

    }

}
