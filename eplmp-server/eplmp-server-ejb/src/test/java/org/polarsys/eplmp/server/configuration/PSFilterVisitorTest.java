/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.internal.util.reflection.Whitebox;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.configuration.ProductConfigSpec;
import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.exceptions.EntityConstraintException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.exceptions.PartMasterNotFoundException;
import org.polarsys.eplmp.core.meta.RevisionStatus;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.server.configuration.spec.SerialNumberBasedEffectivityConfigSpec;
import org.polarsys.eplmp.server.dao.PartMasterDAO;
import org.polarsys.eplmp.server.util.ProductUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PSFilterVisitor.class)
public class PSFilterVisitorTest {

    @InjectMocks
    private PSFilterVisitor psFilterVisitor = new PSFilterVisitor();

    @Mock
    private PartMasterDAO partMasterDAO;
    @Mock
    private ProductStructureFilter filter;
    @Mock
    private PartMaster partMaster;
    @Mock
    User user;
    @Mock
    private PSFilterVisitorCallbacks callbacks;
    @Mock
    PartIteration partIteration;
    @Mock
    PartIteration partIteration_1;
    @Mock
    PartIteration partIteration_2;
    @Mock
    PartRevision partRevision;
    @Mock
    PartLink partLink;
    @Mock
    PartLink partLink_1;
    @Mock
    PartLink partLink_2;
    @Mock
    PartMaster component;
    @Mock
    PartMaster component_1;
    @Mock
    PartMaster component_2;
    @Mock
    PartUsageLink partUsageLink;

    @Before
    public void setUp(){

        initMocks(this);

        //Basic mocked methods
        when(partMaster.getAuthor()).thenReturn(user);
    }


    @Test
    public void visitTest() throws Exception {

        /**
         * Start the visitor with given part master
         * */

        //******* I - Check if PSFilterVisitorCallbacks's methods are called when call getComponentsRecursively's method.

        //-------------------------- TEST CALL TO : ON_UNRESOLVED_VERSION CALLBACK --------------------------

        //## BEGIN CONFIGURATION
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(filter.filter(partMaster)).thenReturn(new ArrayList<>());//need an empty array
        //## END CONFIGURATION

        psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, partMaster, -1, callbacks);
        verify(callbacks,times(1)).onUnresolvedVersion(anyObject());

        //-------------------------- TEST CALL TO : ON_INDETERMINATE_VERSION CALLBACK --------------------------

        //## BEGIN CONFIGURATION
        when(filter.filter(partMaster)).thenReturn(Arrays.asList(new PartIteration(), new PartIteration()));
        //## END CONFIGURATION

        psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, partMaster, -1, callbacks);
        verify(callbacks,times(1)).onIndeterminateVersion(anyObject(), anyListOf(PartIteration.class));

        //-------------------------- TEST CALL TO : ON_BRANCH_DISCOVERED CALLBACK --------------------------

        //At this level in runtime we simulated a structure with 3 nodes without no child nodes.
        //just test if those nodes was really discovered.
        //## BEGIN CONFIGURATION
        when(filter.filter(partMaster)).thenReturn(Collections.singletonList(new PartIteration()));
        //## END CONFIGURATION

        psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, partMaster, -1, callbacks);
        verify(callbacks,times(3)).onBranchDiscovered(anyListOf(PartLink.class), anyListOf(PartIteration.class));// 3 nodes must have been discovered

        //******* II - Check navigation between links.
        //We now add child to our nodes.

        reset(callbacks);//necessary for reset the number call 'callbacks' generate by the verify method

        //-------------------------- TEST : Recursive call -------------------------
        //## BEGIN CONFIGURATION
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(filter.filter(partMaster)).thenReturn(Collections.singletonList(partIteration));
        when(partIteration.getComponents()).thenReturn(Collections.singletonList(partUsageLink));
        when(filter.filter(anyListOf(PartLink.class))).thenReturn(Arrays.asList(partLink,partLink_1,partLink_2));
        when(partLink.getComponent()).thenReturn(component);
        when(partLink_1.getComponent()).thenReturn(component_1);
        when(partLink_2.getComponent()).thenReturn(component_2);
        when(component.getNumber()).thenReturn("PART-000");
        when(component_1.getNumber()).thenReturn("PART-001");
        when(component_2.getNumber()).thenReturn("PART-002");
        when(partMasterDAO.loadPartM(new PartMasterKey(ProductUtil.WORKSPACE_ID, "PART-000")))
                        .thenReturn(component);
        when(partMasterDAO.loadPartM(new PartMasterKey(ProductUtil.WORKSPACE_ID,"PART-001")))
                .thenReturn(component_1);
        when(partMasterDAO.loadPartM(new PartMasterKey(ProductUtil.WORKSPACE_ID,"PART-002")))
                .thenReturn(component_2);
        //## END CONFIGURATION

        Component result = psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, partMaster, -1, callbacks);

        verify(callbacks, times(0)).onUnresolvedPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        verify(callbacks,times(1)).onIndeterminatePath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        verify(callbacks,times(0)).onOptionalPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        //As we've child we must not call onBranchDiscovered again
        verify(callbacks, times(0)).onBranchDiscovered(anyListOf(PartLink.class), anyListOf(PartIteration.class));

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getComponents());
        Assert.assertFalse(result.getComponents().isEmpty());
        Assert.assertTrue(result.getComponents().size() == 3);

        reset(callbacks);//necessary for reset the number call 'callbacks' generate by the verify method

        //-------------------------- TEST : FAIL BECAUSE OF CYCLIC INTEGRITY -------------------------
        //## BEGIN CONFIGURATION
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(filter.filter(partMaster)).thenReturn(Collections.singletonList(partIteration));
        when(filter.filter(anyListOf(PartLink.class))).thenReturn(Collections.singletonList(partLink));
        when(partIteration.getComponents()).thenReturn(Collections.singletonList(partUsageLink));
        when(partLink.getComponent()).thenReturn(partMaster);
        when(partMaster.getNumber()).thenReturn("A");
        when(partMaster.getWorkspaceId()).thenReturn(ProductUtil.WORKSPACE_ID);
        when(partMasterDAO.loadPartM(anyObject())).thenReturn(partMaster);
        //## END CONFIGURATION
        try {

            psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, partMaster, -1, callbacks);
            Assert.fail();
        }catch (EntityConstraintException e) {

            verify(callbacks, times(0)).onUnresolvedPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
            verify(callbacks,times(0)).onIndeterminatePath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
            verify(callbacks,times(0)).onOptionalPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
            //As we've child we must not call onBranchDiscovered
            verify(callbacks, times(0)).onBranchDiscovered(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        }

        //Ensure mock are clean since theirs last use
        reset(callbacks);//necessary for reset the number call 'callbacks' generate by the verify method
        reset(partMaster);
        reset(partLink);
        reset(filter);
        reset(partIteration);
        reset(partMasterDAO);

        /**
         * Start the visitor with given path
         * */

        //Notice : We already tested 'callback' methods and cyclic integrity we just ensure than recursion is done
        //correctly with a given path ( List<PartLink> ).
        //-------------------------- TEST : RECURSIVE CALL WHEN A PATH GIVEN -------------------------

        //## STRUCTURE USED :

        //partMaster ( iteration )
        //  |
        //  |-> component_1 ( iteration_1 )
        //          |
        //          |->component_2 ( iteration_2 )

        //## END STRUCTURE

        //ADDITIONAL MOCKS
        PartUsageLink partUsageLink_1 = mock(PartUsageLink.class);
        //END ADDITIONAL MOCKS

        //## BEGIN CONFIGURATION
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(partLink.getComponent()).thenReturn(partMaster);
        when(filter.filter(partMaster)).thenReturn(Collections.singletonList(partIteration));
        when(partIteration.getComponents()).thenReturn(Collections.singletonList(partUsageLink));
        when(filter.filter(Arrays.asList(partLink,partUsageLink))).thenReturn(Collections.singletonList(partLink_1));
        when(partLink_1.getComponent()).thenReturn(component_1);
        when(component_1.getNumber()).thenReturn("COMP-001");
        when(partMasterDAO.loadPartM(new PartMasterKey(ProductUtil.WORKSPACE_ID, "COMP-001")))
                .thenReturn(component_1);

        when(partLink_1.getComponent()).thenReturn(component_1);
        when(filter.filter(component_1)).thenReturn(Collections.singletonList(partIteration_1));
        when(partIteration_1.getComponents()).thenReturn(Collections.singletonList(partUsageLink_1));
        when(filter.filter(Arrays.asList(partLink, partLink_1, partUsageLink_1))).thenReturn(Collections.singletonList(partLink_2));
        when(partLink_2.getComponent()).thenReturn(component_2);
        when(component_2.getNumber()).thenReturn("COMP-002");
        when(partMasterDAO.loadPartM(new PartMasterKey(ProductUtil.WORKSPACE_ID, "COMP-002")))
                .thenReturn(component_2);

        when(partLink_2.getComponent()).thenReturn(component_2);
        when(filter.filter(component_2)).thenReturn(Collections.singletonList(partIteration_2));
        when(partIteration_2.getComponents()).thenReturn(new ArrayList<PartUsageLink>());
        //## END CONFIGURATION

        result = psFilterVisitor.visit(ProductUtil.WORKSPACE_ID,filter,Collections.singletonList(partLink),-1,callbacks);

        verify(callbacks, times(0)).onUnresolvedPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        verify(callbacks,times(0)).onIndeterminatePath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        verify(callbacks,times(0)).onOptionalPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        //As we've child we must not call onBranchDiscovered again
        verify(callbacks, times(1)).onBranchDiscovered(anyListOf(PartLink.class), anyListOf(PartIteration.class));

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getComponents());
        Assert.assertFalse(result.getComponents().isEmpty());
        Assert.assertTrue(result.getComponents().size() == 1);
        Assert.assertTrue(component_1.getNumber().equals(result.getComponents().get(0).getPartMaster().getNumber()));
        Assert.assertTrue(result.getComponents().get(0).getComponents().size() == 1);
        Assert.assertTrue(component_2.getNumber().equals(result.getComponents().get(0).getComponents().get(0).getPartMaster().getNumber()));

        //-------------------------- TEST : MISSING IF CASES  -------------------------

        //**** ELIGIBLEPATH EMPTY AND NON OPTIONAL USAGELINK
        //## BEGIN CONFIGURATION
        reset(callbacks);//necessary for reset the number call 'callbacks' generate by the verify method
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(filter.filter(Arrays.asList(partLink,partUsageLink))).thenReturn(new ArrayList<PartLink>());
        when(partUsageLink.isOptional()).thenReturn(false);
        //## END CONFIGURATION

        psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, Collections.singletonList(partLink), -1, callbacks);

        verify(callbacks, times(1)).onUnresolvedPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));

        //**** ELIGIBLEPATH==1 AND OPTIONAL OPTIONAL LINK
        //## BEGIN CONFIGURATION
        reset(callbacks);//necessary for reset the number call 'callbacks' generate by the verify method
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(true);
        when(filter.filter(Arrays.asList(partLink,partUsageLink))).thenReturn(Collections.singletonList(partLink));
        when(partLink.isOptional()).thenReturn(true);
        //## END CONFIGURATION

        try {

            //Cause a null pointer because we not provide a component with number ( no interest for this case )
            psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, Collections.singletonList(partLink), -1, callbacks);
        }catch (NullPointerException e) {

            verify(callbacks, times(0)).onUnresolvedPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
            verify(callbacks, times(1)).onOptionalPath(anyListOf(PartLink.class), anyListOf(PartIteration.class));
        }

        //**** STOP CASES
        Whitebox.setInternalState(psFilterVisitor,"stopped",true);

        result =  psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, Collections.singletonList(partLink), -1, callbacks);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getComponents());
        Assert.assertTrue(result.getComponents().isEmpty());

        Whitebox.setInternalState(psFilterVisitor,"stopped",false);
        when(callbacks.onPathWalk(anyListOf(PartLink.class),anyListOf(PartMaster.class))).thenReturn(false);

        result =  psFilterVisitor.visit(ProductUtil.WORKSPACE_ID, filter, Collections.singletonList(partLink), -1, callbacks);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getComponents());
        Assert.assertTrue(result.getComponents().isEmpty());
    }


    @Test
    public void visitWithSNEffectivityFilter() throws EntityConstraintException, NotAllowedException, PartMasterNotFoundException {
        //Given
        String workspaceId = "workspace01";
        Workspace workspace = new Workspace(workspaceId);
        String login = "user1";
        User user = new User(workspace, new Account(login, login, login + "@docdoku.com", "en", new Date(), null));
        ConfigurationItem configurationItem = new ConfigurationItem(user, workspace, "product1", "description");
        ProductConfigSpec filter = new SerialNumberBasedEffectivityConfigSpec("2", configurationItem);

        // Create Mock ConfigurationItem with custom PartMaster
        PartMaster designItem = createPartMaster(configurationItem, "1", "A", true, ProductBaselineType.EFFECTIVE_SERIAL_NUMBER);
        //When
        visit(workspaceId, filter, designItem);

        //Then
        Assert.assertNotNull(filter);
        Assert.assertNotNull(filter.getRetainedPartIterations());
        Assert.assertEquals(filter.getRetainedPartIterations().size(), 2);
    }

    private void visit(String workspaceId, ProductConfigSpec filter, PartMaster designItem) throws PartMasterNotFoundException, EntityConstraintException, NotAllowedException {
        psFilterVisitor.visit(workspaceId, filter, designItem, -1, new PSFilterVisitorCallbacks() {
        });
    }

    /**
     * Create a PartMaster for effectivity tests
     *
     * @param configurationItem
     * @param pNumber
     * @param pVersion
     * @param productBaselineType
     * @return custom PartMaster
     */
    private PartMaster createPartMaster(ConfigurationItem configurationItem, String pNumber, String pVersion, boolean createPartLink, ProductBaselineType productBaselineType) throws PartMasterNotFoundException {
        User user = configurationItem.getAuthor();
        Workspace workspace = configurationItem.getWorkspace();
        PartMaster partMaster = new PartMaster(workspace, pNumber, user);
        partMaster.setPartRevisions(new ArrayList<PartRevision>() {{
            add(createPartRevision(configurationItem, partMaster, pNumber, pVersion, createPartLink, productBaselineType));
        }});
        return partMaster;
    }

    private PartRevision createPartRevision(ConfigurationItem configurationItem, PartMaster partMaster, String pNumber, String pVersion, boolean createPartLink, ProductBaselineType productBaselineType) throws PartMasterNotFoundException {
        PartRevision partRevision = new PartRevision(partMaster, pVersion, configurationItem.getAuthor());
        partRevision.setStatus(RevisionStatus.RELEASED);
        partRevision.setPartIterations(new ArrayList<PartIteration>() {{
            add(createPartIteration(configurationItem, partRevision, pNumber, createPartLink));
        }});

        Set<Effectivity> effectivities = new HashSet<>();
        switch (productBaselineType) {
            case EFFECTIVE_DATE:
                effectivities.add(new DateBasedEffectivity("DateEffectivity-" + pVersion, configurationItem, new Date(), new Date()));
                break;
            case EFFECTIVE_LOT_ID:
                effectivities.add(new LotBasedEffectivity("LotIdEffectivity-" + pVersion, configurationItem, "54", "56"));
                break;
            case EFFECTIVE_SERIAL_NUMBER:
                effectivities.add(new SerialNumberBasedEffectivity("SerialNumberEffectivity-" + pVersion, configurationItem, "1", "3"));
                break;
            default:
        }
        partRevision.setEffectivities(effectivities);

        return partRevision;
    }

    private PartIteration createPartIteration(ConfigurationItem configurationItem, PartRevision partRevision, String pNumber, boolean createPartLink) throws PartMasterNotFoundException {
        PartIteration partIteration = new PartIteration(partRevision, configurationItem.getAuthor());
        if (createPartLink) {
            partIteration.setComponents(new ArrayList<PartUsageLink>() {{
                String newPNumber = String.valueOf(Integer.parseInt(pNumber) + 1);
                add(createPartUsageLink(configurationItem, newPNumber, ProductBaselineType.EFFECTIVE_SERIAL_NUMBER));
                newPNumber = String.valueOf(Integer.parseInt(newPNumber) + 1);
                add(createPartUsageLink(configurationItem, newPNumber, ProductBaselineType.EFFECTIVE_LOT_ID));
            }});
        }
        return partIteration;
    }

    private PartUsageLink createPartUsageLink(ConfigurationItem configurationItem, String pNumber, ProductBaselineType productBaselineType) throws PartMasterNotFoundException {
        PartMaster partMaster = createPartMaster(configurationItem, pNumber, "B", false, productBaselineType);
        Mockito.when(partMasterDAO.loadPartM(new PartMasterKey(configurationItem.getWorkspaceId(), pNumber))).thenReturn(partMaster);
        PartUsageLink partUsageLink = new PartUsageLink();
        partUsageLink.setComponent(partMaster);
        return partUsageLink;
    }
}