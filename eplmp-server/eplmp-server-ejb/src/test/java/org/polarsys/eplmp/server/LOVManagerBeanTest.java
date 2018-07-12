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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.meta.NameValuePair;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.dao.LOVDAO;
import org.polarsys.eplmp.server.dao.WorkspaceDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author lebeaujulien on 12/03/15.
 */
public class LOVManagerBeanTest extends LOVManagerBean {

    private static final String WORKSPACE_ID="TestWorkspace";
    private static final String USER_LOGIN="User1";
    private static final String USER_NAME="UserName";
    private static final String USER_MAIL="UserMail@mail.com";
    private static final String USER_LANGUAGE="fr";

    @InjectMocks
    private LOVManagerBean lovManager;

    @Mock
    private LOVDAO lovDAO;
    @Mock
    private WorkspaceDAO workspaceDAO;
    @Mock
    private IUserManagerLocal userManager;

    private Workspace workspace ;
    private User user;

    @Before
    public void setup() {
        initMocks(this);
        Account account = new Account(USER_LOGIN, USER_NAME, USER_MAIL, USER_LANGUAGE, new Date(), null);
        workspace = new Workspace(WORKSPACE_ID, account, "pDescription", false);
        user = new User(workspace, new Account(USER_LOGIN, USER_LOGIN, USER_MAIL,USER_LANGUAGE, new Date(), null));
    }

    @Test
    public void creationNominal() throws Exception {
        Mockito.when(userManager.checkWorkspaceWriteAccess(workspace.getId())).thenReturn(user);
        Mockito.when(userManager.checkWorkspaceReadAccess(workspace.getId())).thenReturn(user);
        Mockito.when(workspaceDAO.loadWorkspace(user.getLocale(), workspace.getId())).thenReturn(workspace);

        List<NameValuePair> possibleValue = new ArrayList<>();
        possibleValue.add(new NameValuePair("first", "value"));

        lovManager.createLov(workspace.getId(), "NominalName", possibleValue);
    }

    @Test (expected = CreationException.class)
    public void creationNoName() throws Exception {
        Mockito.when(userManager.checkWorkspaceWriteAccess(workspace.getId())).thenReturn(user);
        Mockito.when(userManager.checkWorkspaceReadAccess(workspace.getId())).thenReturn(user);

        List<NameValuePair> possibleValue = new ArrayList<>();
        possibleValue.add(new NameValuePair("first", "value"));

        lovManager.createLov(workspace.getId(), null, possibleValue);
    }

    @Test (expected = CreationException.class)
    public void creationNoPossibleValue() throws Exception {
        Mockito.when(userManager.checkWorkspaceWriteAccess(workspace.getId())).thenReturn(user);
        Mockito.when(userManager.checkWorkspaceReadAccess(workspace.getId())).thenReturn(user);

        lovManager.createLov(workspace.getId(), "Name", null);
    }
}
