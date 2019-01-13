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

package org.polarsys.eplmp.server.products;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.ProductBaseline;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ConfigurationItem;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PathToPathLink;
import org.polarsys.eplmp.server.ProductManagerBean;
import org.polarsys.eplmp.server.UserManagerBean;
import org.polarsys.eplmp.server.configuration.PSFilterVisitor;
import org.polarsys.eplmp.server.dao.*;
import org.polarsys.eplmp.server.util.BaselineRule;

import javax.ejb.SessionContext;
import javax.persistence.TypedQuery;
import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ProductBaselineManagerBeanTest {

    @InjectMocks
    private ProductBaselineManagerBean productBaselineService = new ProductBaselineManagerBean();
    @Mock
    private SessionContext ctx;
    @Mock
    private Principal principal;
    @Mock
    private UserManagerBean userManager;
    @Mock
    private ConfigurationItemDAO configurationItemDAO;
    @Mock
    private PartIterationDAO partIterationDAO;
    @Mock
    private ProductManagerBean productService;
    @Mock
    private PartCollectionDAO partCollectionDAO;
    @Mock
    private DocumentCollectionDAO documentCollectionDAO;
    @Mock
    private ProductBaselineDAO productBaselineDAO;
    @Mock
    private PathToPathLinkDAO pathToPathLinkDAO;

    @Mock
    private PSFilterVisitor psFilterVisitor;

    @Rule
    public BaselineRule baselineRuleNotReleased;
    @Rule
    public BaselineRule baselineRuleReleased;
    @Rule
    public BaselineRule baselineRuleLatest;
    @Mock
    private TypedQuery<PathToPathLink> mockedQuery;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
        Mockito.when(ctx.getCallerPrincipal()).thenReturn(principal);
        Mockito.when(principal.getName()).thenReturn("user1");
    }

    @Test
    public void createReleasedBaseline() throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, NotAllowedException, UserNotActiveException, PartIterationNotFoundException, PartRevisionNotReleasedException, EntityConstraintException, PartMasterNotFoundException, CreationException, BaselineNotFoundException, PathToPathLinkAlreadyExistsException, WorkspaceNotEnabledException {

        //Given
        baselineRuleReleased = new BaselineRule("myBaseline", ProductBaselineType.RELEASED, "description", "workspace01", "user1", "part01", "product01", true);
        doReturn(new User()).when(userManager).checkWorkspaceWriteAccess(Matchers.anyString());
        Mockito.when(userManager.checkWorkspaceWriteAccess(Matchers.anyString())).thenReturn(baselineRuleReleased.getUser());

        Mockito.when(productService.getRootPartUsageLink(Matchers.any())).thenReturn(baselineRuleReleased.getRootPartUsageLink());
        Mockito.when(mockedQuery.setParameter(Matchers.anyString(), Matchers.any())).thenReturn(mockedQuery);

        // Create Mock ConfigurationItem
        ConfigurationItem configurationItem = new ConfigurationItem(baselineRuleReleased.getUser(), baselineRuleReleased.getWorkspace(), baselineRuleReleased.getConfigurationItemKey().getId(), "description");
        configurationItem.setDesignItem(baselineRuleReleased.getPartMaster());
        Mockito.when(configurationItemDAO.loadConfigurationItem(baselineRuleReleased.getConfigurationItemKey())).thenReturn(configurationItem);

        //When
        ProductBaseline baseline = productBaselineService.createBaseline(baselineRuleReleased.getConfigurationItemKey(), baselineRuleReleased.getName(), baselineRuleReleased.getType(), baselineRuleReleased.getDescription(), new ArrayList<>(), baselineRuleReleased.getSubstituteLinks(), baselineRuleReleased.getOptionalUsageLinks(), null, null, null, false);

        //Then
        Assert.assertNotNull(baseline);
        Assert.assertEquals(baseline.getDescription(), baselineRuleReleased.getDescription());
        Assert.assertEquals(baseline.getType(), baselineRuleReleased.getType());
        Assert.assertEquals(baseline.getConfigurationItem().getWorkspaceId(), baselineRuleReleased.getWorkspace().getId());

    }

    @Test(expected = NotAllowedException.class)
    public void createReleasedBaselineUsingPartNotReleased() throws Exception{

        //Given
        baselineRuleNotReleased = new BaselineRule("myBaseline", ProductBaselineType.RELEASED, "description", "workspace01", "user1", "part01", "product01", false);

        doReturn(new User()).when(userManager).checkWorkspaceWriteAccess(Matchers.anyString());
        Mockito.when(userManager.checkWorkspaceWriteAccess(Matchers.anyString())).thenReturn(baselineRuleNotReleased.getUser());

        // Create Mock ConfigurationItem
        ConfigurationItem configurationItem = new ConfigurationItem(baselineRuleNotReleased.getUser(), baselineRuleNotReleased.getWorkspace(), baselineRuleNotReleased.getConfigurationItemKey().getId(), "description");
        configurationItem.setDesignItem(baselineRuleNotReleased.getPartMaster());
        Mockito.doThrow(NotAllowedException.class).when(psFilterVisitor).visit(any(), any(), any(PartMaster.class), anyInt(), any());
        Mockito.when(configurationItemDAO.loadConfigurationItem(baselineRuleNotReleased.getConfigurationItemKey())).thenReturn(configurationItem);

        //When
        productBaselineService.createBaseline(baselineRuleNotReleased.getConfigurationItemKey(), baselineRuleNotReleased.getName(), baselineRuleNotReleased.getType(), baselineRuleNotReleased.getDescription(),new ArrayList<>(), baselineRuleNotReleased.getSubstituteLinks(), baselineRuleNotReleased.getOptionalUsageLinks(), null, null, null, false);

    }

    @Test
    public void createLatestBaseline() throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, EntityConstraintException, UserNotActiveException, NotAllowedException, PartIterationNotFoundException, PartRevisionNotReleasedException, PartMasterNotFoundException, CreationException, BaselineNotFoundException, PathToPathLinkAlreadyExistsException, WorkspaceNotEnabledException {

        //Given
        baselineRuleLatest = new BaselineRule("myBaseline", ProductBaselineType.LATEST, "description", "workspace01", "user1", "part01", "product01", true);
        doReturn(new User()).when(userManager).checkWorkspaceWriteAccess(Matchers.anyString());
        Mockito.when(userManager.checkWorkspaceWriteAccess(Matchers.anyString())).thenReturn(baselineRuleLatest.getUser());

        Mockito.when(productService.getRootPartUsageLink(Matchers.any())).thenReturn(baselineRuleLatest.getRootPartUsageLink());
        Mockito.when(mockedQuery.setParameter(Matchers.anyString(), Matchers.any())).thenReturn(mockedQuery);

        // Create Mock ConfigurationItem
        ConfigurationItem configurationItem = new ConfigurationItem(baselineRuleLatest.getUser(), baselineRuleLatest.getWorkspace(), baselineRuleLatest.getConfigurationItemKey().getId(), "description");
        configurationItem.setDesignItem(baselineRuleLatest.getPartMaster());
        Mockito.when(configurationItemDAO.loadConfigurationItem(baselineRuleLatest.getConfigurationItemKey())).thenReturn(configurationItem);

        //When
        ProductBaseline baseline = productBaselineService.createBaseline(baselineRuleLatest.getConfigurationItemKey(), baselineRuleLatest.getName(), baselineRuleLatest.getType(), baselineRuleLatest.getDescription(), new ArrayList<>(), baselineRuleLatest.getSubstituteLinks(), baselineRuleLatest.getOptionalUsageLinks(), null, null, null, false);

        //Then
        Assert.assertNotNull(baseline);
        Assert.assertEquals(baseline.getDescription(), baselineRuleLatest.getDescription());
        Assert.assertEquals(baseline.getType(), baselineRuleLatest.getType());
        Assert.assertEquals(baseline.getConfigurationItem().getWorkspaceId(), baselineRuleLatest.getWorkspace().getId());

    }

    @Test
    public void createLatestBaselineWithCheckedPart() throws UserNotFoundException, AccessRightException, WorkspaceNotFoundException, ConfigurationItemNotFoundException, NotAllowedException, UserNotActiveException, PartIterationNotFoundException, PartRevisionNotReleasedException, EntityConstraintException, PartMasterNotFoundException, CreationException, BaselineNotFoundException, PathToPathLinkAlreadyExistsException, WorkspaceNotEnabledException {

        //Given
        baselineRuleReleased = new BaselineRule("myBaseline", ProductBaselineType.LATEST , "description", "workspace01", "user1", "part01", "product01", true, false);
        doReturn(new User()).when(userManager).checkWorkspaceWriteAccess(Matchers.anyString());
        Mockito.when(userManager.checkWorkspaceWriteAccess(Matchers.anyString())).thenReturn(baselineRuleReleased.getUser());
        Mockito.when(partIterationDAO.loadPartI(baselineRuleReleased.getPartMaster().getLastReleasedRevision().getIteration(1).getKey())).thenReturn(baselineRuleReleased.getPartMaster().getLastReleasedRevision().getIteration(1));
        Mockito.when(productService.getRootPartUsageLink(Matchers.any())).thenReturn(baselineRuleReleased.getRootPartUsageLink());
        Mockito.when(mockedQuery.setParameter(Matchers.anyString(), Matchers.any())).thenReturn(mockedQuery);

        // Create Mock ConfigurationItem
        ConfigurationItem configurationItem = new ConfigurationItem(baselineRuleReleased.getUser(), baselineRuleReleased.getWorkspace(), baselineRuleReleased.getConfigurationItemKey().getId(), "description");
        configurationItem.setDesignItem(baselineRuleReleased.getPartMaster());
        Mockito.when(configurationItemDAO.loadConfigurationItem(baselineRuleReleased.getConfigurationItemKey())).thenReturn(configurationItem);

        //When
        ProductBaseline baseline = productBaselineService.createBaseline(baselineRuleReleased.getConfigurationItemKey(), baselineRuleReleased.getName(), baselineRuleReleased.getType(), baselineRuleReleased.getDescription(), new ArrayList<>(), baselineRuleReleased.getSubstituteLinks(), baselineRuleReleased.getOptionalUsageLinks(), null, null, null, false);

        //Then
        Assert.assertNotNull(baseline);
        Assert.assertEquals(baselineRuleReleased.getDescription(), baseline.getDescription());
        Assert.assertEquals(ProductBaselineType.LATEST, baseline.getType());
        Assert.assertEquals(baselineRuleReleased.getWorkspace().getId(), baseline.getConfigurationItem().getWorkspaceId());

    }

}
