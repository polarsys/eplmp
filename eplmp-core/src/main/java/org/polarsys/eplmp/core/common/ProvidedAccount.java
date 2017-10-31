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

package org.polarsys.eplmp.core.common;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The ProvidedAccount class maps an account to a provider and the associated subject
 *
 * @author Morgan Guimard
 */
@Table(name = "PROVIDEDACCOUNT")
@Entity
@IdClass(org.polarsys.eplmp.core.common.ProvidedAccountKey.class)
@NamedQueries({
        @NamedQuery(name = "ProvidedAccount.getProvidedAccount", query = "SELECT p FROM ProvidedAccount p WHERE p.provider.id = :id AND p.sub = :sub"),
        @NamedQuery(name = "ProvidedAccount.getProvidedAccountFromAccount", query = "SELECT p FROM ProvidedAccount p WHERE p.account = :account")
})
public class ProvidedAccount implements Serializable {

    @Id
    @JoinColumn(name = "ID")
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private OAuthProvider provider;

    @Id
    private String sub;

    @Id
    @JoinColumn(name = "LOGIN", unique = true)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Account account;

    public ProvidedAccount() {
    }

    public ProvidedAccount(Account account, OAuthProvider provider, String sub) {
        this.account = account;
        this.provider = provider;
        this.sub = sub;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public void setProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
