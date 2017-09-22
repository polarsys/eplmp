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

import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.security.ACL;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A milestone acts like a container for change items.
 * This is useful for associating changes with specific features or project phases.
 *
 * @author Florent Garin
 * @version 2.0, 05/02/14
 * @since V2.0
 */
@Table(name = "MILESTONE")
@javax.persistence.Entity
@NamedQueries({
        @NamedQuery(name = "Milestone.findMilestonesByWorkspace", query = "SELECT DISTINCT m FROM Milestone m WHERE m.workspace.id = :workspaceId"),
        @NamedQuery(name = "Milestone.findMilestonesByTitleAndWorkspace", query = "SELECT DISTINCT m FROM Milestone m WHERE m.workspace.id = :workspaceId AND m.title = :title")
})
public class Milestone implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String title = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    @Lob
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ACL acl;

    public Milestone() {
    }

    public Milestone(Workspace pWorkspace, String pTitle) {
        setWorkspace(pWorkspace);
        title = pTitle;
    }

    public Milestone(String title, Date dueDate, String description, Workspace workspace) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.workspace = workspace;
    }

    public int getId() {
        return id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getWorkspaceId() {
        return workspace.getId();
    }

    public void setWorkspace(Workspace pWorkspace) {
        workspace = pWorkspace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ACL getACL() {
        return acl;
    }

    public void setACL(ACL acl) {
        this.acl = acl;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Milestone))
            return false;
        Milestone milestone = (Milestone) pObj;

        return milestone.id == id;
    }

    @Override
    public String toString() {
        return title;
    }
}
