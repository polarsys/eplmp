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
 * The DocumentLog class represents an entry in the log
 * table that keeps track of all activities around a specific document.
 * 
 * @author Florent Garin
 * @version 1.1, 22/09/11
 * @since   V1.1
 */
@Table(name="DOCUMENTLOG")
@javax.persistence.Entity
@NamedQueries ({
    @NamedQuery(name="findLogByDocumentAndUserAndEvent", query="SELECT l FROM DocumentLog l WHERE l.userLogin = :userLogin AND l.documentWorkspaceId = :documentWorkspaceId AND l.documentId = :documentId AND l.documentVersion = :documentVersion AND l.documentIteration = :documentIteration AND l.event = :event ORDER BY l.logDate")
})
public class DocumentLog implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date logDate;
    private String documentWorkspaceId;
    private String documentId;
    private String documentVersion;
    private int documentIteration;
    private String userLogin;
    private String event;
    private String info;
    
    
    public DocumentLog() {
    }

    public int getId() { return id; }

    public int getDocumentIteration() {
        return documentIteration;
    }

    public void setDocumentIteration(int documentIteration) {
        this.documentIteration = documentIteration;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public String getDocumentWorkspaceId() {
        return documentWorkspaceId;
    }

    public void setDocumentWorkspaceId(String documentWorkspaceId) {
        this.documentWorkspaceId = documentWorkspaceId;
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
