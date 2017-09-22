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

package org.polarsys.eplmp.core.product;

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A class that stores the result of a data import.
 * In addition to persist if the import is complete or not this class
 * keeps a track of all warning and error messages if any.
 *
 * @author Morgan Guimard
 * @version 2.5, 11/05/17
 * @since   V2.5
 */
@Entity
@Table(name = "IMPORT")
public class Import implements Serializable {

    @Column(length = 255)
    @Id
    private String id="";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "USER_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name = "USER_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User user;

    @ElementCollection
    @CollectionTable(name="IMPORT_WARNING",
            joinColumns = {
                    @JoinColumn(name = "IMPORT_ID", referencedColumnName = "ID")
            })
    private List<String> warnings;

    @ElementCollection
    @CollectionTable(name="IMPORT_ERROR", joinColumns = {
            @JoinColumn(name = "IMPORT_ID", referencedColumnName = "ID")
    })
    private List<String> errors;

    private String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private boolean pending;

    private boolean succeed;

    public Import() {
    }

    public Import(User user, String fileName) {
        this(user, fileName, new Date(), null, true, false);
    }

    public Import(User user, String fileName, Date startDate, Date endDate, boolean pending, boolean succeed) {
        this.user=user;
        this.fileName=fileName;
        this.id=UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.pending = pending;
        this.succeed = succeed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
