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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.admin.OperationSecurityStrategy;
import org.polarsys.eplmp.core.admin.PlatformOptions;

/**
 *
 * @author Morgan Guimard
 * @version 2.5, 03/02/16
 * @since   V2.5
 */
public interface IPlatformOptionsManagerLocal {
    OperationSecurityStrategy getWorkspaceCreationStrategy();
    OperationSecurityStrategy getRegistrationStrategy();
    void setWorkspaceCreationStrategy(OperationSecurityStrategy strategy);
    void setRegistrationStrategy(OperationSecurityStrategy strategy);
    PlatformOptions getPlatformOptions();
}
