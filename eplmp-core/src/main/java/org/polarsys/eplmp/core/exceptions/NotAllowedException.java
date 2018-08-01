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

package org.polarsys.eplmp.core.exceptions;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class NotAllowedException extends ApplicationException {
    private final String mKey;
    private final String mName;

    public NotAllowedException(String pKey) {
        super();
        mKey=pKey;
        mName=null;
    }

    public NotAllowedException(String pKey, String pName) {
        super();
        mKey=pKey;
        mName=pName;
    }

    @Override
    public String getLocalizedMessage() {
        if (mKey == null) {
            return null;
        }

        String message = getBundleMessage(mKey);
        return MessageFormat.format(message, mName);
    }
}
