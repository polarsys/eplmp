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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.JWTokenUserGroupMapping;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.sharing.SharedEntity;

import javax.servlet.http.HttpServletResponse;
import java.security.Key;

/**
 * @author Morgan Guimard
 */
public interface ITokenManagerLocal {
    String createAuthToken(Key key, UserGroupMapping userGroupMapping);

    String createSharedEntityToken(Key key, SharedEntity sharedEntity) ;

    JWTokenUserGroupMapping validateAuthToken(Key key, String jwt);

    String validateSharedResourceToken(Key key, String jwt) ;

    boolean isJWTValidBefore(Key key, int seconds, String authorizationString) ;

    void refreshTokenIfNeeded(Key key, HttpServletResponse response, JWTokenUserGroupMapping jwTokenUserGroupMapping) ;

    String createEntityToken(Key key, String entityKey);

    String validateEntityToken(Key key, String jwt) ;
}
