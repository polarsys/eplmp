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

import org.polarsys.eplmp.core.exceptions.CreationException;
import org.polarsys.eplmp.core.exceptions.TagAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.TagNotFoundException;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.meta.TagKey;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

public class TagDAO {

    private EntityManager em;
    private Locale mLocale;

    public TagDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public Tag[] findAllTags(String pWorkspaceId) {
        Tag[] tags;
        TypedQuery<Tag> query = em.createQuery("SELECT DISTINCT t FROM Tag t WHERE t.workspaceId = :workspaceId",Tag.class);
        List<Tag> listTags = query.setParameter("workspaceId", pWorkspaceId).getResultList();
        tags = new Tag[listTags.size()];
        for (int i = 0; i < listTags.size(); i++) {
            tags[i] = listTags.get(i);
        }

        return tags;
    }

    public void deleteOrphanTags(String pWorkspaceId) {
        TypedQuery<Tag> query = em.createQuery("SELECT t FROM Tag t WHERE t.workspaceId = :workspaceId AND t.label <> ALL (SELECT t2.label FROM DocumentMaster m, IN (m.tags) t2 WHERE t2.workspaceId = :workspaceId)", Tag.class);
        List<Tag> tags = query.setParameter("workspaceId", pWorkspaceId).getResultList();
        for (Tag t : tags) {
            em.remove(t);
        }
    }

    public void removeTag(TagKey pTagKey) throws TagNotFoundException {
        Tag tag = em.find(Tag.class, pTagKey);
        if (tag == null) {
            throw new TagNotFoundException(mLocale, pTagKey);
        } else {
            em.remove(tag);
        }
    }

    public Tag loadTag(TagKey pTagKey) throws TagNotFoundException {
        Tag tag = em.find(Tag.class,pTagKey);
        if (tag == null) {
            throw new TagNotFoundException(mLocale, pTagKey);
        } else {
            return tag;
        }
    }

    public void createTag(Tag pTag) throws CreationException, TagAlreadyExistsException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pTag);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new TagAlreadyExistsException(mLocale, pTag);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }
}
