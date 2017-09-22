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

import javax.persistence.EntityManager;
import java.util.Locale;

public class MarkerDAO {

    private EntityManager em;
    private Locale mLocale;

    public MarkerDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
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

    public void removeMarker(int pId) throws MarkerNotFoundException {
        Marker marker = loadMarker(pId);
        em.remove(marker);
    }

}
