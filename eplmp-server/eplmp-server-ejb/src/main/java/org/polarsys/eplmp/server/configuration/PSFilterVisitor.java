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

package org.polarsys.eplmp.server.configuration;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.exceptions.EntityConstraintException;
import org.polarsys.eplmp.core.exceptions.NotAllowedException;
import org.polarsys.eplmp.core.exceptions.PartMasterNotFoundException;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.server.dao.PartMasterDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Stateless
public class PSFilterVisitor {
    @Inject
    private PartMasterDAO partMasterDAO;

    private Locale locale;
    private String workspaceId;
    private ProductStructureFilter filter;
    private Component component;
    private int stopAtDepth = -1;
    private boolean stopped = false;

    private PSFilterVisitorCallbacks callbacks;

    /**
     * Start the visitor with given part master
     * */
    public void visit(User pUser, ProductStructureFilter pFilter, PartMaster pNodeFrom, Integer pStopAtDepth, PSFilterVisitorCallbacks callbacks) throws PartMasterNotFoundException, EntityConstraintException, NotAllowedException {
        this.locale = pUser.getLocale();
        this.workspaceId = pUser.getWorkspaceId();
        this.filter = pFilter;
        this.callbacks = callbacks;

        setDepth(pStopAtDepth);
        List<PartLink> currentPath = new ArrayList<>();
        List<PartMaster> currentPathParts = new ArrayList<>();
        List<PartIteration> currentPathPartIterations = new ArrayList<>();

        PartLink virtualLink = createVirtualRootLink(pNodeFrom);
        currentPathParts.add(pNodeFrom);
        currentPath.add(virtualLink);

        component = new Component(pNodeFrom.getAuthor(),pNodeFrom,currentPath,null);
        List<Component> result = getComponentsRecursively(component, currentPathPartIterations, currentPathParts, currentPath);
        component.setComponents(result);
    }

    /**
     * Start the visitor with given path
     * */
    public void visit(User pUser, ProductStructureFilter pFilter, List<PartLink> pStartingPath, Integer pStopAtDepth, PSFilterVisitorCallbacks callbacks) throws PartMasterNotFoundException, EntityConstraintException, NotAllowedException {
        this.locale = pUser.getLocale();
        this.workspaceId = pUser.getWorkspaceId();
        this.filter = pFilter;
        this.callbacks = callbacks;

        setDepth(pStopAtDepth);
        List<PartMaster> currentPathParts = new ArrayList<>();
        List<PartIteration> currentPathPartIterations = new ArrayList<>();

        PartMaster rootNode = pStartingPath.get(pStartingPath.size() - 1).getComponent();
        currentPathParts.add(rootNode);

        component = new Component(rootNode.getAuthor(),rootNode, pStartingPath,null);
        List<Component> result = getComponentsRecursively(component, currentPathPartIterations, currentPathParts, pStartingPath);
        component.setComponents(result);
    }

    public void stop(){
        stopped = true;
    }

    private void setDepth(Integer pDepth){
        stopAtDepth = pDepth == null ? -1 : pDepth;
    }

    private List<Component> getComponentsRecursively(Component currentComponent, List<PartIteration> pCurrentPathPartIterations, List<PartMaster> pCurrentPathParts, List<PartLink> pCurrentPath) throws PartMasterNotFoundException, NotAllowedException, EntityConstraintException {
        List<Component> components = new ArrayList<>();

        if(stopped || !callbacks.onPathWalk(new ArrayList<>(pCurrentPath), new ArrayList<>(pCurrentPathParts))){
            return components;
        }

        // Current depth
        int currentDepth = pCurrentPathParts.size();

        // Current part master is the last from pCurrentPathParts
        PartMaster currentUsagePartMaster = pCurrentPathParts.get(pCurrentPathParts.size()-1);

        // Find filtered iterations to visit
        List<PartIteration> partIterations = filter.filter(currentUsagePartMaster);

        if(partIterations.isEmpty()){
            callbacks.onUnresolvedVersion(currentUsagePartMaster);
        }

        if(partIterations.size() > 1){
            callbacks.onIndeterminateVersion(currentUsagePartMaster, new ArrayList<>(partIterations));
        }

        if(partIterations.size()==1){
            currentComponent.setRetainedIteration(partIterations.get(0));
        }

        // Visit them all, potentially diverging branches
        for (PartIteration partIteration : partIterations) {

            // We know which iteration of current partMaster, add it to list
            List<PartIteration> copyPartIteration = new ArrayList<>(pCurrentPathPartIterations);
            copyPartIteration.add(partIteration);

            // Is branch over ?
            if(partIteration.getComponents().isEmpty()){
                callbacks.onBranchDiscovered(new ArrayList<>(pCurrentPath),new ArrayList<>(copyPartIteration));
            }

            components.addAll(navigateLinks(partIteration, pCurrentPath, copyPartIteration, pCurrentPathParts, currentDepth));
        }

        return components;
    }

