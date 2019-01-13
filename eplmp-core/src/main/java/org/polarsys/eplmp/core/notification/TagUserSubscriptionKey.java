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

package org.polarsys.eplmp.core.notification;

import org.polarsys.eplmp.core.common.UserKey;
import org.polarsys.eplmp.core.meta.TagKey;

import java.io.Serializable;

/**
 * Identity class of {@link TagUserSubscription} objects.
 *
 * @author Florent Garin on 12/09/16.
 */
public class TagUserSubscriptionKey implements Serializable{

    private UserKey userSubscriber;
    private TagKey tag;

    public TagUserSubscriptionKey(){

    }

    public TagUserSubscriptionKey(String pWorkspaceId, String pLogin, String pLabel) {
        this(new UserKey(pWorkspaceId, pLogin), new TagKey(pWorkspaceId, pLabel));
    }

    public TagUserSubscriptionKey(UserKey userSubscriber, TagKey tag) {
        this.userSubscriber = userSubscriber;
        this.tag = tag;
    }

    public UserKey getUserSubscriber() {
        return userSubscriber;
    }

    public void setUserSubscriber(UserKey userSubscriber) {
        this.userSubscriber = userSubscriber;
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

        TagUserSubscriptionKey that = (TagUserSubscriptionKey) o;

        if (userSubscriber != null ? !userSubscriber.equals(that.userSubscriber) : that.userSubscriber != null) return false;
        return !(tag != null ? !tag.equals(that.tag) : that.tag != null);

    }

    @Override
    public int hashCode() {
        int result = userSubscriber != null ? userSubscriber.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
