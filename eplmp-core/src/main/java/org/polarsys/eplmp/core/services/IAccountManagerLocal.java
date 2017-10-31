/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;

import java.util.List;

/**
 * @author Elisabel Généreux
 */
public interface IAccountManagerLocal {

    Account getAccount(String pLogin) throws AccountNotFoundException;

    String getRole(String pLogin);

    Account createAccount(String pLogin, String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountAlreadyExistsException, CreationException;

    Account updateAccount(String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountNotFoundException;

    Account updateAccount(String pLogin, String pName, String pEmail, String pLanguage, String pPassword, String pTimeZone) throws AccountNotFoundException, NotAllowedException;

    Account getMyAccount() throws AccountNotFoundException;

    Account checkAdmin(Organization pOrganization) throws AccessRightException, AccountNotFoundException;

    Account checkAdmin(String pOrganizationName) throws AccessRightException, AccountNotFoundException, OrganizationNotFoundException;

    void setGCMAccount(String gcmId) throws AccountNotFoundException, GCMAccountAlreadyExistsException, CreationException;

    void deleteGCMAccount() throws AccountNotFoundException, GCMAccountNotFoundException;

    boolean isAccountEnabled(String pLogin) throws AccountNotFoundException;

    List<Account> getAccounts();

    Account enableAccount(String login, boolean enabled) throws AccountNotFoundException, NotAllowedException;

    Account authenticateAccount(String login, String password);

    UserGroupMapping getUserGroupMapping(String login);
}
