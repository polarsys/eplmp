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

import org.polarsys.eplmp.core.document.DocumentMasterTemplate;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.WorkflowModelAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.WorkflowModelNotFoundException;
import org.polarsys.eplmp.core.product.PartMasterTemplate;
import org.polarsys.eplmp.core.workflow.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "WorkflowModelDAO")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class WorkflowModelDAO {

    private static final Logger LOGGER = Logger.getLogger(WorkflowModelDAO.class.getName());
    public static final String WORKSPACE_ID = "workspaceId";

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public WorkflowModelDAO() {
        mLocale = Locale.getDefault();
    }

    public void removeAllActivityModels(WorkflowModelKey pKey) {
        em.createQuery("DELETE FROM TaskModel t WHERE t.activityModel.workflowModel.id = :id AND t.activityModel.workflowModel.workspaceId = :workspaceId")
                .setParameter("id", pKey.getId())
                .setParameter(WORKSPACE_ID, pKey.getWorkspaceId()).executeUpdate();
        em.createQuery("DELETE FROM ActivityModel a WHERE a.workflowModel.id = :id AND a.workflowModel.workspaceId = :workspaceId")
                .setParameter("id", pKey.getId())
                .setParameter(WORKSPACE_ID, pKey.getWorkspaceId()).executeUpdate();
    }

    public void removeWorkflowModel(WorkflowModelKey pKey) throws WorkflowModelNotFoundException {
        WorkflowModel model = loadWorkflowModel(pKey);
        for(ActivityModel activity:model.getActivityModels()){
            activity.setRelaunchActivity(null);
        }
        em.flush();
        em.remove(model);
    }

    public List<WorkflowModel> findAllWorkflowModels(String pWorkspaceId) {
        TypedQuery<WorkflowModel> query = em.createQuery("SELECT DISTINCT w FROM WorkflowModel w WHERE w.workspaceId = :workspaceId",WorkflowModel.class);
        return query.setParameter(WORKSPACE_ID, pWorkspaceId).getResultList();
    }

    public void removeWorkflowModelConstraints(WorkflowModel pWorkflowModel){
        if(pWorkflowModel != null) {
            List<ActivityModel> activityModels = pWorkflowModel.getActivityModels();
            for(ActivityModel activityModel:activityModels){
                activityModel.setRelaunchActivity(null);
            }
            em.flush();
        }
    }

    public void createWorkflowModel(WorkflowModel pModel) throws WorkflowModelAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            //Because ActivityModel has a generated id which is part of the TaskModel's PK
            //we force generated it to avoid cache issue with the TaskModel.
            List<ActivityModel> activityModels = pModel.getActivityModels();
            List<List<TaskModel>> taskModels=new LinkedList<>();
            for(ActivityModel activityModel:activityModels){
                taskModels.add(activityModel.getTaskModels());
                activityModel.setTaskModels(new ArrayList<>());
            }
            em.persist(pModel);
            em.flush();
            int i=0;
            for(ActivityModel activityModel:activityModels){
                activityModel.setTaskModels(taskModels.get(i++));
            }
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINEST,null,pEEEx);
            throw new WorkflowModelAlreadyExistsException(mLocale, pModel);
        } catch (PersistenceException pPEx) {
            LOGGER.log(Level.FINEST,null,pPEx);
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createWorkflowModel(Locale pLocale, WorkflowModel pModel) throws WorkflowModelAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createWorkflowModel(pModel);
    }

    public WorkflowModel loadWorkflowModel(WorkflowModelKey pKey) throws WorkflowModelNotFoundException {
        WorkflowModel model = em.find(WorkflowModel.class, pKey);
        if (model == null) {
            throw new WorkflowModelNotFoundException(mLocale, pKey.getId());
        } else {
            return model;
        }
    }

    public WorkflowModel loadWorkflowModel(Locale pLocale, WorkflowModelKey pKey) throws WorkflowModelNotFoundException {
        mLocale = pLocale;
        return loadWorkflowModel(pKey);
    }

    public boolean isInUseInDocumentMasterTemplate(WorkflowModel workflowModel) {
        return !em.createNamedQuery("DocumentMasterTemplate.findWhereWorkflowModel", DocumentMasterTemplate.class)
                .setParameter("workflowModel",workflowModel)
                .getResultList()
                .isEmpty();
    }

    public boolean isInUseInPartMasterTemplate(WorkflowModel workflowModel) {
        return !em.createNamedQuery("PartMasterTemplate.findWhereWorkflowModel", PartMasterTemplate.class)
                .setParameter("workflowModel",workflowModel)
                .getResultList()
                .isEmpty();
    }
}
