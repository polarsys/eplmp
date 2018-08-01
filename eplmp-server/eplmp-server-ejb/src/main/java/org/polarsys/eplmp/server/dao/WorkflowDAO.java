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
package org.polarsys.eplmp.server.dao;

import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.workflow.Activity;
import org.polarsys.eplmp.core.workflow.Workflow;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflow;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflowKey;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class WorkflowDAO {

    public static final String WORKFLOW = "workflow";
    public static final String WORKSPACE = "workspace";

    @PersistenceContext
    private EntityManager em;

    public void createWorkflow(Workflow pWf) {
        //Hack to prevent a bug inside the JPA implementation (Eclipse Link)
        List<Activity> activities = pWf.getActivities();
        pWf.setActivities(new ArrayList<>());
        em.persist(pWf);
        em.flush();
        pWf.setActivities(activities);
    }

    public Workflow duplicateWorkflow(Workflow workflow) {
        Workflow duplicatedWF = new Workflow(workflow.getFinalLifeCycleState());
        em.persist(duplicatedWF);
        em.flush();

        List<Activity> rwActivities = new ArrayList<>();
        for (Activity activity : workflow.getActivities()) {
            Activity clonedActivity = activity.clone();
            clonedActivity.setWorkflow(duplicatedWF);
            Activity relaunchActivity = activity.getRelaunchActivity();
            if (relaunchActivity != null) {
                Activity clonedRelaunchActivity = rwActivities.get(relaunchActivity.getStep());
                clonedActivity.setRelaunchActivity(clonedRelaunchActivity);
            }
            rwActivities.add(clonedActivity);
        }

        duplicatedWF.setActivities(rwActivities);
        return duplicatedWF;

    }

    public DocumentRevision getDocumentTarget(Workflow pWorkflow) {
        TypedQuery<DocumentRevision> query = em.createNamedQuery("DocumentRevision.findByWorkflow", DocumentRevision.class);
        try {
            return query.setParameter(WORKFLOW, pWorkflow).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public PartRevision getPartTarget(Workflow pWorkflow) {
        TypedQuery<PartRevision> query = em.createNamedQuery("PartRevision.findByWorkflow", PartRevision.class);
        try {
            return query.setParameter(WORKFLOW, pWorkflow).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public WorkspaceWorkflow getWorkspaceWorkflowTarget(String workspaceId, Workflow workflow) {
        TypedQuery<WorkspaceWorkflow> query = em.createQuery("SELECT w FROM WorkspaceWorkflow w WHERE w.workflow = :workflow AND w.workspace.id = :workspaceId", WorkspaceWorkflow.class);
        try {
            return query.setParameter(WORKFLOW, workflow)
                    .setParameter("workspaceId", workspaceId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void removeWorkflowConstraints(WorkspaceWorkflow ww) {
        List<Workflow> abortedWorkflowList = ww.getAbortedWorkflows();
        Workflow workflow = ww.getWorkflow();
        abortedWorkflowList.forEach(this::removeWorkflowConstraints);
        removeWorkflowConstraints(workflow);
    }

    public void removeWorkflowConstraints(DocumentRevision pDocR) {
        List<Workflow> abortedWorkflowList = pDocR.getAbortedWorkflows();
        Workflow workflow = pDocR.getWorkflow();
        abortedWorkflowList.forEach(this::removeWorkflowConstraints);
        removeWorkflowConstraints(workflow);
    }

    public void removeWorkflowConstraints(PartRevision pPartR) {
        List<Workflow> abortedWorkflowList = pPartR.getAbortedWorkflows();
        Workflow workflow = pPartR.getWorkflow();
        abortedWorkflowList.forEach(this::removeWorkflowConstraints);
        removeWorkflowConstraints(workflow);
    }

    public void removeWorkflowConstraints(Workflow pWorkflow) {
        if (pWorkflow != null) {
            for (Activity activity : pWorkflow.getActivities()) {
                activity.setRelaunchActivity(null);
            }
        }
    }

    public Workflow getWorkflow(int workflowId) {
        return em.find(Workflow.class, workflowId);
    }


    public void createWorkspaceWorkflow(WorkspaceWorkflow workspaceWorkflow) {
        em.persist(workspaceWorkflow);
        em.flush();
    }

    public WorkspaceWorkflow getWorkspaceWorkflow(String workspaceId, String workspaceWorkflowId) {
        return em.find(WorkspaceWorkflow.class, new WorkspaceWorkflowKey(workspaceId, workspaceWorkflowId));
    }

    public List<WorkspaceWorkflow> getWorkspaceWorkflowList(String workspaceId) {
        TypedQuery<WorkspaceWorkflow> query = em.createQuery("SELECT w FROM WorkspaceWorkflow w WHERE w.workspace.id = :workspaceId", WorkspaceWorkflow.class);
        try {
            return query.setParameter("workspaceId", workspaceId).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteWorkspaceWorkflow(WorkspaceWorkflow workspaceWorkflow) {
        removeWorkflowConstraints(workspaceWorkflow);
        em.remove(workspaceWorkflow);
        em.flush();
    }

    public void removeWorkflowConstraints(Workspace workspace) {
        removeWorkflowConstraintsOnDocuments(workspace);
        removeWorkflowConstraintsOnParts(workspace);
        removeWorkflowConstraintsOnWorkspaceWorkflow(workspace);
    }

    private void removeWorkflowConstraintsOnWorkspaceWorkflow(Workspace workspace) {
        TypedQuery<Workflow> query = em.createQuery("SELECT ww.workflow FROM WorkspaceWorkflow ww WHERE ww.workspace = :workspace AND ww.workflow IS NOT NULL",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);

        query = em.createQuery("SELECT ww.abortedWorkflows FROM WorkspaceWorkflow ww WHERE ww.workspace = :workspace AND size(ww.abortedWorkflows) > 0",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);
    }

    private void removeWorkflowConstraintsOnParts(Workspace workspace) {
        TypedQuery<Workflow> query = em.createQuery("SELECT p.workflow FROM PartRevision p WHERE p.partMaster.workspace = :workspace AND p.workflow IS NOT NULL",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);

        query = em.createQuery("SELECT p.abortedWorkflows FROM PartRevision p WHERE p.partMaster.workspace = :workspace AND size(p.abortedWorkflows) > 0",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);
    }

    private void removeWorkflowConstraintsOnDocuments(Workspace workspace) {
        TypedQuery<Workflow> query = em.createQuery("SELECT d.workflow FROM DocumentRevision d WHERE d.documentMaster.workspace = :workspace AND d.workflow IS NOT NULL",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);

        query = em.createQuery("SELECT d.abortedWorkflows FROM DocumentRevision d WHERE d.documentMaster.workspace = :workspace AND size(d.abortedWorkflows) > 0",
                Workflow.class).setParameter(WORKSPACE, workspace);

        query.getResultList().forEach(this::removeWorkflowConstraints);
    }


}
