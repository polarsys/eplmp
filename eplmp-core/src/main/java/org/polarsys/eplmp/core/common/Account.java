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

package org.polarsys.eplmp.core.common;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * The Account class holds personal user data applicable inside the whole application.
 * However {@link User} objects encapsulate personal information
 * only in the context of a particular workspace.
 *
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since V1.0
 */
@Table(name="ACCOUNT")
@javax.persistence.Entity
public class Account implements Serializable, Cloneable {


    @javax.persistence.Id
    private String login="";

    private String name;
    private String email;
    private String language;
    private String timeZone = "Europe/London";
    private boolean enabled;

    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;

    public Account(){
    }

    public Account(String pLogin){
        login = pLogin;
    }

    public Account(String pLogin, String pName, String pEmail, String pLanguage, Date pCreationDate,String pTimeZone) {
        login = pLogin;
        name = pName;
        email = pEmail;
        language = pLanguage;
        creationDate = pCreationDate;
        timeZone = pTimeZone;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String pLogin) {
        login=pLogin;
    }

    public String getName() {
        return name;
    }
    public void setName(String pName) {
        name = pName;
    }

    public void setEmail(String pEmail) {
        email = pEmail;
    }
    public String getEmail() {
        return email;
    }

    public void setLanguage(String pLanguage) {
        language = pLanguage;
    }
    public String getLanguage() {
        return language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setCreationDate(Date pCreationDate) {
        creationDate = pCreationDate;
    }
    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Locale getLocale() {
        return new Locale(this.language);
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Account)){
            return false;
        }
        Account account = (Account) pObj;
        return account.login.equals(login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    /**
     * perform a deep clone operation
     */
    @Override
    public Account clone() {
        Account clone;
        try {
            clone = (Account) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        clone.creationDate = (Date) creationDate.clone();
        return clone;
    }
}
