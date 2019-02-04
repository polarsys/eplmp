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

package org.polarsys.eplmp.core.meta;

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Embeddable class to track status change.
 * Actually it gathers an author {@code statusChangeAuthor}
 * and a date {@code statusModificationDate} attributes.
 *
 * @author Charles Fallourd
 * @version 2.5, 27/02/15
 * @since V2.5
 */
@Embeddable
public class StatusChange implements Serializable{


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="USER_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name="USER_WORKSPACE",referencedColumnName = "WORKSPACE_ID")})
    private User statusChangeAuthor;

    @Temporal(TemporalType.TIMESTAMP)
    private Date statusModificationDate;

    public Date getStatusModificationDate() {
        return statusModificationDate;
    }

    public void setStatusModificationDate(Date statusModificationDate) {
        this.statusModificationDate = statusModificationDate;
    }

    public User getStatusChangeAuthor() {
        return statusChangeAuthor;
    }

    public void setStatusChangeAuthor(User statusChangeAuthor) {
        this.statusChangeAuthor = statusChangeAuthor;
    }

}
