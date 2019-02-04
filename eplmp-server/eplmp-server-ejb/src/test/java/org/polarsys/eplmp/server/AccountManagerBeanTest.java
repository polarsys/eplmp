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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.server.dao.AccountDAO;
import java.util.Date;
import org.junit.Assert;
public class AccountManagerBeanTest {
    @InjectMocks
    private AccountManagerBean accountManager = new AccountManagerBean();
    @Mock
    private IContextManagerLocal contextManager;
    @Mock
    private AccountDAO accountDAO;
    @Mock
    private ConfigManager configManager;
    @Spy
    private Account account = new Account("login", "user", "test@docdoku.com", "en", new Date(), null);
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void updateAccountTest()throws AccountNotFoundException,NotAllowedException{
        Account accountClone =  account.clone();
        Mockito.when(accountDAO.loadAccount(contextManager.getCallerPrincipalLogin())).thenReturn(accountClone);
        Account accountResult = accountManager.updateAccount(account.getName(), "test@test.com","fr", "test", "GMT");
        Assert.assertNotEquals(account.getEmail(), accountResult.getEmail());
        try {
            accountManager.updateAccount(account.getName(), account.getEmail(),"bidon", "test", "GMT");
            Assert.fail();
        }catch(NotAllowedException e){
            Assert.assertNotNull(e.getMessage());
        }
        try {
            accountManager.updateAccount(account.getName(), account.getEmail(),"fr", "test", "bidon");
            Assert.fail();
        }catch(NotAllowedException e){
            Assert.assertNotNull(e.getMessage());
        }
    }
    @Test
    public void updateAccountTestAdmin()throws AccountNotFoundException,NotAllowedException{
        Account accountClone =  account.clone();
        Mockito.when(accountManager.getAccount("login")).thenReturn(accountClone);
        Account accountResult = accountManager.updateAccount(account.getLogin(), account.getName(), "test@test.com", "fr", "test", "GMT");
        Assert.assertNotEquals(account.getEmail(), accountResult.getEmail());
        try {
            accountManager.updateAccount(account.getLogin(), account.getName(), account.getEmail(), "bidon", "test", "GMT");
            Assert.fail();
        }catch(NotAllowedException e){
            Assert.assertNotNull(e.getMessage());
        }
        try {
            accountManager.updateAccount(account.getLogin(), account.getName(), account.getEmail(), "fr", "test", "bidon");
            Assert.fail();
        }catch(NotAllowedException e){
            Assert.assertNotNull(e.getMessage());
        }
    }
}