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
import org.polarsys.eplmp.core.document.DocumentRevision;

import javax.persistence.*;
import java.util.Date;

/**
 * SharedDocument permits the creation of permanent link to document for users that do not have an account.
 *
 * @author Morgan Guimard
 */

@Table(name="SHAREDDOCUMENT")
@Entity
@NamedQueries({
        @NamedQuery(name="SharedDocument.deleteSharesForGivenDocument", query="DELETE FROM SharedDocument sd WHERE sd.documentRevision = :pDocR"),
})
public class SharedDocument extends SharedEntity{

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "DOCUMENTMASTER_ID", referencedColumnName = "DOCUMENTMASTER_ID"),
            @JoinColumn(name = "DOCUMENTREVISION_VERSION", referencedColumnName = "VERSION"),
            @JoinColumn(name = "ENTITY_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private DocumentRevision documentRevision;

    public SharedDocument(){
    }

    public SharedDocument(Workspace workspace, User author, Date expireDate, String password, DocumentRevision documentRevision) {
        super(workspace, author, expireDate, password);
        this.documentRevision = documentRevision;
    }

    public SharedDocument(Workspace workspace, User author, DocumentRevision documentRevision) {
        super(workspace, author);
        this.documentRevision = documentRevision;
    }

    public SharedDocument(Workspace workspace, User author, Date expireDate, DocumentRevision documentRevision) {
        super(workspace, author, expireDate);
        this.documentRevision = documentRevision;
    }

    public SharedDocument(Workspace workspace, User author, String password, DocumentRevision documentRevision) {
        super(workspace, author, password);
        this.documentRevision = documentRevision;
    }

    public DocumentRevision getDocumentRevision() {
        return documentRevision;
    }

    public void setDocumentRevision(DocumentRevision documentRevision) {
        this.documentRevision = documentRevision;
    }
}
