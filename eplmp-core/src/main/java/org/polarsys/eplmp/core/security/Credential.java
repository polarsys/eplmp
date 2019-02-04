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

package org.polarsys.eplmp.core.security;

import org.polarsys.eplmp.core.util.HashUtils;

import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Useful class for storing credential, login/password pair, to the persistence
 * storage. 
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="CREDENTIAL")
@javax.persistence.Entity
@NamedQuery(name="Credential.authenticate", query = "SELECT c FROM Credential c WHERE :login = c.login AND :password = c.password")
public class Credential implements java.io.Serializable {


    @javax.persistence.Id
    private String login="";
    
    private String password;

    private static final Logger LOGGER = Logger.getLogger(Credential.class.getName());

    public Credential() {
    }
    
    public static Credential createCredential(String pLogin, String pClearPassword, String pAlgorithm){
        Credential credential = new Credential();
        credential.login = pLogin;
        try {
            credential.password= HashUtils.digest(pClearPassword, pAlgorithm);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException pEx) {
            LOGGER.log(Level.SEVERE, null, pEx);
        }
        return credential;
    }

    public boolean authenticate(String pPassword, String pAlgorithm){
        try {
            return password != null && pPassword != null && HashUtils.digest(pPassword, pAlgorithm).equals(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException pEx) {
            LOGGER.log(Level.SEVERE, null, pEx);
            return false;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
