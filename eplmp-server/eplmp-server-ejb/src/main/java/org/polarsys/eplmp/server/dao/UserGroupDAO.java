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
package org.polarsys.eplmp.server.dao;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.UserGroupKey;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.EntityConstraintException;
import org.polarsys.eplmp.core.exceptions.UserGroupAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.UserGroupNotFoundException;
import org.polarsys.eplmp.core.security.WorkspaceUserGroupMembership;
import org.polarsys.eplmp.core.security.WorkspaceUserGroupMembershipKey;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.List;


@RequestScoped
public class UserGroupDAO {

    public static final String WORKSPACE_ID = "workspaceId";

    @Inject
    private EntityManager em;

    public UserGroupDAO() {
    }

    public UserGroup loadUserGroup(UserGroupKey pKey) throws UserGroupNotFoundException {
        UserGroup group = em.find(UserGroup.class, pKey);
        if (group == null) {
            throw new UserGroupNotFoundException(pKey);
        } else {
            return group;
        }
    }

    public WorkspaceUserGroupMembership[] getUserGroupMemberships(String pWorkspaceId, User pUser) {
        WorkspaceUserGroupMembership[] ms;
        TypedQuery<WorkspaceUserGroupMembership> query = em.createQuery("SELECT DISTINCT m FROM WorkspaceUserGroupMembership m WHERE m.workspaceId = :workspaceId AND :user MEMBER OF m.member.users", WorkspaceUserGroupMembership.class);
        query.setParameter(WORKSPACE_ID, pWorkspaceId);
        query.setParameter("user", pUser);
        List<WorkspaceUserGroupMembership> listUserGroupMemberships = query.getResultList();
        ms = new WorkspaceUserGroupMembership[listUserGroupMemberships.size()];
        for (int i = 0; i < listUserGroupMemberships.size(); i++) {
            ms[i] = listUserGroupMemberships.get(i);
        }
        return ms;
    }

    public UserGroup[] findAllUserGroups(String pWorkspaceId) {
        UserGroup[] groups;
        TypedQuery<UserGroup> query = em.createQuery("SELECT DISTINCT g FROM UserGroup g WHERE g.workspaceId = :workspaceId", UserGroup.class);
        List<UserGroup> listUserGroups = query.setParameter(WORKSPACE_ID, pWorkspaceId).getResultList();
        groups = new UserGroup[listUserGroups.size()];
        for (int i = 0; i < listUserGroups.size(); i++) {
            groups[i] = listUserGroups.get(i);
        }
        return groups;
    }

    public WorkspaceUserGroupMembership loadUserGroupMembership(WorkspaceUserGroupMembershipKey pKey) throws UserGroupNotFoundException {
        WorkspaceUserGroupMembership workspaceUserGroupMembership = em.find(WorkspaceUserGroupMembership.class, pKey);
        if (workspaceUserGroupMembership == null) {
            throw new UserGroupNotFoundException(new UserGroupKey(pKey.getWorkspaceId(), pKey.getMemberId()));
        } else {
            return workspaceUserGroupMembership;
        }
    }

    public void addUserGroupMembership(Workspace pWorkspace, UserGroup pMember) {
        WorkspaceUserGroupMembership ms = em.find(WorkspaceUserGroupMembership.class, new WorkspaceUserGroupMembershipKey(pWorkspace.getId(), pWorkspace.getId(), pMember.getId()));
        if (ms == null) {
            ms = new WorkspaceUserGroupMembership(pWorkspace, pMember);
            em.persist(ms);
        }
    }

    public void removeUserGroupMembership(WorkspaceUserGroupMembershipKey pKey) {
        WorkspaceUserGroupMembership ms = em.find(WorkspaceUserGroupMembership.class, pKey);
        if (ms != null) {
            em.remove(ms);
        }
    }

    public void removeUserFromAllGroups(User pUser) {
        TypedQuery<UserGroup> query = em.createQuery("SELECT DISTINCT g FROM UserGroup g WHERE g.workspaceId = :workspaceId", UserGroup.class);
        List<UserGroup> listUserGroups = query.setParameter(WORKSPACE_ID, pUser.getWorkspaceId()).getResultList();
        for (UserGroup listUserGroup : listUserGroups) {
            listUserGroup.removeUser(pUser);
        }
    }

    public WorkspaceUserGroupMembership[] findAllWorkspaceUserGroupMemberships(String pWorkspaceId) {
        WorkspaceUserGroupMembership[] memberships;
        TypedQuery<WorkspaceUserGroupMembership> query = em.createQuery("SELECT DISTINCT m FROM WorkspaceUserGroupMembership m WHERE m.workspaceId = :workspaceId", WorkspaceUserGroupMembership.class);
        List<WorkspaceUserGroupMembership> listMemberships = query.setParameter(WORKSPACE_ID, pWorkspaceId).getResultList();
        memberships = new WorkspaceUserGroupMembership[listMemberships.size()];
        for (int i = 0; i < listMemberships.size(); i++) {
            memberships[i] = listMemberships.get(i);
        }

        return memberships;
    }

    public void removeUserGroup(UserGroup pUserGroup) throws EntityConstraintException {
        removeUserGroupMembership(new WorkspaceUserGroupMembershipKey(pUserGroup.getWorkspaceId(), pUserGroup.getWorkspaceId(), pUserGroup.getId()));
        try {
            em.remove(pUserGroup);
            em.flush();
        } catch (PersistenceException pPEx) {
            throw new EntityConstraintException("EntityConstraintException28");
        }
    }

    public boolean hasACLConstraint(UserGroupKey pKey) {
        Query query = em.createQuery("SELECT DISTINCT a FROM ACLUserGroupEntry a WHERE a.principal.id = :id AND a.principal.workspaceId = :workspaceId");
        query.setParameter("id", pKey.getId());
        query.setParameter(WORKSPACE_ID, pKey.getWorkspaceId());
        return !query.getResultList().isEmpty();
    }

    public void createUserGroup(UserGroup pUserGroup) throws CreationException, UserGroupAlreadyExistsException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pUserGroup);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new UserGroupAlreadyExistsException(pUserGroup);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException();
        }
    }

    public List<UserGroup> getUserGroups(String workspaceId, User user) {
        return em.createNamedQuery("UserGroup.findUserGroups", UserGroup.class).
                setParameter(WORKSPACE_ID, workspaceId).
                setParameter("user", user).
                getResultList();
    }
}
