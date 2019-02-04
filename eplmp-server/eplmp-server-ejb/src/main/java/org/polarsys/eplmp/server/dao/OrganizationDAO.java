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

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.Organization;
import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.OrganizationAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.OrganizationNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;


@RequestScoped
public class OrganizationDAO {

    @Inject
    private EntityManager em;

    public OrganizationDAO() {
    }

    public Organization findOrganizationOfAccount(Account pAccount) throws OrganizationNotFoundException {
        try {
            return em.createNamedQuery("Organization.ofAccount", Organization.class)
                    .setParameter("account", pAccount).getSingleResult();
        } catch (NoResultException ex) {
            throw new OrganizationNotFoundException(pAccount.getLogin());
        }
    }

    public boolean hasOrganization(Account pAccount) {
        return !em.createNamedQuery("Organization.ofAccount", Organization.class)
                .setParameter("account", pAccount).getResultList().isEmpty();
    }

    public void updateOrganization(Organization pOrganization) {
        em.merge(pOrganization);
    }

    public void createOrganization(Organization pOrganization) throws OrganizationAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            if (pOrganization.getName().trim().equals(""))
                throw new CreationException();
            em.persist(pOrganization);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new OrganizationAlreadyExistsException(pOrganization);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException();
        }
    }

    public void deleteOrganization(Organization pOrganization) {
        em.remove(pOrganization);
        em.flush();
    }

    public Organization loadOrganization(String pName) throws OrganizationNotFoundException {
        Organization organization = em.find(Organization.class, pName);
        if (organization == null) {
            throw new OrganizationNotFoundException(pName);
        } else {
            return organization;
        }
    }

}
