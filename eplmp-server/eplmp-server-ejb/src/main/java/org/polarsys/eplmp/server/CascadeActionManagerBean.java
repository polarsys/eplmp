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

import org.polarsys.eplmp.core.configuration.CascadeResult;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;
import org.polarsys.eplmp.core.product.PartRevision;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.ICascadeActionManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Charles Fallourd on 10/02/16.
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID, UserGroupMapping.ADMIN_ROLE_ID})
@Local(ICascadeActionManagerLocal.class)
@Stateless(name = "CascadeActionManagerBean")
public class CascadeActionManagerBean implements ICascadeActionManagerLocal {

    private static final Logger LOGGER = Logger.getLogger(ProductManagerBean.class.getName());

    @EJB
    private IProductManagerLocal productManager;

    //Every action should be transactional
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public CascadeResult cascadeCheckOut(ConfigurationItemKey configurationItemKey, String path) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, EntityConstraintException, PartMasterNotFoundException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException {
        CascadeResult cascadeResult = new CascadeResult();

        Set<PartRevision> partRevisions = productManager.getWritablePartRevisionsFromPath(configurationItemKey,path);

        for(PartRevision pr : partRevisions) {
            try {
                productManager.checkOutPart(pr.getKey());
                cascadeResult.incSucceedAttempts();
            } catch (PartRevisionNotFoundException | AccessRightException  | NotAllowedException | FileAlreadyExistsException | CreationException e) {
                cascadeResult.incFailedAttempts();
                LOGGER.log(Level.SEVERE,null,e);
            }
        }
        return cascadeResult;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public CascadeResult cascadeUndoCheckOut(ConfigurationItemKey configurationItemKey, String path) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, NotAllowedException, EntityConstraintException, PartMasterNotFoundException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException {
        CascadeResult cascadeResult = new CascadeResult();
        Set<PartRevision> partRevisions = productManager.getWritablePartRevisionsFromPath(configurationItemKey, path);
        for(PartRevision pr : partRevisions) {
            try {
                productManager.undoCheckOutPart(pr.getKey());
                cascadeResult.incSucceedAttempts();
            } catch (PartRevisionNotFoundException | AccessRightException  | NotAllowedException  e) {
                cascadeResult.incFailedAttempts();
                LOGGER.log(Level.SEVERE,null,e);
            }
        }
        return cascadeResult;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    @RolesAllowed(UserGroupMapping.REGULAR_USER_ROLE_ID)
    public CascadeResult cascadeCheckIn(ConfigurationItemKey configurationItemKey, String path, String iterationNote) throws UserNotFoundException, UserNotActiveException, WorkspaceNotFoundException, PartMasterNotFoundException, EntityConstraintException, NotAllowedException, PartUsageLinkNotFoundException, ConfigurationItemNotFoundException, WorkspaceNotEnabledException {

        CascadeResult cascadeResult = new CascadeResult();
        Set<PartRevision> partRevisions = productManager.getWritablePartRevisionsFromPath(configurationItemKey, path);
        for(PartRevision pr : partRevisions) {
            try {
                // Set the iteration note only if param is set and part has no iteration note
                if( (iterationNote != null && iterationNote.isEmpty()) && (null == pr.getLastIteration().getIterationNote() && pr.getLastIteration().getIterationNote().isEmpty())){
                    productManager.updatePartIteration(pr.getLastIteration().getKey(), iterationNote, null, null, null, null, null, null, null);
                }

                productManager.checkInPart(pr.getKey());
                cascadeResult.incSucceedAttempts();

            } catch (DocumentRevisionNotFoundException | PartRevisionNotFoundException | AccessRightException  | NotAllowedException | ListOfValuesNotFoundException e) {
                cascadeResult.incFailedAttempts();
                LOGGER.log(Level.SEVERE,null,e);
            }
        }
        return cascadeResult;
    }
}
