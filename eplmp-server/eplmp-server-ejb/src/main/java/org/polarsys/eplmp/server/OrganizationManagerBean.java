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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IOrganizationManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.dao.AccountDAO;
import org.polarsys.eplmp.server.dao.OrganizationDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;
import java.util.logging.Logger;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IOrganizationManagerLocal.class)
@Stateless(name = "OrganizationManagerBean")
public class OrganizationManagerBean implements IOrganizationManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IUserManagerLocal userManager;

    @Inject
    private IContextManagerLocal contextManagerLocal;

    @Inject
    private IAccountManagerLocal accountManager;

    private static final Logger LOGGER = Logger.getLogger(OrganizationManagerBean.class.getName());

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void updateOrganization(Organization pOrganization)
            throws AccountNotFoundException, OrganizationNotFoundException, AccessRightException {

        OrganizationDAO organizationDAO = new OrganizationDAO(em);
        Organization oldOrganization = organizationDAO.loadOrganization(pOrganization.getName());

        if (accountManager.checkAdmin(oldOrganization) != null) {
            organizationDAO.updateOrganization(pOrganization);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization getOrganizationOfAccount(String pLogin) throws AccountNotFoundException, OrganizationNotFoundException {
        Account account = accountManager.getMyAccount();
        Locale locale = new Locale(account.getLanguage());

        OrganizationDAO organizationDAO = new OrganizationDAO(locale, em);
        return organizationDAO.findOrganizationOfAccount(account);
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization getMyOrganization() throws AccountNotFoundException, OrganizationNotFoundException {
        String login = contextManagerLocal.getCallerPrincipalLogin();
        return getOrganizationOfAccount(login);
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Organization createOrganization(String pName, String pDescription) throws AccountNotFoundException, OrganizationAlreadyExistsException, CreationException, NotAllowedException {

        Account me = accountManager.getMyAccount();
        OrganizationDAO organizationDAO = new OrganizationDAO(new Locale(me.getLanguage()), em);

        try {
            // Will throw an exception if account already have an organization
            organizationDAO.findOrganizationOfAccount(me);
            throw new NotAllowedException(new Locale(me.getLanguage()), "NotAllowedException11");
        } catch (OrganizationNotFoundException e) {
            // No organization for account, allowed to creat it
            Organization organization = new Organization(pName, me, pDescription);
            organizationDAO.createOrganization(organization);
            organization.addMember(me);
            return organization;
        }
    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    public void deleteOrganization(String pName) throws OrganizationNotFoundException, AccessRightException, AccountNotFoundException {
        OrganizationDAO organizationDAO = new OrganizationDAO(em);
        Organization organization = organizationDAO.loadOrganization(pName);

        if (accountManager.checkAdmin(organization) != null) {
            organizationDAO.deleteOrganization(organization);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void addAccountInOrganization(String pOrganizationName, String pLogin) throws OrganizationNotFoundException, AccountNotFoundException, NotAllowedException, AccessRightException {
        OrganizationDAO organizationDAO = new OrganizationDAO(em);
        Organization organization = organizationDAO.loadOrganization(pOrganizationName);
        Account account = accountManager.checkAdmin(organization);
        Locale locale = new Locale(account.getLanguage());

        Account accountToAdd = new AccountDAO(locale, em).loadAccount(pLogin);
        Organization accountToAddOrg = organizationDAO.findOrganizationOfAccount(accountToAdd);
        if (accountToAddOrg != null) {
            throw new NotAllowedException(locale, "NotAllowedException12");
        } else {
            organization.addMember(accountToAdd);
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void removeAccountsFromOrganization(String pOrganizationName, String[] pLogins) throws OrganizationNotFoundException, AccessRightException, AccountNotFoundException {
        OrganizationDAO organizationDAO = new OrganizationDAO(em);
        Organization organization = organizationDAO.loadOrganization(pOrganizationName);

        Account account = accountManager.checkAdmin(organization);
        Locale locale = new Locale(account.getLanguage());

        AccountDAO accountDAO = new AccountDAO(locale, em);
        for (String login : pLogins) {
            Account accountToRemove = accountDAO.loadAccount(login);
            organization.removeMember(accountToRemove);
        }

    }

}
