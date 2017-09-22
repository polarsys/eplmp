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

package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentLink;

import java.io.Serializable;

/**
 * This class is used to carry additional information along the document
 * link itself. More precisely, this information is the target document
 * iteration to consider which may vary according to the chosen
 * resolution process.
 *
 * As a reminder, the stored {@link DocumentLink} targets
 * a document revision not a document iteration.
 *
 * Instances of this class are not persisted.
 *
 * @author Morgan Guimard
 */
public class ResolvedDocumentLink implements Serializable {
    private DocumentLink documentLink;
    private DocumentIteration documentIteration;

    public ResolvedDocumentLink() {
    }

    public ResolvedDocumentLink(DocumentLink documentLink, DocumentIteration documentIteration) {
        this.documentLink = documentLink;
        this.documentIteration = documentIteration;
    }

    public DocumentLink getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(DocumentLink documentLink) {
        this.documentLink = documentLink;
    }

    public DocumentIteration getDocumentIteration() {
        return documentIteration;
    }

    public void setDocumentIteration(DocumentIteration documentIteration) {
        this.documentIteration = documentIteration;
    }
}
