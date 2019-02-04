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

package org.polarsys.eplmp.server.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.server.rest.dto.AccountDTO;

public class AdminResourceTest {

    @InjectMocks
    AdminResource adminResource;

    @Mock
    private IAccountManagerLocal accountManager;

    @Mock
    private UserGroupMapping userGroupMapping;
    @Spy
    private Account account = new Account("test", "test", "test@test.com", "fr", null, "GMT");
    @Mock
    private AccountDTO accountDTO;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        adminResource.init();
    }

    @Test
    public void updateAccount() throws EntityNotFoundException, NotAllowedException {

        Mockito.when(accountManager.updateAccount(accountDTO.getLogin(), accountDTO.getName(), accountDTO.getEmail(),
                accountDTO.getLanguage(), accountDTO.getNewPassword(), accountDTO.getTimeZone())).thenReturn(account);
        Mockito.when(accountManager.getUserGroupMapping(accountDTO.getLogin())).thenReturn(userGroupMapping);

        Mockito.when(accountManager.getUserGroupMapping(accountDTO.getLogin()).getGroupName())
                .thenReturn(UserGroupMapping.ADMIN_ROLE_ID);

        AccountDTO accountDTOResult = adminResource.updateAccount(accountDTO);
        Assert.assertTrue(accountDTOResult.isAdmin());
    }
}
