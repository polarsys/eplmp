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

import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import java.util.List;


@RequestScoped
public class TagDAO {

    @PersistenceContext
    private EntityManager em;

    public TagDAO() {
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

    public void removeTag(TagKey pTagKey) throws TagNotFoundException {
        Tag tag = em.find(Tag.class, pTagKey);
        if (tag == null) {
            throw new TagNotFoundException(pTagKey);
        } else {
            em.remove(tag);
        }
    }


    public Tag loadTag(TagKey pTagKey) throws TagNotFoundException {
        Tag tag = em.find(Tag.class,pTagKey);
        if (tag == null) {
            throw new TagNotFoundException(pTagKey);
        } else {
            return tag;
        }
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
                throw new TagAlreadyExistsException(pTag);
            }
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            if(!silent) {
                throw new CreationException("");
            }
        }
    }
}
