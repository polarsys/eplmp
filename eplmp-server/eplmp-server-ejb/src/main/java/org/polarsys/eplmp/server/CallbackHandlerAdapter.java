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

import javax.security.auth.callback.*;
import java.io.IOException;

public class CallbackHandlerAdapter implements CallbackHandler {

    private String mLogin;
    private char[] mPassword;

    public CallbackHandlerAdapter(String pLogin, char[] pPassword) {
        mLogin = pLogin;
        mPassword = pPassword;
    }

    @Override
    public void handle(Callback[] pCallbacks)
            throws IOException, UnsupportedCallbackException {
        for (Callback pCallback : pCallbacks) {
            if (pCallback instanceof NameCallback) {
                NameCallback nc = (NameCallback) pCallback;
                nc.setName(mLogin);
            } else if (pCallback instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) pCallback;
                pc.setPassword(mPassword);
            } else {
                throw new UnsupportedCallbackException(
                        pCallback,
                        "Unrecognized callback");
            }
        }
    }
}
