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

import org.polarsys.eplmp.core.product.Import;
import org.polarsys.eplmp.core.product.ImportResult;
import org.polarsys.eplmp.core.services.IProductManagerLocal;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@FileImport
@Interceptor
public class FileImportInterceptor {

    @Inject
    private IProductManagerLocal productService;

    private static final Logger LOGGER = Logger.getLogger(FileImportInterceptor.class.getName());

    @AroundInvoke
    // Safe cast, ignore warning
    @SuppressWarnings("unchecked")
    public Object createImport(InvocationContext ctx) throws Exception {

        Object[] parameters = ctx.getParameters();
        // TODO : check parameters before cast
        String workspaceId = (String) parameters[0];
        File file = (File) parameters[1];
        String originalFileName = (String) parameters[2];

        Import newImport = productService.createImport(workspaceId, originalFileName);
        String id = newImport.getId();

        ImportResult importResult = null;

        try { // Run the import

            Object proceed = ctx.proceed();
            Future<ImportResult> result = (Future<ImportResult>) proceed;
            importResult = result.get();
            return proceed;

        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Cannot import the file", e);
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            errors.add("Unhandled exception");
            importResult = new ImportResult(file, warnings, errors);
            return null;

        } finally {
            productService.endImport(workspaceId, id, importResult);
        }

    }


}
