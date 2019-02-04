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
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IOAuthManagerLocal;
import org.polarsys.eplmp.server.auth.AuthConfig;
import org.polarsys.eplmp.server.rest.dto.AccountDTO;
import org.polarsys.eplmp.server.rest.dto.LoginRequestDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

public class AuthResourceTest {

    @InjectMocks
    AuthResource resource = new AuthResource();
    @Mock
    private IAccountManagerLocal accountManager;
    @Mock
    private IOAuthManagerLocal oAuthManager;
    @Mock
    private AuthConfig authConfig;

    @Mock
    private LoginRequestDTO loginDTO;

    @Mock
    private UserGroupMapping userGroupMapping;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Spy
    private Account account = new Account("login", "user", "test@docdoku.com", "en", new Date(), null);

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        resource.init();
    }

    @Test
    public void loginTest() throws EntityNotFoundException, IOException, ServletException {

        // TEST FOR ADMIN USER CONNECTION

        Mockito.when(loginDTO.getLogin()).thenReturn("login");
        Mockito.when(loginDTO.getPassword()).thenReturn("root");
        Mockito.when(accountManager.authenticateAccount(loginDTO.getLogin(), loginDTO.getPassword()))
        .thenReturn(account);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(oAuthManager.isProvidedAccount(account)).thenReturn(true);

        // wait for 403 status
        Response res = resource.login(request, response, loginDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

        // -----------------------------------------------------------------------------------

        Mockito.when(oAuthManager.isProvidedAccount(account)).thenReturn(false);
        Mockito.when(account.isEnabled()).thenReturn(false);
        Mockito.when(authConfig.isSessionEnabled()).thenReturn(true);

        // wait for 403 status
        res = resource.login(request, response, loginDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());

        // -----------------------------------------------------------------------------------

        Mockito.when(oAuthManager.isProvidedAccount(account)).thenReturn(false);
        Mockito.when(account.isEnabled()).thenReturn(true);
        Mockito.when(accountManager.getUserGroupMapping(loginDTO.getLogin())).thenReturn(userGroupMapping);
        Mockito.when(userGroupMapping.getGroupName()).thenReturn(UserGroupMapping.ADMIN_ROLE_ID);
        Mockito.when(authConfig.isSessionEnabled()).thenReturn(true);

        // -----------------------------------------------------------------------------------

        // wait for 200 status
        res = resource.login(request, response, loginDTO);
        Object entity = res.getEntity();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertTrue(entity.getClass().isAssignableFrom(AccountDTO.class));
        Assert.assertEquals(loginDTO.getLogin(), ((AccountDTO) entity).getLogin());
        Assert.assertTrue(((AccountDTO) entity).isAdmin());

        // TEST FOR REGULAR USER CONNECTION

        Mockito.when(userGroupMapping.getGroupName()).thenReturn(UserGroupMapping.REGULAR_USER_ROLE_ID);
        res = resource.login(request, response, loginDTO);
        entity = res.getEntity();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertTrue(entity.getClass().isAssignableFrom(AccountDTO.class));
        Assert.assertEquals(loginDTO.getLogin(), ((AccountDTO) entity).getLogin());
        Assert.assertFalse(((AccountDTO) entity).isAdmin());

        // TEST FOR GUEST USER CONNECTION

        Mockito.when(userGroupMapping.getGroupName()).thenReturn(UserGroupMapping.GUEST_ROLE_ID);
        res = resource.login(request, response, loginDTO);
        entity = res.getEntity();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertTrue(entity.getClass().isAssignableFrom(AccountDTO.class));
        Assert.assertEquals(loginDTO.getLogin(), ((AccountDTO) entity).getLogin());
        Assert.assertFalse(((AccountDTO) entity).isAdmin());

        // IOException RAISED
        Mockito.when(request.authenticate(response)).thenThrow(new IOException());

        // wait for 403 status
        res = resource.login(request, response, loginDTO);
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), res.getStatus());
    }
}
