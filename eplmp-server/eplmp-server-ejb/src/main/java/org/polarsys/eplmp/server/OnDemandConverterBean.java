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

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.server.converters.OnDemandConverter;
import org.polarsys.eplmp.server.dao.BinaryResourceDAO;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.InputStream;


/**
 * Resource Getter
 */
@Stateless(name = "OnDemandConverterBean")
public class OnDemandConverterBean implements IOnDemandConverterManagerLocal {

    @Inject
    private BinaryResourceDAO binaryResourceDAO;

    @Inject
    @Any
    private Instance<OnDemandConverter> documentResourceGetters;

    @Inject
    private IDocumentManagerLocal documentService;

    @Inject
    private IProductManagerLocal productService;

    @Inject
    private IContextManagerLocal contextManager;

    @Inject
    private IUserManagerLocal userManager;

    @Override
    public InputStream getDocumentConvertedResource(String outputFormat, BinaryResource binaryResource)
            throws WorkspaceNotFoundException, UserNotActiveException, UserNotFoundException, ConvertedResourceException, WorkspaceNotEnabledException {

        DocumentIteration docI = binaryResourceDAO.getDocumentHolder(binaryResource);
        OnDemandConverter selectedOnDemandConverter = selectOnDemandConverter(outputFormat, binaryResource);
        User user = userManager.whoAmI(docI.getWorkspaceId());
        if (selectedOnDemandConverter != null) {
            return selectedOnDemandConverter.getConvertedResource(outputFormat, binaryResource, docI, user.getLocale());
        }

        return null;
    }

    @Override
    public InputStream getPartConvertedResource(String outputFormat, BinaryResource binaryResource)
            throws WorkspaceNotFoundException, UserNotActiveException, UserNotFoundException, ConvertedResourceException, WorkspaceNotEnabledException {

        PartIteration partIteration = binaryResourceDAO.getPartHolder(binaryResource);
        OnDemandConverter selectedOnDemandConverter = selectOnDemandConverter(outputFormat, binaryResource);
        User user = userManager.whoAmI(partIteration.getWorkspaceId());

        if (selectedOnDemandConverter != null) {
            return selectedOnDemandConverter.getConvertedResource(outputFormat, binaryResource, partIteration, user.getLocale());
        }

        return null;
    }

    private OnDemandConverter selectOnDemandConverter(String outputFormat, BinaryResource binaryResource) {
        OnDemandConverter selectedOnDemandConverter = null;
        for (OnDemandConverter onDemandConverter : documentResourceGetters) {
            if (onDemandConverter.canConvert(outputFormat, binaryResource)) {
                selectedOnDemandConverter = onDemandConverter;
                break;
            }
        }
        return selectedOnDemandConverter;
    }
}
