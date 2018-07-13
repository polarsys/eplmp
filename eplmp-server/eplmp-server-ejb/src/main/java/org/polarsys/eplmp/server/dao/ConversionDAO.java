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
import org.polarsys.eplmp.core.product.Conversion;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Locale;

@Stateless(name = "ConversionDAO")
public class ConversionDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public ConversionDAO() {
        mLocale=Locale.getDefault();
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
            throw new CreationException(mLocale);
        }
    }
    public void createConversion(Locale pLocale, Conversion conversion) throws  CreationException {
        this.mLocale = pLocale;
        createConversion(conversion);
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
}
