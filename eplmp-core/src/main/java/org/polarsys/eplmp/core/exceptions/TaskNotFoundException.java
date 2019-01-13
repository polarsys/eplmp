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

import org.polarsys.eplmp.core.workflow.TaskKey;

import java.text.MessageFormat;


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
    
    public TaskNotFoundException(TaskKey pTaskKey) {
        super();
        mTaskKey=pTaskKey;
    }

    @Override
    public String getLocalizedMessage() {
        String message = getBundleDefaultMessage();
        return MessageFormat.format(message,mTaskKey);     
    }
}
