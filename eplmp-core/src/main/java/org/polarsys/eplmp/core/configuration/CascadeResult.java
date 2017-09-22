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

package org.polarsys.eplmp.core.configuration;

import java.io.Serializable;

/**
 * Encapsulates the result of a cascade operation made upon
 * the Product Structure.
 *
 * @author Charles Fallourd
 * @version 2.5, 12/04/16
 * @since   V2.5
 */
public class CascadeResult implements Serializable{

    private int succeedAttempts;
    private int failedAttempts;

    public CascadeResult() {
        this.succeedAttempts = 0;
        this.failedAttempts = 0;
    }

    public void incSucceedAttempts() {
        this.succeedAttempts++;
    }

    public void incFailedAttempts() {
        this.failedAttempts++;
    }

    public int getSucceedAttempts() {
        return succeedAttempts;
    }

    public void setSucceedAttempts(int succeedAttempts) {
        this.succeedAttempts = succeedAttempts;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
