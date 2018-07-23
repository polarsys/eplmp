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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.configuration.*;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IPSFilterManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.server.configuration.filter.LatestPSFilter;
import org.polarsys.eplmp.server.configuration.filter.LatestReleasedPSFilter;
import org.polarsys.eplmp.server.configuration.filter.ReleasedPSFilter;
import org.polarsys.eplmp.server.configuration.filter.WIPPSFilter;
import org.polarsys.eplmp.server.configuration.spec.ProductBaselineConfigSpec;
import org.polarsys.eplmp.server.configuration.spec.ProductInstanceConfigSpec;
import org.polarsys.eplmp.server.dao.ProductBaselineDAO;
import org.polarsys.eplmp.server.dao.ProductInstanceMasterDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(IPSFilterManagerLocal.class)
@Stateless(name = "PSFilterManagerBean")
public class PSFilterManagerBean implements IPSFilterManagerLocal {

    @Inject
    private ProductBaselineDAO productBaselineDAO;

    @Inject
    private ProductInstanceMasterDAO productInstanceMasterDAO;

    @Inject
    private IUserManagerLocal userManager;

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ProductStructureFilter getBaselinePSFilter(int baselineId) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException {
        ProductBaseline productBaseline = productBaselineDAO.loadBaseline(baselineId);
        //User user = userManager.checkWorkspaceReadAccess(productBaseline.getConfigurationItem().getWorkspaceId());
        return new ProductBaselineConfigSpec(productBaseline);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ProductStructureFilter getProductInstanceConfigSpec(ConfigurationItemKey ciKey, String serialNumber) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, WorkspaceNotEnabledException {
        User user = userManager.checkWorkspaceReadAccess(ciKey.getWorkspace());
        ProductInstanceMasterKey productInstanceMasterKey = new ProductInstanceMasterKey(serialNumber, ciKey);
        ProductInstanceMaster productIM = productInstanceMasterDAO.loadProductInstanceMaster(user.getLocale(), productInstanceMasterKey);
        ProductInstanceIteration productII = productIM.getLastIteration();
        return new ProductInstanceConfigSpec(productII, user);
    }

    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    @Override
    public ProductStructureFilter getPSFilter(ConfigurationItemKey ciKey, String filterType, boolean diverge) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, ProductInstanceMasterNotFoundException, BaselineNotFoundException, WorkspaceNotEnabledException {

        User user = userManager.checkWorkspaceReadAccess(ciKey.getWorkspace());

        if (filterType == null) {
            return new WIPPSFilter(user);
        }

        ProductStructureFilter filter;

        switch (filterType) {

            case "wip":
            case "undefined":
                filter = new WIPPSFilter(user, diverge);
                break;
            case "latest":
                filter = new LatestPSFilter(user, diverge);
                break;
            case "released":
                filter = new ReleasedPSFilter(user, diverge);
                break;
            case "latest-released":
                filter = new LatestReleasedPSFilter(user, diverge);
                break;
            default:
                if (filterType.startsWith("pi-")) {
                    String serialNumber = filterType.substring(3);
                    filter = getProductInstanceConfigSpec(ciKey, serialNumber);
                } else {
                    filter = getBaselinePSFilter(Integer.parseInt(filterType));
                }
                break;
        }
        return filter;
    }

}
