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

package org.polarsys.eplmp.core.admin;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Class that holds setting options of the whole system (platform level).
 * Only one instance of the class must exist.
 *
 * @author Morgan Guimard
 * @version 2.5, 02/06/16
 * @since V2.5
 */
@Table(name = "PLATFORMOPTIONS")
@Entity
public class PlatformOptions implements Serializable {

    public static final int UNIQUE_ID = 1;

    @Id
    private int id = UNIQUE_ID;

    private OperationSecurityStrategy registrationStrategy;

    private OperationSecurityStrategy workspaceCreationStrategy;

    public PlatformOptions() {
    }

    public int getId() {
        return id;
    }

    public OperationSecurityStrategy getRegistrationStrategy() {
        return registrationStrategy;
    }

    public void setRegistrationStrategy(OperationSecurityStrategy registrationStrategy) {
        this.registrationStrategy = registrationStrategy;
    }

    public OperationSecurityStrategy getWorkspaceCreationStrategy() {
        return workspaceCreationStrategy;
    }

    public void setWorkspaceCreationStrategy(OperationSecurityStrategy workspaceCreationStrategy) {
        this.workspaceCreationStrategy = workspaceCreationStrategy;
    }

    public void setDefaults() {
        registrationStrategy = OperationSecurityStrategy.NONE;
        workspaceCreationStrategy = OperationSecurityStrategy.NONE;
    }

}
