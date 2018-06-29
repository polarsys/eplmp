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
import org.polarsys.eplmp.core.exceptions.PartMasterTemplateAlreadyExistsException;
import org.polarsys.eplmp.core.exceptions.PartMasterTemplateNotFoundException;
import org.polarsys.eplmp.core.meta.ListOfValuesKey;
import org.polarsys.eplmp.core.product.PartMasterTemplate;
import org.polarsys.eplmp.core.product.PartMasterTemplateKey;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Stateless(name = "PartMasterTemplateDAO")
public class PartMasterTemplateDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public PartMasterTemplateDAO() {
        mLocale = Locale.getDefault();
    }

    public PartMasterTemplate removePartMTemplate(PartMasterTemplateKey pKey) throws PartMasterTemplateNotFoundException {
        PartMasterTemplate template = loadPartMTemplate(pKey);
        em.remove(template);
        return template;
    }

    public List<PartMasterTemplate> findAllPartMTemplates(String pWorkspaceId) {
        TypedQuery<PartMasterTemplate> query = em.createQuery("SELECT DISTINCT t FROM PartMasterTemplate t WHERE t.workspaceId = :workspaceId", PartMasterTemplate.class);
        return query.setParameter("workspaceId", pWorkspaceId).getResultList();
    }

    public PartMasterTemplate loadPartMTemplate(PartMasterTemplateKey pKey)
            throws PartMasterTemplateNotFoundException {
        PartMasterTemplate template = em.find(PartMasterTemplate.class, pKey);
        if (template == null) {
            throw new PartMasterTemplateNotFoundException(mLocale, pKey.getId());
        } else {
            return template;
        }
    }

    public PartMasterTemplate loadPartMTemplate(Locale pLocale, PartMasterTemplateKey pKey) throws PartMasterTemplateNotFoundException {
        mLocale = pLocale;
        return loadPartMTemplate(pKey);
    }

    public void createPartMTemplate(PartMasterTemplate pTemplate) throws PartMasterTemplateAlreadyExistsException, CreationException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pTemplate);
            em.flush();
        } catch (EntityExistsException pEEEx) {
            throw new PartMasterTemplateAlreadyExistsException(mLocale, pTemplate);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createPartMTemplate(Locale pLocale, PartMasterTemplate pTemplate) throws PartMasterTemplateAlreadyExistsException, CreationException {
        mLocale = pLocale;
        createPartMTemplate(pTemplate);
    }

    public List<PartMasterTemplate> findAllPartMTemplatesFromLOV(ListOfValuesKey lovKey){
        return em.createNamedQuery("PartMasterTemplate.findWhereLOV", PartMasterTemplate.class)
                .setParameter("lovName", lovKey.getName())
                .setParameter("workspace_id", lovKey.getWorkspaceId())
                .getResultList();
    }
}
