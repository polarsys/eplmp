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
package org.polarsys.eplmp.core.gcm;

import org.polarsys.eplmp.core.common.Account;

import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name="GCMACCOUNT")
@javax.persistence.Entity
public class GCMAccount implements Serializable {

    @Id
    @OneToOne
    private Account account;

    @NotNull
    private String gcmId;

    public GCMAccount() {
    }

    public GCMAccount(Account account, String gcmId) {
        this.account = account;
        this.gcmId = gcmId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GCMAccount that = (GCMAccount) o;
        return account.equals(that.account) && gcmId.equals(that.gcmId);
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + gcmId.hashCode();
        return result;
    }
}
