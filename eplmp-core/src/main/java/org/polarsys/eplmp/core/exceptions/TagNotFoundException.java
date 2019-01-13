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

import org.polarsys.eplmp.core.meta.TagKey;

import java.text.MessageFormat;


/**
 *
 * @author Florent Garin
 */
public class TagNotFoundException extends EntityNotFoundException {
    private final TagKey mTagKey;
    
    public TagNotFoundException(String pMessage) {
        super(pMessage);
        mTagKey=null;
    }
    
    public TagNotFoundException(TagKey pTagKey) {
        super();
        mTagKey=pTagKey;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mTagKey);     
    }
}
