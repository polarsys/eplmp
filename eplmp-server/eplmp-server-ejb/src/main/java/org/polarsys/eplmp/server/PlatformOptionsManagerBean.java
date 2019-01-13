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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import org.polarsys.eplmp.core.admin.PlatformOptions;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IPlatformOptionsManagerLocal;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morgan Guimard
 */
@DeclareRoles({UserGroupMapping.ADMIN_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(IPlatformOptionsManagerLocal.class)
@Stateless(name = "PlatformOptionsManagerBean")
public class PlatformOptionsManagerBean implements IPlatformOptionsManagerLocal {

    private static final Logger LOGGER = Logger.getLogger(PlatformOptionsManagerBean.class.getName());

    @Inject
    private EntityManager em;

    @Override
    public PlatformOptions getPlatformOptions() {
        return loadPlatformOptions();
    }

    @Override
    public OperationSecurityStrategy getWorkspaceCreationStrategy(){
        PlatformOptions platformOptions = loadPlatformOptions();
        return platformOptions.getWorkspaceCreationStrategy();
    }

    @Override
    public OperationSecurityStrategy getRegistrationStrategy(){
        PlatformOptions platformOptions = loadPlatformOptions();
        return platformOptions.getRegistrationStrategy();
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public void setWorkspaceCreationStrategy(OperationSecurityStrategy strategy){
        PlatformOptions platformOptions = loadPlatformOptions();
        platformOptions.setWorkspaceCreationStrategy(strategy);
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public void setRegistrationStrategy(OperationSecurityStrategy strategy){
        PlatformOptions platformOptions = loadPlatformOptions();
        platformOptions.setRegistrationStrategy(strategy);
    }

    private PlatformOptions loadPlatformOptions() {

        PlatformOptions platformOptions = em.find(PlatformOptions.class, PlatformOptions.UNIQUE_ID);

        if(platformOptions == null){
            LOGGER.log(Level.INFO, "No options set. Creating default options ...");
            platformOptions = new PlatformOptions();
            platformOptions.setDefaults();
            em.persist(platformOptions);
            em.flush();
        }

        return platformOptions;

    }

}
