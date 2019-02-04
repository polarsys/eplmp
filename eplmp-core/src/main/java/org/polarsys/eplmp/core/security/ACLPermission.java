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

package org.polarsys.eplmp.core.security;

/**
 * Enumeration for all kind of permissions assignable to users or groups.
 *
 * @author Morgan Guimard
 * @version 1.1, 17/07/09
 * @since   V1.1
 */
public enum ACLPermission {
    FORBIDDEN,
    READ_ONLY,
    FULL_ACCESS
}
