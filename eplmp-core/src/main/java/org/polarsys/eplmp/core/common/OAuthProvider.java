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

package org.polarsys.eplmp.core.common;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The OAuthProvider class wraps an OAuth provider parameters.
 *
 * @author Morgan Guimard
 * @version 2.5, 29/11/17
 * @since V2.5
 */
@Table(name = "OAUTHPROVIDER")
@javax.persistence.Entity
@NamedQueries({
        @NamedQuery(name = "OAuthProvider.findAll", query = "SELECT o FROM OAuthProvider o")
})
public class OAuthProvider implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String name;
    private boolean enabled;
    private String authority;
    private String issuer;
    private String clientID;
    private String jwsAlgorithm;
    private String jwkSetURL;
    private String redirectUri;
    private String secret;
    private String scope;
    private String responseType;
    private String authorizationEndpoint;

    public OAuthProvider() {
    }

    public OAuthProvider(String name, boolean enabled, String authority, String issuer,
                         String clientID, String jwsAlgorithm, String jwkSetURL, String redirectUri,
                         String secret, String scope, String responseType, String authorizationEndpoint) {
        this.name = name;
        this.enabled = enabled;
        this.authority = authority;
        this.issuer = issuer;
        this.clientID = clientID;
        this.jwsAlgorithm = jwsAlgorithm;
        this.jwkSetURL = jwkSetURL;
        this.redirectUri = redirectUri;
        this.secret = secret;
        this.scope = scope;
        this.responseType = responseType;
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getJwsAlgorithm() {
        return jwsAlgorithm;
    }

    public void setJwsAlgorithm(String jwsAlgorithm) {
        this.jwsAlgorithm = jwsAlgorithm;
    }

    public String getJwkSetURL() {
        return jwkSetURL;
    }

    public void setJwkSetURL(String jwkSetURL) {
        this.jwkSetURL = jwkSetURL;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

}
