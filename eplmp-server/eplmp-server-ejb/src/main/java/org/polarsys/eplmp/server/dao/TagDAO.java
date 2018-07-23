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

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Stateless(name = "TagDAO")
public class TagDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public TagDAO() {
        mLocale = Locale.getDefault();
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

    public void removeTag(Locale pLocale, TagKey pTagKey) throws TagNotFoundException {
        mLocale = pLocale;
        removeTag(pTagKey);
    }

    public Tag loadTag(TagKey pTagKey) throws TagNotFoundException {
        Tag tag = em.find(Tag.class,pTagKey);
        if (tag == null) {
            throw new TagNotFoundException(mLocale, pTagKey);
        } else {
            return tag;
        }
    }

    public Tag loadTag(Locale pLocale, TagKey pTagKey) throws TagNotFoundException {
        mLocale = pLocale;
        return loadTag(pTagKey);
    }

    public void createTag(Tag pTag) throws CreationException, TagAlreadyExistsException {
        createTag(pTag, false);
    }

    public void createTag(Tag pTag, boolean silent) throws CreationException, TagAlreadyExistsException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pTag);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            if(!silent) {
                throw new TagAlreadyExistsException(mLocale, pTag);
            }
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            if(!silent) {
                throw new CreationException(mLocale);
            }
        }
    }

    public void createTag(Locale pLocale, Tag pTag) throws CreationException, TagAlreadyExistsException {
        mLocale = pLocale;
        createTag(pTag, false);
    }
}
