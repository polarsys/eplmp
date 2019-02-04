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

import org.polarsys.eplmp.core.common.Workspace;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class that wraps setting options of a particular workspace.
 * These settings are related to back-end concerns.
 *
 * @author Morgan Guimard
 * @version 2.5, 14/09/17
 * @since V2.5
 */
@Table(name = "WORKSPACEBACKOPTIONS")
@Entity
public class WorkspaceBackOptions implements Serializable {

    private static final boolean SEND_EMAILS_DEFAULT = true;

    @Id
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Workspace workspace;


    /**
     * Indicates that the system must not send email notifications.
     * Usually used when using a third notification system connected through
     * webhooks.
     */
    private boolean sendEmails;

    public WorkspaceBackOptions() {
    }

    public WorkspaceBackOptions(Workspace workspace) {
        this.workspace = workspace;
        this.sendEmails = SEND_EMAILS_DEFAULT;
    }

    public WorkspaceBackOptions(Workspace workspace, boolean sendEmails) {
        this.workspace = workspace;
        this.sendEmails = sendEmails;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public boolean isSendEmails() {
        return sendEmails;
    }

    public void setSendEmails(boolean sendEmails) {
        this.sendEmails = sendEmails;
    }

    public String getWorkspaceId() {
        return workspace.getId();
    }

}