    private List<Component> navigateLinks(PartIteration pPartIteration, List<PartLink> pCurrentPath, List<PartIteration> pCopyPartIteration, List<PartMaster> pCurrentPathParts, int pCurrentDepth) throws NotAllowedException, PartMasterNotFoundException, EntityConstraintException {
        List<Component> components = new ArrayList<>();
        for (PartUsageLink usageLink : pPartIteration.getComponents()) {

            List<PartLink> currentPath = new ArrayList<>(pCurrentPath);
            currentPath.add(usageLink);

            // Filter the current path, potentially diverging branches
            List<PartLink> eligiblePath = filter.filter(currentPath);

            if(eligiblePath.isEmpty() && !usageLink.isOptional()){
                callbacks.onUnresolvedPath(new ArrayList<>(currentPath), new ArrayList<>(pCopyPartIteration));
            }

            if(eligiblePath.size() > 1 ){
                callbacks.onIndeterminatePath(new ArrayList<>(currentPath), new ArrayList<>(pCopyPartIteration));
            }

            if (eligiblePath.size() == 1 && eligiblePath.get(0).isOptional()){
                callbacks.onOptionalPath(new ArrayList<>(currentPath), new ArrayList<>(pCopyPartIteration));
            }

            components.addAll(getComponentsFromPartLinks(pCurrentPathParts, currentPath, pCopyPartIteration, pCurrentDepth, eligiblePath));
        }

        return components;
    }

    private List<Component> getComponentsFromPartLinks(List<PartMaster> pCurrentPathParts, List<PartLink> pCurrentPath, List<PartIteration> pCopyPartIteration, int pCurrentDepth, List<PartLink> eligiblePath) throws PartMasterNotFoundException, EntityConstraintException, NotAllowedException {
        List<Component> components = new ArrayList<>();

        for(PartLink link : eligiblePath){
            List<PartLink> nextPath = new ArrayList<>(pCurrentPath);
            nextPath.add(link);

            if (stopAtDepth == -1 || stopAtDepth >= pCurrentDepth) {

                // Going on a new path
                PartMaster pm = loadPartMaster(link.getComponent().getNumber());

                // Run cyclic integrity check here
                if(pCurrentPathParts.contains(pm)){
                    throw new EntityConstraintException(locale,"EntityConstraintException12");
                }

                // Continue tree walking on pm
                List<PartMaster> copyPathParts = new ArrayList<>(pCurrentPathParts);
                List<PartLink> copyPath = new ArrayList<>(nextPath);
                List<PartIteration> copyPartIterations = new ArrayList<>(pCopyPartIteration);
                copyPathParts.add(pm);

                // Recursive
                Component subComponent= new Component(pm.getAuthor(), pm, copyPath, null);
                subComponent.setComponents(getComponentsRecursively(subComponent, copyPartIterations, copyPathParts, copyPath));
                components.add(subComponent);
            }

        }

        return components;
    }

    private PartMaster loadPartMaster(String partNumber) throws PartMasterNotFoundException {
        return partMasterDAO.loadPartM(new PartMasterKey(workspaceId, partNumber));
    }

    private PartLink createVirtualRootLink(PartMaster pNodeFrom) {

        return new PartLink() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public double getAmount() {
                return 1;
            }

            @Override
            public String getUnit() {
                return null;
            }

            @Override
            public String getComment() {
                return "";
            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public PartMaster getComponent() {
                return pNodeFrom;
            }

            @Override
            public List<PartSubstituteLink> getSubstitutes() {
                return null;
            }

            @Override
            public String getReferenceDescription() {
                return null;
            }

            @Override
            public Character getCode() {
                return '-';
            }

            @Override
            public String getFullId() {
                return "-1";
            }

            @Override
            public List<CADInstance> getCadInstances() {
                return null;
            }
        };
    }

    /**
     * Getters
     */
    public Component getComponent() {
        return component;
    }
}
