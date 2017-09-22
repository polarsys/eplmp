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

import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.gcm.GCMAccount;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.INotifierLocal;
import org.polarsys.eplmp.core.services.IPlatformOptionsManagerLocal;
import org.polarsys.eplmp.server.dao.AccountDAO;
import org.polarsys.eplmp.server.dao.GCMAccountDAO;
import org.polarsys.eplmp.server.dao.OrganizationDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IAccountManagerLocal.class)
@Stateless(name = "AccountManagerBean")
public class AccountManagerBean implements IAccountManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IContextManagerLocal contextManager;

    @Inject
    private INotifierLocal mailer;

    @Inject
    private IPlatformOptionsManagerLocal platformOptionsManager;

    @Inject
    private ConfigManager configManager;

    private static final Logger LOGGER = Logger.getLogger(AccountManagerBean.class.getName());

    public AccountManagerBean() {
    }

    @Override
    public Account authenticateAccount(String login, String password) {
        AccountDAO accountDAO = new AccountDAO(em);
        Account account = null;

        if(accountDAO.authenticate(login, password, configManager.getDigestAlgorithm())){

            try {
                account = getAccount(login);
            } catch (AccountNotFoundException e) {
               return null;
            }
        }

        return account;
    }

    @Override
    public UserGroupMapping getUserGroupMapping(String login) {
        return em.find(UserGroupMapping.class,login);
    }

    @Override
    public Account createAccount(String pLogin, String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountAlreadyExistsException, CreationException {
        OperationSecurityStrategy registrationStrategy = platformOptionsManager.getRegistrationStrategy();
        Date now = new Date();
        Account account = new Account(pLogin, pName, pEmail, pLanguage, now, pTimeZone);
        account.setEnabled(registrationStrategy.equals(OperationSecurityStrategy.NONE));
        new AccountDAO(new Locale(pLanguage), em).createAccount(account, pPassword, configManager.getDigestAlgorithm());
        mailer.sendCredential(account);
        return account;
    }

    @Override
    public Account getAccount(String pLogin) throws AccountNotFoundException {
        return new AccountDAO(em).loadAccount(pLogin);
    }

    public String getRole(String login) {
        UserGroupMapping userGroupMapping = em.find(UserGroupMapping.class, login);
        if (userGroupMapping == null) {
            return null;
        } else {
            return userGroupMapping.getGroupName();
        }
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Account updateAccount(String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountNotFoundException {
        AccountDAO accountDAO = new AccountDAO(new Locale(pLanguage), em);
        Account account = accountDAO.loadAccount(contextManager.getCallerPrincipalLogin());
        account.setName(pName);
        account.setEmail(pEmail);
        account.setLanguage(pLanguage);
        account.setTimeZone(pTimeZone);
        if (pPassword != null) {
            accountDAO.updateCredential(account.getLogin(), pPassword, configManager.getDigestAlgorithm());
        }
        return account;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Account checkAdmin(Organization pOrganization) throws AccessRightException, AccountNotFoundException {
        Account account = new AccountDAO(em).loadAccount(contextManager.getCallerPrincipalLogin());

        if (!contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID) && !pOrganization.getOwner().equals(account)) {
            throw new AccessRightException(new Locale(account.getLanguage()), account);
        }

        return account;
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Account checkAdmin(String pOrganizationName)
            throws AccessRightException, AccountNotFoundException, OrganizationNotFoundException {

        Account account = new AccountDAO(em).loadAccount(contextManager.getCallerPrincipalLogin());
        Organization organization = new OrganizationDAO(em).loadOrganization(pOrganizationName);

        if (!contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID) && !organization.getOwner().equals(account)) {
            throw new AccessRightException(new Locale(account.getLanguage()), account);
        }

        return account;
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public void setGCMAccount(String gcmId) throws AccountNotFoundException, GCMAccountAlreadyExistsException, CreationException {
        String callerLogin = contextManager.getCallerPrincipalLogin();
        Account account = getAccount(callerLogin);
        GCMAccountDAO gcmAccountDAO = new GCMAccountDAO(em);

        try {
            GCMAccount gcmAccount = gcmAccountDAO.loadGCMAccount(account);
            gcmAccount.setGcmId(gcmId);
        } catch (GCMAccountNotFoundException e) {
            gcmAccountDAO.createGCMAccount(new GCMAccount(account, gcmId));
        }

    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public void deleteGCMAccount() throws AccountNotFoundException, GCMAccountNotFoundException {
        String callerLogin = contextManager.getCallerPrincipalLogin();
        Account account = getAccount(callerLogin);
        GCMAccountDAO gcmAccountDAO = new GCMAccountDAO(new Locale(account.getLanguage()), em);
        GCMAccount gcmAccount = gcmAccountDAO.loadGCMAccount(account);
        gcmAccountDAO.deleteGCMAccount(gcmAccount);
    }

    @Override
    public boolean isAccountEnabled(String pLogin) throws AccountNotFoundException {
        Account account = getAccount(pLogin);
        return account.isEnabled();
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public List<Account> getAccounts() {
        AccountDAO accountDAO = new AccountDAO(em);
        return accountDAO.getAccounts();
    }

    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
    @Override
    public Account getMyAccount() throws AccountNotFoundException {
        return getAccount(contextManager.getCallerPrincipalName());
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public Account enableAccount(String login, boolean enabled) throws AccountNotFoundException, NotAllowedException {
        String callerPrincipalLogin = contextManager.getCallerPrincipalLogin();
        if (!callerPrincipalLogin.equals(login)) {
            Account account = getAccount(login);
            account.setEnabled(enabled);
            return account;
        } else {
            Account callerAccount = getMyAccount();
            throw new NotAllowedException(new Locale(callerAccount.getLanguage()), "NotAllowedException67");
        }
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public Account updateAccount(String pLogin, String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountNotFoundException, NotAllowedException {
        Account account = getMyAccount();
        AccountDAO accountDAO = new AccountDAO(new Locale(account.getLanguage()), em);
        Account otherAccount = getAccount(pLogin);
        otherAccount.setName(pName);
        otherAccount.setEmail(pEmail);
        otherAccount.setLanguage(pLanguage);
        otherAccount.setTimeZone(pTimeZone);
        if (pPassword != null) {
            accountDAO.updateCredential(otherAccount.getLogin(), pPassword, configManager.getDigestAlgorithm());
        }
        return otherAccount;
    }
}
