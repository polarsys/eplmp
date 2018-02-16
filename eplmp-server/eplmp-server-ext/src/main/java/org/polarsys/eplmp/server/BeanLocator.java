/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class that looks-up EJBs implementing a given business interface from JNDI
 * naming service.
 *
 * @author Olivier Bourgeat
 */
public class BeanLocator {

    private static final Logger LOGGER = Logger.getLogger(BeanLocator.class.getName());

    /**
     * Search for EJBs implementing a given business interface within the naming
     * Context "java:global".
     *
     * @param type Bean's Business Interface.
     * @return the list of EJBs implementing the given interface.
     * @throws NamingException
     */
    public <T> List<T> search(Class<T> type) {

        List<T> result = new ArrayList<>();

        try {
            InitialContext context = new InitialContext();
            Context ctx = (Context) context.lookup("java:global");
            result.addAll(search(type, ctx));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }


        return result;
    }

    /**
     * Search for EJBs implementing a given business interface within the given
     * Naming Context.
     *
     * @param type EJB's Business Interface
     * @param ctx  Current Naming Context
     * @return the list of EJBs implementing the given interface within the
     * context.
     * @throws NamingException
     */
    @SuppressWarnings("unchecked")
    // Cause : Generic Type Erasure
    <T> List<T> search(Class<T> type, Context ctx) {
        List<T> result = new ArrayList<T>();
        try {
            NamingEnumeration<NameClassPair> ncps = ctx.list("");
            while (ncps.hasMoreElements()) {
                try {
                    NameClassPair ncp = ncps.next();
                    Object o = ctx.lookup(ncp.getName());
                    if (ncp.getName().contains("!" + type.getCanonicalName())) {
                        // bean reference
                        LOGGER.info("EJB found: " + ncp.getName());
                        result.add((T) PortableRemoteObject.narrow(o, type));
                    } else if (Context.class.isAssignableFrom(o.getClass())) {
                        // sub-context
                        result.addAll(search(type, (Context) o));
                    }
                    // else ignore this object
                } catch (Exception e) {
                    LOGGER.log(Level.INFO, "Ignoring JNDI entry: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
}
