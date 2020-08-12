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
import org.polarsys.eplmp.core.product.Conversion;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RequestScoped
public class ConversionDAO {

    @Inject
    private EntityManager em;

    public ConversionDAO() {
    }

    public void createConversion(Conversion conversion) throws  CreationException {
        try{
            //the EntityExistsException is thrown only when flush occurs
            em.persist(conversion);
            em.flush();
        } catch(PersistenceException pPEx){
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException();
        }
    }

    public Conversion findConversion(PartIteration partIteration) {
        TypedQuery<Conversion> query = em.createQuery("SELECT DISTINCT c FROM Conversion c WHERE c.partIteration = :partIteration", Conversion.class);
        query.setParameter("partIteration", partIteration);
        try{
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public void deleteConversion(Conversion conversion) {
        em.remove(conversion);
        em.flush();
    }

    public void removePartRevisionConversions(PartRevision pPartR) {
        em.createQuery("DELETE FROM Conversion c WHERE c.partIteration.partRevision = :partRevision", Conversion.class)
                .setParameter("partRevision", pPartR)
                .executeUpdate();
    }

    public void removePartIterationConversion(PartIteration pPartI) {
        em.createQuery("DELETE FROM Conversion c WHERE c.partIteration = :partIteration", Conversion.class)
                .setParameter("partIteration", pPartI)
                .executeUpdate();
    }

    public Integer setPendingConversionsAsFailedIfOver(Integer retentionTimeMs) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, -retentionTimeMs);

        TypedQuery<Conversion> query =
                em.createQuery("SELECT DISTINCT c FROM Conversion c WHERE c.pending = true AND c.startDate <= :date", Conversion.class)
                        .setParameter("date", calendar.getTime());

        List<Conversion> conversions = query.getResultList();
        if(!conversions.isEmpty()){
            for(Conversion conversion: conversions){
                conversion.setPending(false);
                conversion.setEndDate(new Date());
                conversion.setSucceed(false);
            }
            em.flush();
        }

        return conversions.size();

    }
}
