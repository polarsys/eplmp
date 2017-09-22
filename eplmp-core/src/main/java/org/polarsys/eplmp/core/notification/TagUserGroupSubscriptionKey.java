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

package org.polarsys.eplmp.core.notification;

import org.polarsys.eplmp.core.common.UserGroupKey;
import org.polarsys.eplmp.core.meta.TagKey;

import java.io.Serializable;

/**
 * Identity class of {@link TagUserGroupSubscription} objects.
 *
 * @author Florent Garin on 12/09/16.
 */
public class TagUserGroupSubscriptionKey implements Serializable{

    private UserGroupKey groupSubscriber;
    private TagKey tag;

    public TagUserGroupSubscriptionKey(){

    }

    public TagUserGroupSubscriptionKey(String pWorkspaceId, String pId, String pLabel) {
        this(new UserGroupKey(pWorkspaceId, pId), new TagKey(pWorkspaceId, pLabel));
    }
    public TagUserGroupSubscriptionKey(UserGroupKey groupSubscriber, TagKey tag) {
        this.groupSubscriber = groupSubscriber;
        this.tag = tag;
    }

    public UserGroupKey getGroupSubscriber() {
        return groupSubscriber;
    }

    public void setGroupSubscriber(UserGroupKey groupSubscriber) {
        this.groupSubscriber = groupSubscriber;
    }

    public TagKey getTag() {
        return tag;
    }

    public void setTag(TagKey tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagUserGroupSubscriptionKey that = (TagUserGroupSubscriptionKey) o;

        if (groupSubscriber != null ? !groupSubscriber.equals(that.groupSubscriber) : that.groupSubscriber != null) return false;
        return !(tag != null ? !tag.equals(that.tag) : that.tag != null);

    }

    @Override
    public int hashCode() {
        int result = groupSubscriber != null ? groupSubscriber.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
