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

import org.polarsys.eplmp.core.exceptions.MarkerNotFoundException;
import org.polarsys.eplmp.core.product.Marker;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;

@Stateless(name = "MarkerDAO")
public class MarkerDAO {

    @PersistenceContext
    private EntityManager em;

    private Locale mLocale;

    public MarkerDAO() {
        mLocale = Locale.getDefault();
    }

    public void createMarker(Marker pMarker) {
        em.persist(pMarker);
        em.flush();
    }

    public Marker loadMarker(int pId) throws MarkerNotFoundException {
        Marker marker = em.find(Marker.class, pId);
        if (marker == null) {
            throw new MarkerNotFoundException(mLocale, pId);
        } else {
            return marker;
        }
    }

    public Marker loadMarker(Locale pLocale, int pId) throws MarkerNotFoundException {
        mLocale = pLocale;
        return loadMarker(pId);
    }

    public void removeMarker(int pId) throws MarkerNotFoundException {
        Marker marker = loadMarker(pId);
        em.remove(marker);
    }

}
