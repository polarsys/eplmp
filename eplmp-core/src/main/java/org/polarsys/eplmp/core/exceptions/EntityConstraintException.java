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


import java.util.Locale;

/**
 *
 * @author Morgan Guimard
 */
public class EntityConstraintException extends ApplicationException{
    private final String mKey;

    public EntityConstraintException(String pMessage) {
        super(pMessage);
        mKey=null;
    }

    public EntityConstraintException(Locale pLocale, String pKey) {
        super(pLocale);
        mKey=pKey;
    }

    @Override
    public String getLocalizedMessage() {
        return mKey==null?null:getBundleMessage(mKey);
    }

}

