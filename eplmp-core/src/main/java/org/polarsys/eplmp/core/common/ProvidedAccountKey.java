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

import java.io.Serializable;

/**
 * Identity class of {@link ProvidedAccount} objects.
 *
 * @author Morgan Guimard
 */
public class ProvidedAccountKey implements Serializable {

    private int provider;
    private String sub;
    private String account;

    public ProvidedAccountKey() {
    }

    public ProvidedAccountKey(int provider, String sub, String account) {
        this.provider = provider;
        this.sub = sub;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProvidedAccountKey that = (ProvidedAccountKey) o;

        if (provider != that.provider) return false;
        if (sub != null ? !sub.equals(that.sub) : that.sub != null) return false;
        return !(account != null ? !account.equals(that.account) : that.account != null);

    }

    @Override
    public int hashCode() {
        int result = provider;
        result = 31 * result + (sub != null ? sub.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
