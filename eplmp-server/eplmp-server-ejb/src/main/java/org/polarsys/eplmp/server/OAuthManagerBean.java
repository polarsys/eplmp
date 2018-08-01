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

package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.OAuthProvider;
import org.polarsys.eplmp.core.common.ProvidedAccount;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.OAuthProviderNotFoundException;
import org.polarsys.eplmp.core.exceptions.ProvidedAccountNotFoundException;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.IOAuthManagerLocal;
import org.polarsys.eplmp.server.dao.OAuthProviderDAO;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morgan Guimard
 */
@DeclareRoles({UserGroupMapping.ADMIN_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(IOAuthManagerLocal.class)
@Stateless(name = "OAuthManagerBean")
public class OAuthManagerBean implements IOAuthManagerLocal {

    private static final Logger LOGGER = Logger.getLogger(OAuthManagerBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private OAuthProviderDAO oAuthProviderDAO;

    @Inject
    private IAccountManagerLocal accountManager;

    @Inject
    private OauthConfig oauthConfig;

    @Override
    public List<OAuthProvider> getProviders() {
        // todo use language if authenticated
        return oAuthProviderDAO.getProviders();
    }

    @Override
    public OAuthProvider getProvider(int id) throws OAuthProviderNotFoundException {
        // todo use language if authenticated
        return oAuthProviderDAO.findProvider(id);
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public OAuthProvider createProvider(String name, boolean enabled, String authority, String issuer,
                                        String clientID, String jwsAlgorithm, String jwkSetURL, String redirectUri,
                                        String secret, String scope, String responseType, String authorizationEndpoint)
            throws AccountNotFoundException {

        accountManager.getMyAccount();
        OAuthProvider oAuthProvider = new OAuthProvider(name, enabled, authority, issuer, clientID, jwsAlgorithm, jwkSetURL, redirectUri, secret, scope, responseType, authorizationEndpoint);
        oAuthProviderDAO.createProvider(oAuthProvider);
        return oAuthProvider;
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public OAuthProvider updateProvider(int id, String name, boolean enabled, String authority, String issuer,
                                        String clientID, String jwsAlgorithm, String jwkSetURL, String redirectUri,
                                        String secret, String scope, String responseType, String authorizationEndpoint)
            throws AccountNotFoundException, OAuthProviderNotFoundException {


        OAuthProvider oAuthProvider = oAuthProviderDAO.findProvider(id);
        oAuthProvider.setName(name);
        oAuthProvider.setAuthority(authority);
        oAuthProvider.setEnabled(enabled);
        oAuthProvider.setIssuer(issuer);
        oAuthProvider.setClientID(clientID);
        oAuthProvider.setJwsAlgorithm(jwsAlgorithm);
        oAuthProvider.setJwkSetURL(jwkSetURL);
        oAuthProvider.setRedirectUri(redirectUri);
        oAuthProvider.setSecret(secret);
        oAuthProvider.setScope(scope);
        oAuthProvider.setResponseType(responseType);
        oAuthProvider.setAuthorizationEndpoint(authorizationEndpoint);

        return oAuthProvider;
    }

    @Override
    @RolesAllowed(UserGroupMapping.ADMIN_ROLE_ID)
    public void deleteProvider(int id) throws AccountNotFoundException, OAuthProviderNotFoundException {
        accountManager.getMyAccount();
        oAuthProviderDAO.removeProvider(id);
    }

    @Override
    public ProvidedAccount getProvidedAccount(int providerId, String sub) throws ProvidedAccountNotFoundException {
        return oAuthProviderDAO.findProvidedAccount(providerId, sub);
    }

    @Override
    public void createProvidedAccount(Account account, String sub, OAuthProvider provider) {
        ProvidedAccount providedAccount = new ProvidedAccount(account, provider, sub);
        em.persist(providedAccount);
        em.flush();
    }

    @Override
    public boolean isProvidedAccount(Account account) {
        return oAuthProviderDAO.hasProvidedAccount(account);
    }

    @Override
    @RolesAllowed({UserGroupMapping.ADMIN_ROLE_ID, UserGroupMapping.REGULAR_USER_ROLE_ID})
    public Integer getProviderId(Account account) {
        try {
            return oAuthProviderDAO.findProvidedAccount(account).getProvider().getId();
        } catch (ProvidedAccountNotFoundException e) {
            return null;
        }
    }

    @Override
    public String findAvailableLogin(String sub) {
        try {
            accountManager.getAccount(sub);
            return generateLogin(sub, 1);
        } catch (AccountNotFoundException e) {
            return sub;
        }
    }

    @Override
    public void loadProvidersFromProperties() {
        LOGGER.log(Level.INFO, "Configuring oauth providers");
        List<OAuthProvider> providers = oauthConfig.getProviders();

        for (OAuthProvider provider : providers) {
            try {
                OAuthProvider existingProvider = oAuthProviderDAO.findProvider(provider.getId());

                existingProvider.setName(provider.getName());
                existingProvider.setEnabled(provider.isEnabled());
                existingProvider.setAuthority(provider.getAuthority());
                existingProvider.setIssuer(provider.getIssuer());
                existingProvider.setClientID(provider.getClientID());
                existingProvider.setJwsAlgorithm(provider.getJwsAlgorithm());
                existingProvider.setJwkSetURL(provider.getJwkSetURL());
                existingProvider.setRedirectUri(provider.getRedirectUri());
                existingProvider.setSecret(provider.getSecret());
                existingProvider.setScope(provider.getScope());
                existingProvider.setResponseType(provider.getResponseType());
                existingProvider.setAuthorizationEndpoint(provider.getAuthorizationEndpoint());

                LOGGER.log(Level.INFO, "Provider updated");
            } catch (OAuthProviderNotFoundException e) {
                oAuthProviderDAO.createProvider(provider);
                LOGGER.log(Level.INFO, "Provider created");
            }
        }

    }

    private String generateLogin(String sub, int start) {
        String tryWith = sub + "-" + start;
        try {
            accountManager.getAccount(tryWith);
            // try next...
            return generateLogin(sub, start + 1);
        } catch (AccountNotFoundException e) {
            return tryWith;
        }
    }


}
