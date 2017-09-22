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

package org.polarsys.eplmp.core.security;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Represents a password recovery request. This class makes the link between
 * the UUID of the request and the user who asked for it.
 * 
 * @author Florent Garin
 * @version 1.0, 01/04/11
 * @since   V1.0
 */
@Table(name="PASSWORDRECOVERYREQUEST")
@javax.persistence.Entity
public class PasswordRecoveryRequest implements java.io.Serializable {

    @Column(length = 255)
    @javax.persistence.Id
    private String uuid="";
    
    private String login;
    
    
    public PasswordRecoveryRequest() {
    }
    

    public static PasswordRecoveryRequest createPasswordRecoveryRequest(String login){
        PasswordRecoveryRequest passwdRR = new PasswordRecoveryRequest();
        passwdRR.setLogin(login);
        passwdRR.setUuid(UUID.randomUUID().toString());
        return passwdRR;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof PasswordRecoveryRequest))
            return false;
        PasswordRecoveryRequest passwdRR = (PasswordRecoveryRequest) pObj;
        return passwdRR.uuid.equals(uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
