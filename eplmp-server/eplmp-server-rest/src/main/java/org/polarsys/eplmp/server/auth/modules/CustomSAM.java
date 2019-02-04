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

package org.polarsys.eplmp.server.auth.modules;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Abstract class for every authentication modules
 *
 * @author Morgan Guimard
 */
public abstract class CustomSAM implements ServerAuthModule {

    private static final Class[] supportedMessageTypes = new Class[]{HttpServletRequest.class};
    protected CallbackHandler callbackHandler;

    @Override
    public void initialize(MessagePolicy messagePolicy, MessagePolicy messagePolicy1, CallbackHandler callbackHandler, Map map) throws AuthException {
        this.callbackHandler = callbackHandler;
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }


    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject subject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
    }

    public abstract boolean canHandle(MessageInfo messageInfo);

}
