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
 * @author Taylor Labejof
 */
public class MilestoneNotFoundException extends EntityNotFoundException {
    private final int mChange;
    private final String mTitle;

    public MilestoneNotFoundException(int pChange) {
        this(pChange, null);
    }

    public MilestoneNotFoundException(int pChange, Throwable pCause) {
        super(pCause);
        mChange =pChange;
        mTitle=null;
    }

    public MilestoneNotFoundException(String pTitle) {
        super();
        mTitle = pTitle;
        mChange = -1;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        if(mTitle!=null){
            return MessageFormat.format(message, mTitle);
        }
        return MessageFormat.format(message, "N° "+mChange);
    }
}
