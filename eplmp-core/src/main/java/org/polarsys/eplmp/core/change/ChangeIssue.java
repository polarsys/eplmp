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

package org.polarsys.eplmp.core.change;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents an identified issue.
 * The issue may result in one or more {@link ChangeRequest}.
 *
 * @author Florent Garin
 * @version 2.0, 06/01/14
 * @since V2.0
 */
@Table(name="CHANGEISSUE")
@Entity
@AssociationOverrides({
        @AssociationOverride(
            name="tags",
            joinTable = @JoinTable(name="CHANGEISSUE_TAG",
                                    inverseJoinColumns={
                                            @JoinColumn(name="TAG_LABEL", referencedColumnName="LABEL"),
                                            @JoinColumn(name="TAG_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
                                    },
                                    joinColumns={
                                            @JoinColumn(name="CHANGEISSUE_ID", referencedColumnName="ID")
                                    }
            )
        ),
        @AssociationOverride(
            name="affectedDocuments",
            joinTable = @JoinTable(name="CHANGEISSUE_AFFECTED_DOCUMENT",
                                    inverseJoinColumns={
                                            @JoinColumn(name="DOCUMENTMASTER_ID", referencedColumnName="DOCUMENTMASTER_ID"),
                                            @JoinColumn(name="DOCUMENTREVISION_VERSION", referencedColumnName="DOCUMENTREVISION_VERSION"),
                                            @JoinColumn(name="DOCUMENTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
                                            @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
                                    },
                                    joinColumns={
                                            @JoinColumn(name="CHANGEISSUE_ID", referencedColumnName="ID")
                                    }
            )
        ),
        @AssociationOverride(
            name="affectedParts",
            joinTable = @JoinTable(name="CHANGEISSUE_AFFECTED_PART",
                                    inverseJoinColumns={
                                            @JoinColumn(name="PARTMASTER_PARTNUMBER", referencedColumnName="PARTMASTER_PARTNUMBER"),
                                            @JoinColumn(name="PARTREVISION_VERSION", referencedColumnName="PARTREVISION_VERSION"),
                                            @JoinColumn(name="PARTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
                                            @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
                                    },
                                    joinColumns={
                                            @JoinColumn(name="CHANGEISSUE_ID", referencedColumnName="ID")
                                    }
            )
        )
})
@NamedQueries({
        @NamedQuery(name="ChangeIssue.findChangeIssuesByWorkspace",query="SELECT DISTINCT c FROM ChangeIssue c WHERE c.workspace.id = :workspaceId"),
        @NamedQuery(name="ChangeIssue.findByName", query="SELECT c FROM ChangeIssue c WHERE c.name LIKE :name AND c.workspace.id = :workspaceId")
})
public class ChangeIssue extends ChangeItem {

    /**
     * Identifies the person or organization at the origin of the change, may be null
     * if it is the user who created the object.
     */
    private String initiator;

    public ChangeIssue() {
    }

    public ChangeIssue(Workspace pWorkspace, String pName, User pAuthor) {
        super(pWorkspace, pName, pAuthor);
    }

    public ChangeIssue(String name, Workspace workspace, User author, User assignee, Date creationDate, String description, ChangeItemPriority priority, ChangeItemCategory category, String initiator) {
        super(name, workspace, author, assignee, creationDate, description, priority, category);
        this.initiator = initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getInitiator() {
        return initiator;
    }
}
