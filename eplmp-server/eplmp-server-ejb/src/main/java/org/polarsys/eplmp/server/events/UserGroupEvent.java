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
package org.polarsys.eplmp.server.events;

import org.polarsys.eplmp.core.common.UserGroup;

/**
 * @author Florent Garin
 */
public class UserGroupEvent {

    private UserGroup observedUserGroup;

    public UserGroupEvent(UserGroup observedUserGroup) {
        this.observedUserGroup = observedUserGroup;
    }

    public UserGroup getObservedUserGroup() {
        return observedUserGroup;
    }

    public void setObservedUserGroup(UserGroup observedUserGroup) {
        this.observedUserGroup = observedUserGroup;
    }
}
