/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.OAuthProvider;
import org.polarsys.eplmp.core.common.ProvidedAccount;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.OAuthProviderNotFoundException;
import org.polarsys.eplmp.core.exceptions.ProvidedAccountNotFoundException;

import java.util.List;

/**
 * @author Morgan Guimard
 */
public interface IOAuthManagerLocal {

    List<OAuthProvider> getProviders();

    OAuthProvider getProvider(int id) throws OAuthProviderNotFoundException;

    OAuthProvider createProvider(String name, boolean enabled, String authority, String issuer,
                                 String clientID, String jwsAlgorithm, String jwkSetURL, String redirectUri,
                                 String secret, String scope, String responseType, String authorizationEndpoint) throws AccountNotFoundException;

    OAuthProvider updateProvider(int id, String name, boolean enabled, String authority, String issuer,
                                 String clientID, String jwsAlgorithm, String jwkSetURL, String redirectUri,
                                 String secret, String scope, String responseType, String authorizationEndpoint) throws AccountNotFoundException, OAuthProviderNotFoundException;

    void deleteProvider(int id) throws AccountNotFoundException, OAuthProviderNotFoundException;

    ProvidedAccount getProvidedAccount(int providerId, String sub) throws ProvidedAccountNotFoundException;


    void createProvidedAccount(Account account, String sub, OAuthProvider provider);

    boolean isProvidedAccount(Account account);

    String findAvailableLogin(String sub);
}
