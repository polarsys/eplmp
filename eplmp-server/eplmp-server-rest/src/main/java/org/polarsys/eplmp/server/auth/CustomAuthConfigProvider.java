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

package org.polarsys.eplmp.server.auth;

import org.polarsys.eplmp.server.config.AuthConfig;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

/**
 * Config provider to register for custom authentication
 *
 * @author Morgan Guimard
 */
public class CustomAuthConfigProvider implements AuthConfigProvider {

    private final AuthConfig authConfig;

    public CustomAuthConfigProvider(AuthConfig authConfig) {
       this.authConfig = authConfig;
    }

    @Override
    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler callbackHandler) throws AuthException {
        return new CustomServerAuthConfig(authConfig, layer, appContext, callbackHandler);
    }

    @Override
    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler callbackHandler) throws AuthException {
        return null;
    }

    @Override
    public void refresh() {

    }
}
