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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserKey;
import org.polarsys.eplmp.core.exceptions.TaskNotFoundException;
import org.polarsys.eplmp.core.workflow.Task;
import org.polarsys.eplmp.core.workflow.TaskKey;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Locale;

public class TaskDAO {
    
    private EntityManager em;
    private Locale mLocale;
    
    public TaskDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale =pLocale;
    }
    
    public TaskDAO(EntityManager pEM) {
        em=pEM;
        mLocale=Locale.getDefault();
    }
    
    public Task loadTask(TaskKey pTaskKey) throws TaskNotFoundException {
        Task task = em.find(Task.class,pTaskKey);
        if (task == null) {
            throw new TaskNotFoundException(mLocale, pTaskKey);
        } else {
            return task;
        }
    }
    
    public Task[] findTasks(User pUser){
        Task[] tasks;
        TypedQuery<Task> query = em.createQuery("SELECT DISTINCT t FROM Task t WHERE t.worker = :user", Task.class);
        query.setParameter("user",pUser);
        List<Task> listTasks = query.getResultList();
        tasks = new Task[listTasks.size()];
        for(int i=0;i<listTasks.size();i++) {
            tasks[i] = listTasks.get(i);
        }
        
        return tasks;
    }

    public Task[] findAssignedTasks(String workspaceId, String userLogin){
        Task[] tasks;
        TypedQuery<Task> query = em.createNamedQuery("Task.findAssignedTasks", Task.class);
        query.setParameter("login", userLogin);
        query.setParameter("workspaceId",workspaceId);
        List<Task> listTasks = query.getResultList();
        tasks = new Task[listTasks.size()];
        for(int i=0;i<listTasks.size();i++) {
            tasks[i] = listTasks.get(i);
        }

        return tasks;
    }

    public Task[] findInProgressTasks(String workspaceId, String userLogin){
        Task[] tasks;
        TypedQuery<Task> query = em.createNamedQuery("Task.findInProgressTasks", Task.class);
        query.setParameter("login", userLogin);
        query.setParameter("workspaceId",workspaceId);
        List<Task> listTasks = query.getResultList();
        tasks = new Task[listTasks.size()];
        for(int i=0;i<listTasks.size();i++) {
            tasks[i] = listTasks.get(i);
        }

        return tasks;
    }

}
