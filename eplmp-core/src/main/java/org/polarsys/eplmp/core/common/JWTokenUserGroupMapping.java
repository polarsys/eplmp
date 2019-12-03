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

package org.polarsys.eplmp.core.common;

import org.jose4j.jwt.JwtClaims;
import org.polarsys.eplmp.core.security.UserGroupMapping;

/**
 * This JWTokenUserGroupMapping class wraps tokens decoded data
 *
 * @author Morgan Guimard
 */
public class JWTokenUserGroupMapping {
    private JwtClaims claims;
    private UserGroupMapping userGroupMapping;

    public JWTokenUserGroupMapping(JwtClaims claims, UserGroupMapping userGroupMapping) {
        this.claims = claims;
        this.userGroupMapping = userGroupMapping;
    }

    public JwtClaims getClaims() {
        return claims;
    }

    public UserGroupMapping getUserGroupMapping() {
        return userGroupMapping;
    }
}
