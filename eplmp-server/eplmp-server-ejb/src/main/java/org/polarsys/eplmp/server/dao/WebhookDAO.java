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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Locale;

@Stateless(name = "WebhookDAO")
public class WebhookDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public WebhookDAO() {
        mLocale = Locale.getDefault();
    }

    public Webhook loadWebhook(int id) throws WebhookNotFoundException {
        Webhook webhook = em.find(Webhook.class, id);
        if (webhook == null) {
            throw new WebhookNotFoundException(mLocale, id);
        }
        return webhook;
    }

    public Webhook loadWebhook(Locale pLocale, int id) throws WebhookNotFoundException {
        mLocale = pLocale;
        return loadWebhook(id);
    }

    public void removeWebook(Webhook w) {
        // then
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
