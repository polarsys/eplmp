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
import java.util.HashSet;
import java.util.Set;

/**
 * A directive to implement an approved {@link ChangeRequest}.
 *
 * @author Florent Garin
 * @version 2.0, 09/01/14
 * @since V2.0
 */
@Table(name="CHANGEORDER")
@Entity
@AssociationOverrides({
        @AssociationOverride(
                name="tags",
                joinTable = @JoinTable(name="CHANGEORDER_TAG",
                        inverseJoinColumns={
                                @JoinColumn(name="TAG_LABEL", referencedColumnName="LABEL"),
                                @JoinColumn(name="TAG_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
                        },
                        joinColumns={
                                @JoinColumn(name="CHANGEORDER_ID", referencedColumnName="ID")
                        }
                )
        ),
        @AssociationOverride(
                name="affectedDocuments",
                joinTable = @JoinTable(name="CHANGEORDER_AFFECTED_DOCUMENT",
                        inverseJoinColumns={
                                @JoinColumn(name="DOCUMENTMASTER_ID", referencedColumnName="DOCUMENTMASTER_ID"),
                                @JoinColumn(name="DOCUMENTREVISION_VERSION", referencedColumnName="DOCUMENTREVISION_VERSION"),
                                @JoinColumn(name="DOCUMENTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
                                @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
                        },
                        joinColumns={
                                @JoinColumn(name="CHANGEORDER_ID", referencedColumnName="ID")
                        }
                )
        ),
        @AssociationOverride(
                name="affectedParts",
                joinTable = @JoinTable(name="CHANGEORDER_AFFECTED_PART",
                        inverseJoinColumns={
                                @JoinColumn(name="PARTMASTER_PARTNUMBER", referencedColumnName="PARTMASTER_PARTNUMBER"),
                                @JoinColumn(name="PARTREVISION_VERSION", referencedColumnName="PARTREVISION_VERSION"),
                                @JoinColumn(name="PARTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
                                @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
                        },
                        joinColumns={
                                @JoinColumn(name="CHANGEORDER_ID", referencedColumnName="ID")
                        }
                )
        )
})
@NamedQueries({
        @NamedQuery(name="ChangeOrder.findChangeOrdersByWorkspace",query="SELECT DISTINCT c FROM ChangeOrder c WHERE c.workspace.id = :workspaceId"),
        @NamedQuery(name="ChangeOrder.countOrderByMilestonesAndWorkspace",query="SELECT COUNT(o) FROM ChangeOrder o WHERE o.workspace.id = :workspaceId AND o.milestone.id = :milestoneId"),
        @NamedQuery(name="ChangeOrder.getOrderByMilestonesAndWorkspace",query="SELECT DISTINCT o FROM ChangeOrder o WHERE o.workspace.id = :workspaceId AND o.milestone.id = :milestoneId"),
        @NamedQuery(name="ChangeOrder.findByReference", query="SELECT c FROM ChangeOrder c WHERE c.name LIKE :name AND c.workspace.id = :workspaceId"),
        @NamedQuery(name="ChangeOrder.findByChangeRequest", query="SELECT c FROM ChangeOrder c WHERE c.workspace.id = :workspaceId AND :changeRequest member of c.addressedChangeRequests")
})
public class ChangeOrder extends ChangeItem {


    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="CHANGEORDER_CHANGEREQUEST",
            inverseJoinColumns={
                    @JoinColumn(name="CHANGEREQUEST_ID", referencedColumnName="ID")
            },
            joinColumns={
                    @JoinColumn(name="CHANGEORDER_ID", referencedColumnName="ID")
            })
    private Set<ChangeRequest> addressedChangeRequests=new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Milestone milestone;

    public ChangeOrder() {
    }

    public ChangeOrder(Workspace pWorkspace, String pName, User pAuthor) {
        super(pWorkspace, pName, pAuthor);
    }

    public ChangeOrder(String name, Workspace workspace, User author, User assignee, Date creationDate, String description, ChangeItemPriority priority, ChangeItemCategory category, Milestone milestone) {
        super(name, workspace, author, assignee, creationDate, description, priority, category);
        this.milestone = milestone;
    }

    public Milestone getMilestone() {
        return milestone;
    }
    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Set<ChangeRequest> getAddressedChangeRequests() {
        return addressedChangeRequests;
    }
    public void setAddressedChangeRequests(Set<ChangeRequest> addressedChangeRequests) {
        this.addressedChangeRequests = addressedChangeRequests;
    }

    public int getMilestoneId(){
        return (milestone!=null) ? milestone.getId() : -1;
    }
}
