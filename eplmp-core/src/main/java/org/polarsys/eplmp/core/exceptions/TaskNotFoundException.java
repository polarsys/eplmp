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

import org.polarsys.eplmp.core.workflow.TaskKey;

import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author Florent Garin
 */
public class TaskNotFoundException extends EntityNotFoundException {
    private final TaskKey mTaskKey;
    
    public TaskNotFoundException(String pMessage) {
        super(pMessage);
        mTaskKey=null;
    }
    
    public TaskNotFoundException(Locale pLocale, TaskKey pTaskKey) {
        super(pLocale);
        mTaskKey=pTaskKey;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mTaskKey);     
    }
}
