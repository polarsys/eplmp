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
 * The PartLog class represents an entry in the log
 * table that keeps track of all activities around a specific part.
 * 
 * @author Florent Garin
 * @version 1.1, 12/03/13
 * @since   V1.1
 */
@Table(name="PARTLOG")
@javax.persistence.Entity
@NamedQueries ({
    @NamedQuery(name="findLogByPartAndUserAndEvent", query="SELECT l FROM PartLog l WHERE l.userLogin = :userLogin AND l.partWorkspaceId = :partWorkspaceId AND l.partNumber = :partNumber AND l.partVersion = :partVersion AND l.partIteration = :partIteration AND l.event = :event ORDER BY l.logDate")
})
public class PartLog implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date logDate;
    private String partWorkspaceId;
    private String partNumber;
    private String partVersion;
    private int partIteration;
    private String userLogin;
    private String event;
    private String info;


    public PartLog() {
    }

    public int getId() {
        return id;
    }

    public int getPartIteration() {
        return partIteration;
    }

    public void setPartIteration(int partIteration) {
        this.partIteration = partIteration;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartVersion() {
        return partVersion;
    }

    public void setPartVersion(String partVersion) {
        this.partVersion = partVersion;
    }

    public String getPartWorkspaceId() {
        return partWorkspaceId;
    }

    public void setPartWorkspaceId(String partWorkspaceId) {
        this.partWorkspaceId = partWorkspaceId;
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
