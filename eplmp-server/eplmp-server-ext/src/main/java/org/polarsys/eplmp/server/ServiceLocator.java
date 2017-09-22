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

import org.polarsys.eplmp.core.services.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ServiceLocator class helps to inject EJB services outside injection context
 */
@ApplicationScoped
public class ServiceLocator {

    private static final String STORAGE_MANAGER = "java:app/eplmp-server-ejb/BinaryStorageManagerBean!org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal";
    private static final String PRODUCT_MANAGER = "java:app/eplmp-server-ejb/ProductManagerBean!org.polarsys.eplmp.core.services.IProductManagerLocal";
    private static final String PRODUCT_INSTANCE_MANAGER = "java:app/eplmp-server-ejb/ProductInstanceManagerBean!org.polarsys.eplmp.core.services.IProductInstanceManagerLocal";
    private static final String USER_MANAGER = "java:app/eplmp-server-ejb/UserManagerBean!org.polarsys.eplmp.core.services.IUserManagerLocal";
    private static final String SHARE_MANAGER = "java:app/eplmp-server-ejb/ShareManagerBean!org.polarsys.eplmp.core.services.IShareManagerLocal";
    private static final String PART_WORKFLOW_MANAGER = "java:app/eplmp-server-ejb/PartWorkflowManagerBean!org.polarsys.eplmp.core.services.IPartWorkflowManagerLocal";
    private static final String DOCUMENT_WORKFLOW_MANAGER = "java:app/eplmp-server-ejb/DocumentWorkflowManagerBean!org.polarsys.eplmp.core.services.IDocumentWorkflowManagerLocal";
    private static final String CASCADE_ACTION_MANAGER = "java:app/eplmp-server-ejb/CascadeActionManagerBean!org.polarsys.eplmp.core.services.ICascadeActionManagerLocal";
    private static final String LOV_MANAGER = "java:app/eplmp-server-ejb/LOVManagerBean!org.polarsys.eplmp.core.services.ILOVManagerLocal";

    private static final Logger LOGGER = Logger.getLogger(ServiceLocator.class.getName());

    private Context context;

    @PostConstruct
    private void init() {
        try {
            context = new InitialContext();
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @InternalService
    @Produces
    public IProductManagerLocal findProductManager() throws NamingException {
        return (IProductManagerLocal) context.lookup(PRODUCT_MANAGER);
    }

    @InternalService
    @Produces
    public IBinaryStorageManagerLocal findStorageManager() throws NamingException {
        return (IBinaryStorageManagerLocal) context.lookup(STORAGE_MANAGER);
    }

    @InternalService
    @Produces
    public IProductInstanceManagerLocal findProductInstanceManager() throws NamingException {
        return (IProductInstanceManagerLocal) context.lookup(PRODUCT_INSTANCE_MANAGER);
    }

    @InternalService
    @Produces
    public IUserManagerLocal findUserManager() throws NamingException {
        return (IUserManagerLocal) context.lookup(USER_MANAGER);
    }

    @InternalService
    @Produces
    public IShareManagerLocal findShareManager() throws NamingException {
        return (IShareManagerLocal) context.lookup(SHARE_MANAGER);
    }

    @InternalService
    @Produces
    public IPartWorkflowManagerLocal findPartWorkflowManager() throws NamingException {
        return (IPartWorkflowManagerLocal) context.lookup(PART_WORKFLOW_MANAGER);
    }

    @InternalService
    @Produces
    public IDocumentWorkflowManagerLocal findDocumentWorkflowManager() throws NamingException {
        return (IDocumentWorkflowManagerLocal) context.lookup(DOCUMENT_WORKFLOW_MANAGER);
    }

    @InternalService
    @Produces
    public ICascadeActionManagerLocal findCascadeActionManager() throws NamingException {
        return (ICascadeActionManagerLocal) context.lookup(CASCADE_ACTION_MANAGER);
    }

    @InternalService
    @Produces
    public ILOVManagerLocal findLOVManager() throws NamingException {
        return (ILOVManagerLocal) context.lookup(LOV_MANAGER);
    }

}
