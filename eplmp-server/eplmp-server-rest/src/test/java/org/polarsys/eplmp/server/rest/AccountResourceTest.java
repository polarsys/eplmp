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

package org.polarsys.eplmp.server.rest;

import org.jose4j.keys.HmacKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.ApplicationException;
import org.polarsys.eplmp.core.exceptions.EntityNotFoundException;
import org.polarsys.eplmp.core.exceptions.GCMAccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IContextManagerLocal;
import org.polarsys.eplmp.core.services.IOAuthManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.auth.AuthConfig;
import org.polarsys.eplmp.server.auth.jwt.JWTokenFactory;
import org.polarsys.eplmp.server.rest.dto.AccountDTO;
import org.polarsys.eplmp.server.rest.dto.GCMAccountDTO;
import org.polarsys.eplmp.server.rest.dto.WorkspaceDTO;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;

import static org.mockito.MockitoAnnotations.initMocks;

public class AccountResourceTest {

    @InjectMocks
    private AccountResource accountResource = new AccountResource();

    @Mock
    private EntityManager em;

    @Mock
    private IAccountManagerLocal accountManager;

    @Mock
    private IContextManagerLocal contextManager;

    @Mock
    private IOAuthManagerLocal authManager;

    @Mock
    private AuthConfig authConfig;

    @Mock
    private IUserManagerLocal userManager;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        accountResource.init();
    }

    @Test
    public void getAccountTest() throws ApplicationException {
        Account account = new Account("FooBar");
        Integer someProviderId = 42;

        Mockito.when(authManager.getProviderId(account)).thenReturn(someProviderId);
        Mockito.when(accountManager.getMyAccount()).thenReturn(account);

        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)).thenReturn(true);
        AccountDTO adminAccount = accountResource.getAccount();
        Assert.assertNotNull(adminAccount);
        Assert.assertTrue(adminAccount.isAdmin());

        Mockito.when(contextManager.isCallerInRole(UserGroupMapping.ADMIN_ROLE_ID)).thenReturn(false);
        AccountDTO userAccount = accountResource.getAccount();
        Assert.assertNotNull(userAccount);
        Assert.assertFalse(userAccount.isAdmin());

        Assert.assertEquals(someProviderId, userAccount.getProviderId());
    }

    @Test
    public void updateAccountTest() throws ApplicationException, UnsupportedEncodingException {

        Key key = new HmacKey("verySecretPhrase".getBytes("UTF-8"));
        UserGroupMapping groupMapping = new UserGroupMapping("FooBar", UserGroupMapping.REGULAR_USER_ROLE_ID);
        String authToken = JWTokenFactory.createAuthToken(key, groupMapping);
        Account account = new Account("FooBar");
        Mockito.when(authConfig.getJWTKey()).thenReturn(key);

        Mockito.when(accountManager.updateAccount(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString(), Matchers.anyString())).thenReturn(account);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setLogin(account.getLogin());
        Response res = accountResource.updateAccount(null, accountDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

        accountDTO.setPassword("");
        res = accountResource.updateAccount(null, accountDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

        res = accountResource.updateAccount("WithoutBearer " + authToken, accountDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

        res = accountResource.updateAccount("Bearer " + authToken, accountDTO);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        accountDTO.setPassword("SomePass");
        Mockito.when(accountManager.authenticateAccount(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(null);
        try {
            accountResource.updateAccount(null, accountDTO);
            Assert.fail("Should have thrown");
        } catch (NotAllowedException e) {
            Assert.assertNotNull(e.getMessage());
        }

        Mockito.when(accountManager.authenticateAccount(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(account);
        res = accountResource.updateAccount(null, accountDTO);

        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


    }

    @Test
    public void createAccountTest() throws ApplicationException, IOException, ServletException {
        Key key = new HmacKey("verySecretPhrase".getBytes("UTF-8"));
        Mockito.when(authConfig.getJWTKey()).thenReturn(key);

        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);
        HttpSession mockedSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockedRequest.getSession()).thenReturn(mockedSession);

        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();

        Mockito.when(accountManager.createAccount(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(),
                Matchers.anyString(), Matchers.anyString(), Matchers.anyString()))
                .thenReturn(account);

        Response res = accountResource.createAccount(mockedRequest, mockedResponse, accountDTO);
        Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), res.getStatus());

        account.setEnabled(true);
        res = accountResource.createAccount(mockedRequest, mockedResponse, accountDTO);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());


        Mockito.when(authConfig.isJwtEnabled()).thenReturn(true);
        res = accountResource.createAccount(mockedRequest, mockedResponse, accountDTO);
        Assert.assertNotNull(res.getHeaderString("jwt"));

        Mockito.when(authConfig.isJwtEnabled()).thenReturn(false);
        res = accountResource.createAccount(mockedRequest, mockedResponse, accountDTO);
        Assert.assertNull(res.getHeaderString("jwt"));


        Mockito.when(mockedRequest.authenticate(mockedResponse))
                .thenThrow(new IOException("Mocked exception"));
        res = accountResource.createAccount(mockedRequest, mockedResponse, accountDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

    }

    @Test
    public void getWorkspacesTest() {
        Workspace[] workspaces = new Workspace[]{new Workspace("wks")};
        Mockito.when(userManager.getWorkspacesWhereCallerIsActive())
                .thenReturn(workspaces);
        Response response = accountResource.getWorkspaces();
        Object entity = response.getEntity();
        Assert.assertTrue(entity.getClass().isAssignableFrom(ArrayList.class));
        ArrayList workspaceList = (ArrayList) entity;
        Object workspaceObject = workspaceList.get(0);
        Assert.assertTrue(workspaceObject.getClass().isAssignableFrom(WorkspaceDTO.class));
        WorkspaceDTO workspace = (WorkspaceDTO) workspaceObject;
        Assert.assertEquals(workspaces[0].getId(), workspace.getId());
    }

    @Test
    public void setGCMAccountTest() throws ApplicationException {
        GCMAccountDTO data = new GCMAccountDTO();
        Response response = accountResource.setGCMAccount(data);
        Mockito.doNothing().when(accountManager).setGCMAccount(data.getGcmId());
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteGCMAccountTest() throws EntityNotFoundException {

        Response response = accountResource.deleteGCMAccount();
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        Mockito.doThrow(new GCMAccountNotFoundException("foo")).when(accountManager).deleteGCMAccount();

        try {
            accountResource.deleteGCMAccount();
            Assert.fail("Should have thrown");
        } catch (EntityNotFoundException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

}
