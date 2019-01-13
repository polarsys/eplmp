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

package org.polarsys.eplmp.core.admin;

/**
 * Setting which indicates if the super-admin have to valid account and workspace creations.
 *
 * @author Morgan Guimard
 * @version 2.5, 02/06/16
 * @since V2.5
 */
public enum OperationSecurityStrategy {
    NONE, ADMIN_VALIDATION
}
