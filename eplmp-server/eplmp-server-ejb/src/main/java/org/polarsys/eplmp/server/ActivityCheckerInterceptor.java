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

import org.polarsys.eplmp.core.services.ITaskManagerLocal;
import org.polarsys.eplmp.core.services.IUserManagerLocal;
import org.polarsys.eplmp.core.workflow.TaskKey;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@CheckActivity
@Interceptor
public class ActivityCheckerInterceptor {

    @Inject
    private ITaskManagerLocal taskManager;

    @AroundInvoke
    public Object check(InvocationContext ctx) throws Exception {
        String workspaceId = (String) ctx.getParameters()[0];
        TaskKey taskKey = (TaskKey) ctx.getParameters()[1];
        taskManager.checkTask(workspaceId, taskKey);
        return ctx.proceed();
    }
}
