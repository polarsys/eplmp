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
package org.polarsys.eplmp.server.events;

import org.polarsys.eplmp.core.common.User;

/**
 * @author Morgan Guimard
 */
public class UserEvent {

    private User observedUser;

    public UserEvent(User observedUser) {
        this.observedUser = observedUser;
    }

    public User getObservedUser() {
        return observedUser;
    }

    public void setObservedUser(User observedUser) {
        this.observedUser = observedUser;
    }
}
