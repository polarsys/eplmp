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

import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.QueryAlreadyExistsException;
import org.polarsys.eplmp.core.query.Query;
import org.polarsys.eplmp.core.query.QueryContext;
import org.polarsys.eplmp.core.query.QueryRule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morgan Guimard on 09/04/15.
 */

@RequestScoped
public class QueryDAO {

    @Inject
    private EntityManager em;


    private static final Logger LOGGER = Logger.getLogger(QueryDAO.class.getName());

    public QueryDAO() {
    }

    public void createQuery(Query query) throws CreationException, QueryAlreadyExistsException {
        try {

            QueryRule queryRule = query.getQueryRule();

            if (queryRule != null) {
                persistQueryRules(queryRule);
            }

            QueryRule pathDataQueryRule = query.getPathDataQueryRule();

            if (pathDataQueryRule != null) {
                persistQueryRules(pathDataQueryRule);
            }

            em.persist(query);
            em.flush();
            persistContexts(query, query.getContexts());
        } catch (EntityExistsException pEEEx) {
            LOGGER.log(Level.FINEST, null, pEEEx);
            throw new QueryAlreadyExistsException(query);
        } catch (PersistenceException pPEx) {
            LOGGER.log(Level.FINEST, null, pPEx);
            throw new CreationException();
        }
    }

    private void persistContexts(Query query, List<QueryContext> contexts) {
        for (QueryContext context : contexts) {
            context.setParentQuery(query);
            em.persist(context);
        }
        em.flush();
    }

    private void persistQueryRules(QueryRule queryRule) {

        em.persist(queryRule);
        em.flush();

        if (!queryRule.hasSubRules()) {
            return;
        }

        for (QueryRule subRule : queryRule.getSubQueryRules()) {
            subRule.setParentQueryRule(queryRule);
            persistQueryRules(subRule);
        }

    }

    public List<Query> loadQueries(String workspaceId) {
        return em.createNamedQuery("Query.findByWorkspace", Query.class)
                .setParameter("workspaceId", workspaceId)
                .getResultList();
    }

    public Query findQueryByName(String workspaceId, String name) {
        try {
            return em.createNamedQuery("Query.findByWorkspaceAndName", Query.class)
                    .setParameter("workspaceId", workspaceId)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Query loadQuery(int id) {
        return em.find(Query.class, id);
    }

    public void removeQuery(Query query) {
        em.remove(query);
        em.flush();
    }
}
