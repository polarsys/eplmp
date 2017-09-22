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
package org.polarsys.eplmp.core.log;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The WorkspaceLog class represents an entry in the log
 * table that keeps track of all activities around a specific workspace.
 * 
 * @author Florent Garin
 * @version 1.1, 12/03/13
 * @since   V1.1
 */
@Table(name="WORKSPACELOG")
@javax.persistence.Entity
@NamedQueries ({
    @NamedQuery(name="findLogByWorkspaceAndUserAndEvent", query="SELECT l FROM WorkspaceLog l WHERE l.userLogin = :userLogin AND l.workspaceId = :workspaceId AND l.event = :event ORDER BY l.logDate")
})
public class WorkspaceLog implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date logDate;
    private String workspaceId;
    private String userLogin;
    private String event;
    private String info;


    public WorkspaceLog() {
    }

    public int getId() {
        return id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String partWorkspaceId) {
        this.workspaceId = partWorkspaceId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    
}
