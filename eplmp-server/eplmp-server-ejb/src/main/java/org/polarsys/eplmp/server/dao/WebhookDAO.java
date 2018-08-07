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

import org.polarsys.eplmp.core.exceptions.WebhookNotFoundException;
import org.polarsys.eplmp.core.hooks.Webhook;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


@RequestScoped
public class WebhookDAO {

    @Inject
    private EntityManager em;

    public WebhookDAO() {
    }

    public Webhook loadWebhook(int id) throws WebhookNotFoundException {
        Webhook webhook = em.find(Webhook.class, id);
        if (webhook == null) {
            throw new WebhookNotFoundException(id);
        }
        return webhook;
    }

    public void removeWebhook(Webhook w) {
        em.remove(w);
        em.flush();
    }

    public List<Webhook> loadWebhooks(String workspaceId) {
        return em.createNamedQuery("Webhook.findByWorkspace", Webhook.class)
                .setParameter("workspaceId", workspaceId).getResultList();
    }

    public void createWebhook(Webhook webhook) {
        em.persist(webhook);
        em.flush();
    }

    public List<Webhook> loadActiveWebhooks(String workspaceId) {
        return em.createNamedQuery("Webhook.findActiveByWorkspace", Webhook.class)
                .setParameter("workspaceId", workspaceId).getResultList();
    }
}
