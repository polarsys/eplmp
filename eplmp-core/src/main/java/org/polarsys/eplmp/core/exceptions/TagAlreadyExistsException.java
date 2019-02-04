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

package org.polarsys.eplmp.core.exceptions;

import org.polarsys.eplmp.core.meta.Tag;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class TagAlreadyExistsException extends EntityAlreadyExistsException {
    private final Tag mTag;
    
    
    public TagAlreadyExistsException(String pMessage) {
        super(pMessage);
        mTag=null;
    }

    public TagAlreadyExistsException(Tag pTag) {
        this(pTag, null);
    }

    public TagAlreadyExistsException(Tag pTag, Throwable pCause) {
        super( pCause);
        mTag=pTag;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mTag);
    }
}
