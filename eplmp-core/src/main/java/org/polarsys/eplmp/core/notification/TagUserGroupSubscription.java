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

import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.meta.Tag;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Subscription based on tag and applicable to a {@link UserGroup}.
 * Thus each time an item (document, part...)
 * is tagged or untagged with the monitored tag a notification is sent
 * to all members of the user group.
 *
 * In addition to these notifications, optionally the group can also
 * be informed when the targeted item has changed (a new iteration has been
 * created) and/or its state has evolved.
 * 
 * @author Florent Garin
 * @version 2.5, 06/09/16
 * @since   V2.5
 */
@Table(name="TAGUSERGROUPSUBSCRIPTION")
@IdClass(TagUserGroupSubscriptionKey.class)
@NamedQueries({
        @NamedQuery(name="TagUserGroupSubscription.findIterationChangeSubscribersByTags", query="SELECT distinct(u) FROM TagUserGroupSubscription s JOIN s.groupSubscriber g JOIN g.users u WHERE s.tag.workspaceId = :workspaceId AND s.onIterationChange = true AND s.tag.label IN :tags"),
        @NamedQuery(name="TagUserGroupSubscription.findStateChangeSubscribersByTags", query="SELECT distinct(u) FROM TagUserGroupSubscription s JOIN s.groupSubscriber g JOIN g.users u WHERE s.tag.workspaceId = :workspaceId AND s.onStateChange = true AND s.tag.label IN :tags"),
        @NamedQuery(name="TagUserGroupSubscription.findSubscribersByTags", query="SELECT distinct(u) FROM TagUserGroupSubscription s JOIN s.groupSubscriber g JOIN g.users u WHERE s.tag.workspaceId = :workspaceId AND s.tag.label IN :tags"),
        @NamedQuery(name="TagUserGroupSubscription.findTagUserGroupSubscriptionsByGroup", query="SELECT s FROM TagUserGroupSubscription s WHERE s.groupSubscriber = :groupSubscriber"),
        @NamedQuery(name="TagUserGroupSubscription.deleteTagUserGroupSubscriptionsFromTag", query="DELETE FROM TagUserGroupSubscription s WHERE s.tag = :tag"),
        @NamedQuery(name="TagUserGroupSubscription.deleteTagUserGroupSubscriptionsFromGroup", query="DELETE FROM TagUserGroupSubscription s WHERE s.groupSubscriber = :groupSubscriber")
})
@Entity
public class TagUserGroupSubscription implements Serializable{


    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "SUBSCRIBER_ID", referencedColumnName = "ID"),
            @JoinColumn(name = "SUBSCRIBER_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private UserGroup groupSubscriber;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="TAG_LABEL", referencedColumnName="LABEL"),
            @JoinColumn(name="TAG_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
        }
    )
    private Tag tag;

    private boolean onIterationChange;
    private boolean onStateChange;


    public TagUserGroupSubscription() {
    }


    public TagUserGroupSubscription(Tag pTag, UserGroup pSubscriber){
        this(pTag, pSubscriber, false, false);
    }

    public TagUserGroupSubscription(Tag pTag, UserGroup pSubscriber, boolean pOnIterationChange, boolean pOnStateChange){
        setTag(pTag);
        setGroupSubscriber(pSubscriber);
        setOnIterationChange(pOnIterationChange);
        setOnStateChange(pOnStateChange);
    }

    public void setTag(Tag pTag) {
        this.tag = pTag;
    }

    public Tag getTag() {
        return tag;
    }

    public UserGroup getGroupSubscriber() {
        return groupSubscriber;
    }

    public void setGroupSubscriber(UserGroup groupSubscriber) {
        this.groupSubscriber = groupSubscriber;
    }

    public void setOnIterationChange(boolean onIterationChange) {
        this.onIterationChange = onIterationChange;
    }

    public boolean isOnIterationChange() {
        return onIterationChange;
    }

    public void setOnStateChange(boolean onStateChange) {
        this.onStateChange = onStateChange;
    }

    public boolean isOnStateChange() {
        return onStateChange;
    }


}
