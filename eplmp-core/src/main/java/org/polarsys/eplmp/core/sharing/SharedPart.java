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

package org.polarsys.eplmp.core.sharing;


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.product.PartRevision;

import javax.persistence.*;
import java.util.Date;

/**
 * SharedPart permits the creation of permanent link to part for users that do not have an account.
 *
 * @author Morgan Guimard
 */

@Table(name="SHAREDPART")
@Entity
@NamedQueries({
        @NamedQuery(name="SharedPart.deleteSharesForGivenPart", query="DELETE FROM SharedPart sp WHERE sp.partRevision = :pPartR")
})
public class SharedPart extends SharedEntity{

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="PARTMASTER_PARTNUMBER", referencedColumnName="PARTMASTER_PARTNUMBER"),
            @JoinColumn(name="PARTREVISION_VERSION", referencedColumnName="VERSION"),
            @JoinColumn(name="ENTITY_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
    })
    private PartRevision partRevision;

    public SharedPart(){
    }

    public SharedPart(Workspace workspace, User author, Date expireDate, String password, PartRevision partRevision) {
        super(workspace, author, expireDate, password);
        this.partRevision = partRevision;
    }

    public SharedPart(Workspace workspace, User author, PartRevision partRevision) {
        super(workspace, author);
        this.partRevision = partRevision;
    }

    public SharedPart(Workspace workspace, User author, Date expireDate, PartRevision partRevision) {
        super(workspace, author, expireDate);
        this.partRevision = partRevision;
    }

    public SharedPart(Workspace workspace, User author, String password, PartRevision partRevision) {
        super(workspace, author, password);
        this.partRevision = partRevision;
    }

    public PartRevision getPartRevision() {
        return partRevision;
    }

    public void setPartRevision(PartRevision partRevision) {
        this.partRevision = partRevision;
    }
}
