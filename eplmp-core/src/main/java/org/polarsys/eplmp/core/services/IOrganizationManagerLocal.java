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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.*;

/**
 *
 * @author Elisabel Généreux
 */
public interface IOrganizationManagerLocal {

    Organization getOrganizationOfAccount(String pLogin) throws AccountNotFoundException, OrganizationNotFoundException;
    Organization getMyOrganization() throws AccountNotFoundException, OrganizationNotFoundException;
    Organization createOrganization(String pName, String pDescription) throws OrganizationAlreadyExistsException, CreationException, NotAllowedException, AccountNotFoundException;
    void deleteOrganization(String pName) throws OrganizationNotFoundException, AccountNotFoundException, AccessRightException;
    void updateOrganization(Organization pOrganization) throws AccountNotFoundException, OrganizationNotFoundException, AccessRightException;
    void addAccountInOrganization(String pOrganizationName, String pLogin) throws OrganizationNotFoundException, AccountNotFoundException, NotAllowedException, AccessRightException;
    void removeAccountsFromOrganization(String pOrganizationName, String[] pLogins) throws OrganizationNotFoundException, AccessRightException, AccountNotFoundException;

}
